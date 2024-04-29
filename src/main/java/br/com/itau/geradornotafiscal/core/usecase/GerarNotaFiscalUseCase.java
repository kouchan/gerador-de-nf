package br.com.itau.geradornotafiscal.core.usecase;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.model.Pedido;

public interface GerarNotaFiscalUseCase {

	public NotaFiscal gerarNotaFiscal(Pedido pedido);
	
}
