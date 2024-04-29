package br.com.itau.geradornotafiscal.entrypoint.controller;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.model.Pedido;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/pedido")
public interface GeradorNFController {

    @PostMapping("/gerarNotaFiscal")
    NotaFiscal gerarNotaFiscal(@RequestBody final Pedido pedido);
}
