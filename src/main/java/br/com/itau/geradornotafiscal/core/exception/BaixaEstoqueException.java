package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class BaixaEstoqueOcorreuUmErro extends IntegrationException {
    public BaixaEstoqueOcorreuUmErro(NotaFiscal notaFiscal) {
        super("Erro ao dar baixa no estoque para a nota fiscal: " + notaFiscal.getIdNotaFiscal());
    }
}
