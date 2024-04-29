package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class ContasAReceberException extends IntegrationException {
    public ContasAReceberException(NotaFiscal notaFiscal) {
        super("Erro ao registrar em contas a receber para a nota fiscal: " + notaFiscal.getIdNotaFiscal());
    }
}
