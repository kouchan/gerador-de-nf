package br.com.itau.calculadoratributos;

import br.com.itau.geradornotafiscal.service.CalculadoraAliquotaProduto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CalculadoratributosApplicationTests {

	@InjectMocks
	private CalculadoraAliquotaProduto calculadoraAliquotaProduto;

	@Mock
	private Item item1;

	@Mock
	private Item item2;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		calculadoraAliquotaProduto = new CalculadoraAliquotaProduto();
	}

	@Test
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
		List<ItemNotaFiscal> result = calculadoraAliquotaProduto.calcularAliquota(items, aliquotaPercentual);

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

