package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.Pedido;

public class RegiaoNaoCadastradoException extends IntegrationException {
    public RegiaoNaoCadastradoException(Pedido pedido) {
        super("Erro ao registrar a nota fiscal para o pedido: "+  pedido.getIdPedido() +", Não pudemos continuar com a operação.");
    }
}
