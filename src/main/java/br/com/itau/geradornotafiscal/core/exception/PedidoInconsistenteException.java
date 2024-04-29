package br.com.itau.geradornotafiscal.core.exception;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import lombok.Getter;

@Getter
public class PedidoInconsistenteErro extends RuntimeException{
    private Integer idPedido;
    private Double valorTotalPedido;
    private Double somaValorTotalItens;
    public PedidoInconsistenteErro(Pedido pedido, Double somaValorTotalItens, String msg){
        super(msg);
        this.idPedido = pedido.getIdPedido();
        this.valorTotalPedido = pedido.getValorTotalItens();
        this.somaValorTotalItens = somaValorTotalItens;
    }
}
