package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;

public interface FreteService {
    Double calculaValorFreteComPercentual(Pedido pedido, Regiao regiao);
}
