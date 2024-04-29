package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import lombok.Getter;

@Getter
public class RegimeTributacaoException extends RuntimeException {
    private RegimeTributacaoPJ regimeTributacaoPJ;
    public RegimeTributacaoException(RegimeTributacaoPJ regimeTributacaoPJ) {
        super("Regime de tributacao PJ nao encontrada: " + regimeTributacaoPJ.toString());
        this.regimeTributacaoPJ = regimeTributacaoPJ;
    }
}
