package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.exception.EnderecoNaoCadastradoException;
import br.com.itau.geradornotafiscal.core.exception.RegiaoNaoCadastradoException;
import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoException;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.core.service.*;
import br.com.itau.geradornotafiscal.core.usecase.GerarNotaFiscalUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GerarNotaFiscalUseCaseImpl implements GerarNotaFiscalUseCase {
    private final List<FreteService> freteServices;
    private final List<TaxaAliquotaService> taxaAliquotaServices;
    private final CalculadoraAliquotaService calculadoraAliquotaService;
    private final EstoqueService estoqueService;
    private final RegistroService registroService;
    private final EntregaService entregaService;
    private final FinanceiroService financeiroService;


    @Override
    public NotaFiscal gerarNotaFiscal(final Pedido pedido) {
        Destinatario destinatario = pedido.getDestinatario();
        TipoPessoa tipoPessoa = destinatario.getTipoPessoa();
        List<ItemNotaFiscal> itemNotaFiscalList;


        var regimeTributacao = destinatario.getRegimeTributacao();
        TaxaAliquotaService taxaAliquotaService = taxaAliquotaServices
                .stream()
                .filter(aliquotaService -> aliquotaService.regimeTributario(regimeTributacao, tipoPessoa))
                .findFirst()
                .orElseThrow(() -> new RegimeTributacaoException(regimeTributacao));
        Double aliquota = taxaAliquotaService.calculaPercentualAliquota(pedido.getValorTotalItens());
        itemNotaFiscalList = calculadoraAliquotaService.calcularAliquota(pedido.getItens(), aliquota);

        //Regras diferentes para frete
        var regiao = destinatario.getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst()
                .orElseThrow(() -> new EnderecoNaoCadastradoException(pedido));

        var freteService = freteServices.stream()
                .filter(serviceName -> serviceName.getClass()
                        .getName().toLowerCase()
                        .contains(regiao.name().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RegiaoNaoCadastradoException(pedido));

        Double valorFreteComPercentual = freteService.calculaValorFreteComPercentual(pedido);

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
        );
        resultadoComunicacaoExterna.blockLast();

        return notaFiscal;
    }
}