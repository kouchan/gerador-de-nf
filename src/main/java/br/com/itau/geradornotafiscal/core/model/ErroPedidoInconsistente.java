package br.com.itau.geradornotafiscal.core.model;

public record ErroPedidoInconsistente(
        String message,
        Integer idPedido,
        Double valorTotalPedido,
        Double valorTotalItens
) {
}
