package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.RegimeTributacaoPJ;

public class RegimeTributacaoNaoEncontrada extends RuntimeException {
    public RegimeTributacaoNaoEncontrada(RegimeTributacaoPJ regimeTributacaoPJ) {
        super("Regime de tributacao PJ nao encontrada: " + regimeTributacaoPJ.toString());
    }
}
