package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import lombok.Getter;

@Getter
public class PedidoInconsistenteException extends RuntimeException{
    private Integer idPedido;
    private Double valorTotalPedido;
    private Double somaValorTotalItens;
    public PedidoInconsistenteException(Pedido pedido, Double somaValorTotalItens, String msg){
        super(msg);
        this.idPedido = pedido.getIdPedido();
        this.valorTotalPedido = pedido.getValorTotalItens();
        this.somaValorTotalItens = somaValorTotalItens;
    }
}
