package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.Pedido;

public interface FreteService {
    Double calculaValorFreteComPercentual(Pedido pedido);
}
