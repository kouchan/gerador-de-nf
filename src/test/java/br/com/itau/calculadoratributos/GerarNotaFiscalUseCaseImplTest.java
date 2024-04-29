package br.com.itau.calculadoratributos;

import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoNaoEncontrada;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.service.CalculadoraAliquotaService;
import br.com.itau.geradornotafiscal.core.service.TaxaAliquotaService;
import br.com.itau.geradornotafiscal.core.usecase.impl.GerarNotaFiscalUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GerarNotaFiscalUseCaseImplTest {

    @InjectMocks
    private GerarNotaFiscalUseCaseImpl geradorNotaFiscalService;

    @Mock
    private CalculadoraAliquotaService calculadoraAliquotaService;

    @Mock
    private List<TaxaAliquotaService> taxaAliquotaServices;

    private TaxaAliquotaService taxaAliquotaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    @DisplayName("Dado pedidos para PF com valor total ate 500" +
            " Quando Calcular Aliquota" +
            " Entao Gerar NF com 1 item, ValorTributoItem 0")
    public void shouldGenerateNotaFiscalForTipoPessoaFisicaWithValorTotalItensLessThan500() {
        Pedido pedido = new Pedido();
        pedido.setValorTotalItens(400);
        pedido.setValorFrete(100);
        Destinatario destinatario = new Destinatario();
        destinatario.setTipoPessoa(TipoPessoa.FISICA);

        // Create and add Endereco to the Destinatario
        Endereco endereco = new Endereco();
        endereco.setFinalidade(Finalidade.ENTREGA);
        endereco.setRegiao(Regiao.SUDESTE);
        destinatario.setEnderecos(List.of(endereco));

        pedido.setDestinatario(destinatario);

        // Create and add items to the Pedido
        Item item = new Item();
        item.setValorUnitario(100);
        item.setQuantidade(4);
        pedido.setItens(List.of(item));



        //When calculadoraAliquotaProduto
        when(calculadoraAliquotaService.calcularAliquota(List.of(item), 0))
                .thenReturn(List.of(ItemNotaFiscal.builder()
                        .idItem("")
                        .descricao("")
                        .quantidade(item.getQuantidade())
                        .valorTributoItem(0)
                        .valorUnitario(item.getValorUnitario()).build()));


        NotaFiscal notaFiscal = geradorNotaFiscalService.gerarNotaFiscal(pedido);

        assertEquals(pedido.getValorTotalItens(), notaFiscal.getValorTotalItens());
        assertEquals(1, notaFiscal.getItens().size());
        assertEquals(0, notaFiscal.getItens().get(0).getValorTributoItem());
    }

    @Test
    @DisplayName("Dado pedidos para PJ e RegimeTributacaoPJ LUCRO_PRESUMIDO e com valor total maior que 5000" +
            " Quando Calcular Aliquota" +
            " Entao Gerar NF com 1 item, ValorTributoItem 0.20")
    public void shouldGenerateNotaFiscalForTipoPessoaJuridicaWithRegimeTributacaoLucroPresumidoAndValorTotalItensGreaterThan5000() {
        Pedido pedido = new Pedido();
        pedido.setValorTotalItens(6000);
        pedido.setValorFrete(100);
        Destinatario destinatario = new Destinatario();
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacaoPJ(RegimeTributacaoPJ.LUCRO_PRESUMIDO);

        // Create and add Endereco to the Destinatario
        Endereco endereco = new Endereco();
        endereco.setFinalidade(Finalidade.ENTREGA);
        endereco.setRegiao(Regiao.SUDESTE);
        destinatario.setEnderecos(Arrays.asList(endereco));

        pedido.setDestinatario(destinatario);

        // Create and add items to the Pedido
        Item item = new Item();
        item.setValorUnitario(1000);
        item.setQuantidade(6);
        pedido.setItens(Arrays.asList(item));

        Double aliquotaEsperada = 0.20;

        when(calculadoraAliquotaService.calcularAliquota(List.of(item), aliquotaEsperada))
                .thenReturn(List.of(ItemNotaFiscal.builder()
                        .idItem("")
                        .descricao("")
                        .quantidade(item.getQuantidade())
                        .valorTributoItem(item.getValorUnitario()*aliquotaEsperada)
                        .valorUnitario(item.getValorUnitario()).build()));

        when(taxaAliquotaServices.stream()
                .filter(aliquotaService -> aliquotaService.regimeTributario(any()))
                .findFirst()
                .orElseThrow(() -> new RegimeTributacaoNaoEncontrada(any()))).thenReturn(taxaAliquotaService);

        NotaFiscal notaFiscal = geradorNotaFiscalService.gerarNotaFiscal(pedido);

        assertEquals(pedido.getValorTotalItens(), notaFiscal.getValorTotalItens());
        assertEquals(1, notaFiscal.getItens().size());
        assertEquals(0.20 * item.getValorUnitario(), notaFiscal.getItens().get(0).getValorTributoItem());
    }

}