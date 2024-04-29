package br.com.itau.calculadoratributos;

import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoException;
import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.core.service.CalculadoraAliquotaService;
import br.com.itau.geradornotafiscal.core.service.TaxaAliquotaService;
import br.com.itau.geradornotafiscal.core.usecase.impl.GerarNotaFiscalUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        // Create and add Endereco to the Destinatario
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

        // Create and add Endereco to the Destinatario
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.ENTREGA)
                .regiao(Regiao.SUDESTE)
                .build();

        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.JURIDICA)
                .regimeTributacaoPJ(RegimeTributacaoPJ.LUCRO_PRESUMIDO)
                .enderecos(List.of(endereco)).build();

        // Create and add items to the Pedido
        Item item = Item.builder()
                .valorUnitario(1000)
                .quantidade(6)
                .build();

        Pedido pedido = Pedido.builder()
                .valorTotalItens(6000)
                .valorFrete(100)
                .destinatario(destinatario)
                .itens(List.of(item)).build();

        double aliquotaEsperada = 0.20;

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
                .orElseThrow(() -> new RegimeTributacaoException(any()))).thenReturn(taxaAliquotaService);

        NotaFiscal notaFiscal = geradorNotaFiscalService.gerarNotaFiscal(pedido);

        assertEquals(pedido.getValorTotalItens(), notaFiscal.getValorTotalItens());
        assertEquals(1, notaFiscal.getItens().size());
        assertEquals(0.20 * item.getValorUnitario(), notaFiscal.getItens().get(0).getValorTributoItem());
    }

}