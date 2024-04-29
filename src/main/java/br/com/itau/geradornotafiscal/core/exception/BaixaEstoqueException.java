package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class BaixaEstoqueException extends IntegrationException {
    public BaixaEstoqueException(NotaFiscal notaFiscal) {
        super("Erro ao dar baixa no estoque para a nota fiscal: " + notaFiscal.getIdNotaFiscal());
    }
}
