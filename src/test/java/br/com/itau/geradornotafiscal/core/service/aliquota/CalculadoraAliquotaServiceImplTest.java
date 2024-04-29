package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.Item;
import br.com.itau.geradornotafiscal.core.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.usecase.impl.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CalculadoraAliquotaServiceImplTest {
    @InjectMocks
    private CalculadoraAliquotaServiceImpl calculadoraAliquotaServiceImpl;

    @Mock
    private Item item1;

    @Mock
    private Item item2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        calculadoraAliquotaServiceImpl = new CalculadoraAliquotaServiceImpl();
    }

    @Test
    @DisplayName("Dado Items validos" +
            "Quando calcular aliquota" +
            "Entao Notafiscal deve os valores de ValorTributoItem calculado")
    public void testCalcularAliquota() {
        // Mocking items
        when(item1.getIdItem()).thenReturn("1");
        when(item1.getDescricao()).thenReturn("Item 1");
        when(item1.getValorUnitario()).thenReturn(10.0);
        when(item1.getQuantidade()).thenReturn(2);

        when(item2.getIdItem()).thenReturn("2");
        when(item2.getDescricao()).thenReturn("Item 2");
        when(item2.getValorUnitario()).thenReturn(15.0);
        when(item2.getQuantidade()).thenReturn(3);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        double aliquotaPercentual = 0.1; // 10%

        // Test method
        List<ItemNotaFiscal> result = calculadoraAliquotaServiceImpl.calcularAliquota(items, aliquotaPercentual);

        // Assertions
        assertEquals(2, result.size()); // Check if two items are returned
        assertEquals("1", result.get(0).getIdItem()); // Check if item ID is correct
        assertEquals("Item 1", result.get(0).getDescricao()); // Check if item description is correct
        assertEquals(1.0, result.get(0).getValorTributoItem()); // Check if calculated tax value is correct for item 1
        assertEquals("2", result.get(1).getIdItem()); // Check if item ID is correct
        assertEquals("Item 2", result.get(1).getDescricao()); // Check if item description is correct
        assertEquals(1.5, result.get(1).getValorTributoItem()); // Check if calculated tax value is correct for item 2
    }
}