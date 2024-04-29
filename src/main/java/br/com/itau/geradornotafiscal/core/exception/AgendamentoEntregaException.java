package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class AgendamentoEntregaOcorreuUmErro extends IntegrationException {
    public AgendamentoEntregaOcorreuUmErro(NotaFiscal notaFiscal) {
        super("Erro ao realizar agendamento de entrega para a nota fiscal: " + notaFiscal.getIdNotaFiscal());
    }
}
