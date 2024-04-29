package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.Item;
import br.com.itau.geradornotafiscal.core.model.ItemNotaFiscal;

import java.util.List;

public interface CalculadoraAliquotaService {
    List<ItemNotaFiscal> calcularAliquota(List<Item> items, double aliquotaPercentual);
}
