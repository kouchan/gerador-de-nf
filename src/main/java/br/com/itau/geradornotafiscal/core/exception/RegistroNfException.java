package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class RegistroNfException extends IntegrationException {
    public RegistroNfException(NotaFiscal notaFiscal) {
        super("Erro ao registrar a nota fiscal: "+  notaFiscal.getIdNotaFiscal() +", Não pudemos continuar com a operação.");
    }
}
