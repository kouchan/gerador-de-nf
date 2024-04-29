package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegimeTributacaoPFTest {

    @Test
    public void testRegimeTributario() {
        // Criação do objeto a ser testado
        RegimeTributacaoPF regimeTributacaoPF = new RegimeTributacaoPF();

        // Teste para regimeTributario sempre retornando false
        assertFalse(regimeTributacaoPF.regimeTributario(RegimeTributacaoPJ.LUCRO_REAL));
        assertFalse(regimeTributacaoPF.regimeTributario(RegimeTributacaoPJ.LUCRO_PRESUMIDO));
        assertFalse(regimeTributacaoPF.regimeTributario(RegimeTributacaoPJ.SIMPLES_NACIONAL));
        assertFalse(regimeTributacaoPF.regimeTributario(RegimeTributacaoPJ.OUTROS));
    }

    @Test
    public void testCalculaPercentualAliquota() {
        // Criação do objeto a ser testado
        RegimeTributacaoPF regimeTributacaoPF = new RegimeTributacaoPF();

        // Teste para valorTotalItens menor que 500
        assertEquals(0d, regimeTributacaoPF.calculaPercentualAliquota(499.99));

        // Teste para valorTotalItens igual a 500
        assertEquals(0.12d, regimeTributacaoPF.calculaPercentualAliquota(500.0));

        // Teste para valorTotalItens entre 500 e 2000
        assertEquals(0.12d, regimeTributacaoPF.calculaPercentualAliquota(1500.0));

        // Teste para valorTotalItens igual a 2000
        assertEquals(0.12d, regimeTributacaoPF.calculaPercentualAliquota(2000.0));

        // Teste para valorTotalItens entre 2000 e 3500
        assertEquals(0.15d, regimeTributacaoPF.calculaPercentualAliquota(2500.0));

        // Teste para valorTotalItens igual a 3500
        assertEquals(0.15d, regimeTributacaoPF.calculaPercentualAliquota(3500.0));

        // Teste para valorTotalItens maior que 3500
        assertEquals(0.17d, regimeTributacaoPF.calculaPercentualAliquota(4000.0));
    }
}
