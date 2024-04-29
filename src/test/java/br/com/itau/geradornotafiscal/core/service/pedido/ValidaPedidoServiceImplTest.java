package br.com.itau.geradornotafiscal.core.service.pedido;

import br.com.itau.geradornotafiscal.core.exception.PedidoInconsistenteException;
import br.com.itau.geradornotafiscal.core.model.Item;
import br.com.itau.geradornotafiscal.core.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidaPedidoServiceImplTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private ValidaPedidoServiceImpl validaPedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerificaConsistenciaValorTotal() {
        // Criar itens para o pedido
        List<Item> itens = new ArrayList<>();
        itens.add(Item.builder().idItem("item1").descricao("Descrição do item 1").valorUnitario(10.0).quantidade( 2).build());
        itens.add(Item.builder().idItem("item2").descricao("Descrição do item 2").valorUnitario(20.0).quantidade( 3).build());

        // Criar um pedido com valor total consistente
        Pedido pedidoConsistente = Pedido.builder().idPedido(1).valorTotalItens(80.0).itens(itens).build();

        // Verificar se a exceção não é lançada para o pedido consistente
        assertDoesNotThrow(() -> validaPedidoService.verificaConsistenciaValorTotal(pedidoConsistente));

        // Criar um pedido com valor total inconsistente
        Pedido pedidoInconsistente = Pedido.builder().idPedido(2).valorTotalItens(0.0).itens(itens).build();

        // Verificar se a exceção é lançada para o pedido inconsistente
        PedidoInconsistenteException exception = assertThrows(PedidoInconsistenteException.class, () -> validaPedidoService.verificaConsistenciaValorTotal(pedidoInconsistente));
        assertEquals("Valor total e soma dos itens não condizem", exception.getMessage());
    }
}
