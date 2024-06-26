package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.exception.EnderecoNaoCadastradoException;
import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoException;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.core.service.*;
import br.com.itau.geradornotafiscal.core.usecase.GerarNotaFiscalUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final FreteService freteService;

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
                    .orElseThrow(() -> new RegimeTributacaoException(regimeTributacaoPJ));
            Double aliquota = taxaAliquotaService.calculaPercentualAliquota(pedido.getValorTotalItens());
            itemNotaFiscalList = calculadoraAliquotaService.calcularAliquota(pedido.getItens(), aliquota);
        }

        //Regras diferentes para frete

        Regiao regiao = destinatario.getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst()
                .orElseThrow(() -> new EnderecoNaoCadastradoException(pedido));

        Double valorFreteComPercentual = freteService.calculaValorFreteComPercentual(pedido, regiao);

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

        Mono.just(registroService.registrarNotaFiscal(notaFiscal))
                .doOnError(throwable -> log.error("Erro ao registrar a nota fiscal! Não pudemos continuar com a operação."))
                .then();

        log.info("Iniciando chamadas em paralelo para os serviços");

        Flux<Void> resultadoComunicacaoExterna = Flux.merge(
            estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal).then(),
            entregaService.agendarEntrega(notaFiscal).then(),
            financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal).then()
        ).doOnError(throwable -> {
            log.error("Erro ao processar nota fiscal "+throwable.getMessage());

        });
        resultadoComunicacaoExterna.blockLast();

        return notaFiscal;
    }
}