package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegimeTributacaoLucroPresumidoTest {

    @Test
    public void testRegimeTributario() {
        // Criação do objeto a ser testado
        RegimeTributacaoLucroPresumido regimeTributacaoLucroPresumido = new RegimeTributacaoLucroPresumido();

        // Teste para regimeTributario com LUCRO_PRESUMIDO
        assertTrue(regimeTributacaoLucroPresumido.regimeTributario(RegimeTributacaoPJ.LUCRO_PRESUMIDO));

        // Teste para regimeTributario com outro regime
        assertFalse(regimeTributacaoLucroPresumido.regimeTributario(RegimeTributacaoPJ.OUTROS));
    }

    @Test
    public void testCalculaPercentualAliquota() {
        // Criação do objeto a ser testado
        RegimeTributacaoLucroPresumido regimeTributacaoLucroPresumido = new RegimeTributacaoLucroPresumido();

        // Teste para valorTotalItens menor que 1000
        assertEquals(0.03, regimeTributacaoLucroPresumido.calculaPercentualAliquota(999.99));

        // Teste para valorTotalItens igual a 1000
        assertEquals(0.09, regimeTributacaoLucroPresumido.calculaPercentualAliquota(1000.0));

        // Teste para valorTotalItens entre 1000 e 2000
        assertEquals(0.09, regimeTributacaoLucroPresumido.calculaPercentualAliquota(1500.0));

        // Teste para valorTotalItens igual a 2000
        assertEquals(0.09, regimeTributacaoLucroPresumido.calculaPercentualAliquota(2000.0));

        // Teste para valorTotalItens entre 2000 e 5000
        assertEquals(0.16, regimeTributacaoLucroPresumido.calculaPercentualAliquota(3500.0));

        // Teste para valorTotalItens igual a 5000
        assertEquals(0.16, regimeTributacaoLucroPresumido.calculaPercentualAliquota(5000.0));

        // Teste para valorTotalItens maior que 5000
        assertEquals(0.20, regimeTributacaoLucroPresumido.calculaPercentualAliquota(6000.0));
    }
}