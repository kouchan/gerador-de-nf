package br.com.itau.geradornotafiscal.core.service.frete;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FreteServiceImplTest {

    @Test
    public void testCalculaValorFreteComPercentual() {
        // Criação do objeto a ser testado
        FreteServiceImpl freteService = new FreteServiceImpl();

        // Teste para região NORTE
        assertEquals(108.00, freteService.calculaValorFreteComPercentual(createPedido(100.00), Regiao.NORTE));

        // Teste para região NORDESTE
        assertEquals(108.5, freteService.calculaValorFreteComPercentual(createPedido(100.0), Regiao.NORDESTE));

        // Teste para região CENTRO_OESTE
        assertEquals(107.0, freteService.calculaValorFreteComPercentual(createPedido(100.0), Regiao.CENTRO_OESTE));

        // Teste para região SUDESTE
        assertEquals(104.8, freteService.calculaValorFreteComPercentual(createPedido(100.0), Regiao.SUDESTE));

        // Teste para região SUL
        assertEquals(106.0, freteService.calculaValorFreteComPercentual(createPedido(100.0), Regiao.SUL));
    }

    private Pedido createPedido(double valorFrete) {
        return Pedido.builder().valorFrete(valorFrete).build();
    }
}
