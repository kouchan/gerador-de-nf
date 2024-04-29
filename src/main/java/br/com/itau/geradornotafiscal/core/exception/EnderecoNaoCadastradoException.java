package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.Pedido;

public class EnderecoNaoCadastradoException extends IntegrationException {
    public EnderecoNaoCadastradoException(Pedido pedido) {
        super("Erro ao registrar a nota fiscal para o pedido: "+  pedido.getIdPedido() +", Não pudemos continuar com a operação.");
    }
}
