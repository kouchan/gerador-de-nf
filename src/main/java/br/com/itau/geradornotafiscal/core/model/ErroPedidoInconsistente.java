package br.com.itau.geradornotafiscal.core.model;

public record PedidoInconsistenteErro(
        String message,
        String idPedido,
        String valorTotalPedido,
        String valorTotalItens
) {
}
