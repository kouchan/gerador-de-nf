package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.Item;
import br.com.itau.geradornotafiscal.core.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.core.service.CalculadoraAliquotaService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalculadoraAliquotaServiceImpl implements CalculadoraAliquotaService {

    @Override
    public List<ItemNotaFiscal> calcularAliquota(List<Item> items, double aliquotaPercentual) {
        final List<ItemNotaFiscal> itemNotaFiscalList = new ArrayList<>();

        items.forEach(item -> {
            double valorTributo = item.getValorUnitario() * aliquotaPercentual;
            ItemNotaFiscal itemNotaFiscal = ItemNotaFiscal.builder()
                    .idItem(item.getIdItem())
                    .descricao(item.getDescricao())
                    .valorUnitario(item.getValorUnitario())
                    .quantidade(item.getQuantidade())
                    .valorTributoItem(valorTributo)
                    .build();
            itemNotaFiscalList.add(itemNotaFiscal);
        });
        return itemNotaFiscalList;
    }
}



