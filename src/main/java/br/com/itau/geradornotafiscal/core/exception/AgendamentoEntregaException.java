package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;

public class AgendamentoEntregaException extends IntegrationException {
    public AgendamentoEntregaException(NotaFiscal notaFiscal) {
        super("Erro ao realizar agendamento de entrega para a nota fiscal: " + notaFiscal.getIdNotaFiscal());
    }
}
