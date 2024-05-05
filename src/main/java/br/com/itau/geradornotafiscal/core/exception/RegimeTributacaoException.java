package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacao;
import lombok.Getter;

@Getter
public class RegimeTributacaoException extends RuntimeException {
    private RegimeTributacao regimeTributacao;
    public RegimeTributacaoException(RegimeTributacao regimeTributacao) {
        super("Regime de tributacao PJ nao encontrada: " + regimeTributacao.toString());
        this.regimeTributacao = regimeTributacao;
    }
}
