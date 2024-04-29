package br.com.itau.geradornotafiscal.core.service.pedido;

import br.com.itau.geradornotafiscal.core.exception.PedidoInconsistenteException;
import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.service.ValidaPedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidaPedidoServiceImpl implements ValidaPedidoService {
    private static final String ERROR_MSG = "Valor total e soma dos itens não condizem";
    @Override
    public void verificaConsistenciaValorTotal(Pedido pedido) {
        //WARNING Caso seja um valor astronomico, pode ser que de algum problema de desempenho
        Double somaValorTotalItens = pedido.getItens()
                .stream()
                .mapToDouble(item -> (item.getValorUnitario()* item.getQuantidade()))
                .reduce(0,Double::sum);
        if(!somaValorTotalItens.equals(pedido.getValorTotalItens())){
            log.error(ERROR_MSG);
            throw new PedidoInconsistenteException(pedido, somaValorTotalItens, ERROR_MSG);
        }
    }
}
