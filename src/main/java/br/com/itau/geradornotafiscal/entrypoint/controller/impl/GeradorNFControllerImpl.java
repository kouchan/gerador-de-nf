package br.com.itau.geradornotafiscal.entrypoint.controller.impl;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.entrypoint.controller.GeradorNFController;
import lombok.RequiredArgsConstructor;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.usecase.GerarNotaFiscalUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GeradorNFControllerImpl implements GeradorNFController {

	private final GerarNotaFiscalUseCase notaFiscalService;

	@Override
	public NotaFiscal gerarNotaFiscal(@RequestBody final Pedido pedido) {
		// Lógica de processamento do pedido
		// Aqui você pode realizar as operações desejadas com o objeto Pedido

		// Exemplo de retorno
		log.info("Nota fiscal gerada com sucesso para o pedido: {}", pedido.getIdPedido());
		NotaFiscal notaFiscal = notaFiscalService.gerarNotaFiscal(pedido);
		return notaFiscal;
	}
	
}
