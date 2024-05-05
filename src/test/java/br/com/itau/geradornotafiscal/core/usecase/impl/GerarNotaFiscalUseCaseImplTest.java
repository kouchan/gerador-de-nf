package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoException;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacao;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.core.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerarNotaFiscalUseCaseImplTest {

    @Mock
    private CalculadoraAliquotaService calculadoraAliquotaService;
    @Mock
    private TaxaAliquotaService taxaAliquotaService;
    @Mock
    private EstoqueService estoqueService;
    @Mock
    private RegistroService registroService;
    @Mock
    private EntregaService entregaService;
    @Mock
    private FinanceiroService financeiroService;
    @Mock
    private FreteService freteService;

    @InjectMocks
    private GerarNotaFiscalUseCaseImpl gerarNotaFiscalUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGerarNotaFiscal_PessoaFisica() {
        // Configuração dos mocks
        when(freteService.calculaValorFreteComPercentual(any(),any())).thenReturn(104.80);
        when(calculadoraAliquotaService.calcularAliquota(anyList(), anyDouble()))
                .thenReturn(Collections.singletonList(new ItemNotaFiscal()));
        when(estoqueService.enviarNotaFiscalParaBaixaEstoque(any())).thenReturn(Mono.empty());
        when(entregaService.agendarEntrega(any())).thenReturn(Mono.empty());
        when(financeiroService.enviarNotaFiscalParaContasReceber(any())).thenReturn(Mono.empty());
        when(registroService.registrarNotaFiscal(any())).thenReturn(Mono.empty());

        // Criação de um pedido de teste
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.ENTREGA)
                .regiao(Regiao.SUDESTE)
                .build();

        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.FISICA)
                .enderecos(List.of(endereco)).build();

        // Create and add items to the Pedido
        Item item = Item.builder()
                .valorUnitario(100)
                .quantidade(4)
                .build();
        Pedido pedido = Pedido.builder()
                .valorTotalItens(400)
                .valorFrete(100)
                .destinatario(destinatario)
                .itens(List.of(item))
                .build();

        // Chamada do método a ser testado
        NotaFiscal notaFiscal = gerarNotaFiscalUseCase.gerarNotaFiscal(pedido);

        // Verificações
        assertNotNull(notaFiscal);
        assertEquals(1, notaFiscal.getItens().size());
        assertEquals(104.80, notaFiscal.getValorFrete());
        verify(calculadoraAliquotaService, times(1)).calcularAliquota(anyList(), anyDouble());
        verify(estoqueService, times(1)).enviarNotaFiscalParaBaixaEstoque(any());
        verify(entregaService, times(1)).agendarEntrega(any());
        verify(financeiroService, times(1)).enviarNotaFiscalParaContasReceber(any());
    }


    @Test
    void testGerarNotaFiscalTipoPessoaJuridica() {
        // Configuração dos mocks
        when(freteService.calculaValorFreteComPercentual(any(),any())).thenReturn(104.80);
        when(calculadoraAliquotaService.calcularAliquota(anyList(), anyDouble()))
                .thenReturn(Collections.singletonList(new ItemNotaFiscal()));
        when(estoqueService.enviarNotaFiscalParaBaixaEstoque(any())).thenReturn(Mono.empty());
        when(entregaService.agendarEntrega(any())).thenReturn(Mono.empty());
        when(financeiroService.enviarNotaFiscalParaContasReceber(any())).thenReturn(Mono.empty());
        when(registroService.registrarNotaFiscal(any())).thenReturn(Mono.empty());
        when(taxaAliquotaService.regimeTributario(any(RegimeTributacao.class))).thenReturn(true);

        List<TaxaAliquotaService> taxaAliquotaServices = new ArrayList<>();
        taxaAliquotaServices.add(taxaAliquotaService);

        // Injetar a lista de serviços na sua classe sob teste
        GerarNotaFiscalUseCaseImpl gerarNotaFiscalUseCase = new GerarNotaFiscalUseCaseImpl(calculadoraAliquotaService, taxaAliquotaServices, estoqueService, registroService, entregaService, financeiroService, freteService);


        // Criação de um pedido de teste
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.ENTREGA)
                .regiao(Regiao.SUDESTE)
                .build();

        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.JURIDICA)
                .regimeTributacao(RegimeTributacao.SIMPLES_NACIONAL)
                .enderecos(List.of(endereco)).build();

        // Create and add items to the Pedido
        Item item = Item.builder()
                .valorUnitario(100)
                .quantidade(4)
                .build();
        Pedido pedido = Pedido.builder()
                .valorTotalItens(400)
                .valorFrete(100)
                .destinatario(destinatario)
                .itens(List.of(item))
                .build();

        // Chamada do método a ser testado
        NotaFiscal notaFiscal = gerarNotaFiscalUseCase.gerarNotaFiscal(pedido);

        // Verificações
        assertNotNull(notaFiscal);
        assertEquals(1, notaFiscal.getItens().size());
        assertEquals(104.80, notaFiscal.getValorFrete());
        verify(calculadoraAliquotaService, times(1)).calcularAliquota(anyList(), anyDouble());
        verify(estoqueService, times(1)).enviarNotaFiscalParaBaixaEstoque(any());
        verify(entregaService, times(1)).agendarEntrega(any());
        verify(financeiroService, times(1)).enviarNotaFiscalParaContasReceber(any());
    }

    @Test
    void testGerarNotaFiscalRegimeTributacaoNaoEncontrado() {
        // Configuração dos mocks
        when(freteService.calculaValorFreteComPercentual(any(),any())).thenReturn(104.80);
        when(calculadoraAliquotaService.calcularAliquota(anyList(), anyDouble()))
                .thenReturn(Collections.singletonList(new ItemNotaFiscal()));
        when(estoqueService.enviarNotaFiscalParaBaixaEstoque(any())).thenReturn(Mono.empty());
        when(entregaService.agendarEntrega(any())).thenReturn(Mono.empty());
        when(financeiroService.enviarNotaFiscalParaContasReceber(any())).thenReturn(Mono.empty());
        when(registroService.registrarNotaFiscal(any())).thenReturn(Mono.empty());
        when(taxaAliquotaService.regimeTributario(any(RegimeTributacao.class))).thenReturn(false);

        List<TaxaAliquotaService> taxaAliquotaServices = new ArrayList<>();
        taxaAliquotaServices.add(taxaAliquotaService);

        // Injetar a lista de serviços na sua classe sob teste
        GerarNotaFiscalUseCaseImpl gerarNotaFiscalUseCase = new GerarNotaFiscalUseCaseImpl(calculadoraAliquotaService, taxaAliquotaServices, estoqueService, registroService, entregaService, financeiroService, freteService);


        // Criação de um pedido de teste
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.ENTREGA)
                .regiao(Regiao.SUDESTE)
                .build();

        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.JURIDICA)
                .regimeTributacao(RegimeTributacao.OUTROS)
                .enderecos(List.of(endereco)).build();

        // Create and add items to the Pedido
        Item item = Item.builder()
                .valorUnitario(100)
                .quantidade(4)
                .build();
        Pedido pedido = Pedido.builder()
                .valorTotalItens(400)
                .valorFrete(100)
                .destinatario(destinatario)
                .itens(List.of(item))
                .build();


        // Definir comportamento esperado dos mocks

        // Executar o método a ser testado e verificar a exceção lançada
        assertThrows(RegimeTributacaoException.class, () -> gerarNotaFiscalUseCase.gerarNotaFiscal(pedido));
    }
}
