package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegimeTributacaoLucroRealTest {

    @Test
    public void testRegimeTributario() {
        // Criação do objeto a ser testado
        RegimeTributacaoLucroReal regimeTributacaoLucroReal = new RegimeTributacaoLucroReal();

        // Teste para regimeTributario com LUCRO_REAL
        assertTrue(regimeTributacaoLucroReal.regimeTributario(RegimeTributacao.LUCRO_REAL));

        // Teste para regimeTributario com outro regime
        assertFalse(regimeTributacaoLucroReal.regimeTributario(RegimeTributacao.OUTROS));
    }

    @Test
    public void testCalculaPercentualAliquota() {
        // Criação do objeto a ser testado
        RegimeTributacaoLucroReal regimeTributacaoLucroReal = new RegimeTributacaoLucroReal();

        // Teste para valorTotalItens menor que 1000
        assertEquals(0.03, regimeTributacaoLucroReal.calculaPercentualAliquota(999.99));

        // Teste para valorTotalItens igual a 1000
        assertEquals(0.09, regimeTributacaoLucroReal.calculaPercentualAliquota(1000.0));

        // Teste para valorTotalItens entre 1000 e 2000
        assertEquals(0.09, regimeTributacaoLucroReal.calculaPercentualAliquota(1500.0));

        // Teste para valorTotalItens igual a 2000
        assertEquals(0.09, regimeTributacaoLucroReal.calculaPercentualAliquota(2000.0));

        // Teste para valorTotalItens entre 2000 e 5000
        assertEquals(0.15, regimeTributacaoLucroReal.calculaPercentualAliquota(3500.0));

        // Teste para valorTotalItens igual a 5000
        assertEquals(0.15, regimeTributacaoLucroReal.calculaPercentualAliquota(5000.0));

        // Teste para valorTotalItens maior que 5000
        assertEquals(0.20, regimeTributacaoLucroReal.calculaPercentualAliquota(6000.0));
    }
}
