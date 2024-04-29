package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoNaoEncontrada;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.service.*;
import br.com.itau.geradornotafiscal.core.usecase.GerarNotaFiscalUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GerarNotaFiscalUseCaseImpl implements GerarNotaFiscalUseCase {
    private final CalculadoraAliquotaService calculadoraAliquotaService;
    private final List<TaxaAliquotaService> taxaAliquotaServices;
    private final EstoqueService estoqueService;
    private final RegistroService registroService;
    private final EntregaService entregaService;
    private final FinanceiroService financeiroService;

    @Override
    public NotaFiscal gerarNotaFiscal(final Pedido pedido) {
        Destinatario destinatario = pedido.getDestinatario();
        TipoPessoa tipoPessoa = destinatario.getTipoPessoa();
        List<ItemNotaFiscal> itemNotaFiscalList = new ArrayList<>();

        if (TipoPessoa.FISICA.equals(tipoPessoa)) {
            double valorTotalItens = pedido.getValorTotalItens();
            double aliquota;

            if (valorTotalItens < 500) {
                aliquota = 0;
            } else if (valorTotalItens <= 2000) {
                aliquota = 0.12;
            } else if (valorTotalItens <= 3500) {
                aliquota = 0.15;
            } else {
                aliquota = 0.17;
            }
            itemNotaFiscalList = calculadoraAliquotaService.calcularAliquota(pedido.getItens(), aliquota);
        } else if (TipoPessoa.JURIDICA.equals(tipoPessoa)) {

            RegimeTributacaoPJ regimeTributacaoPJ = destinatario.getRegimeTributacaoPJ();
            TaxaAliquotaService taxaAliquotaService = taxaAliquotaServices
                    .stream()
                    .filter(aliquotaService -> aliquotaService.regimeTributario(regimeTributacaoPJ))
                    .findFirst()
                    .orElseThrow(() -> new RegimeTributacaoNaoEncontrada(regimeTributacaoPJ));
            Double aliquota = taxaAliquotaService.calculaPercentualAliquota(pedido.getValorTotalItens());
            itemNotaFiscalList = calculadoraAliquotaService.calcularAliquota(pedido.getItens(), aliquota);
        }
        //Regras diferentes para frete

        Regiao regiao = destinatario.getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst()
                .orElse(null);

        double valorFreteComPercentual = calculaValorFreteComPercentual(pedido, regiao);

        // Create the NotaFiscal object
        String idNotaFiscal = UUID.randomUUID().toString();

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(idNotaFiscal)
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(valorFreteComPercentual)
                .itens(itemNotaFiscalList)
                .destinatario(pedido.getDestinatario())
                .build();

        log.info("Iniciando chamadas em paralelo para os serviços");

        Flux<Void> resultadoComunicacaoExterna = Flux.merge(
            estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal).then(),
            registroService.registrarNotaFiscal(notaFiscal).then(),
            entregaService.agendarEntrega(notaFiscal).then(),
            financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal).then()
        ).doOnError(throwable -> {
            log.error("Erro ao processar nota fiscal "+throwable.getMessage());

        });
        resultadoComunicacaoExterna.blockLast();

        return notaFiscal;
    }

    private static double calculaValorFreteComPercentual(Pedido pedido, Regiao regiao) {
        double valorFrete = pedido.getValorFrete();
        double valorFreteComPercentual = 0;

        if (regiao == Regiao.NORTE) {
            valorFreteComPercentual = valorFrete * 1.08;
        } else if (regiao == Regiao.NORDESTE) {
            valorFreteComPercentual = valorFrete * 1.085;
        } else if (regiao == Regiao.CENTRO_OESTE) {
            valorFreteComPercentual = valorFrete * 1.07;
        } else if (regiao == Regiao.SUDESTE) {
            valorFreteComPercentual = valorFrete * 1.048;
        } else if (regiao == Regiao.SUL) {
            valorFreteComPercentual = valorFrete * 1.06;
        }
        return valorFreteComPercentual;
    }
}