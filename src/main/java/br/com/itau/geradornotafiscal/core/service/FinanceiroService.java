package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import reactor.core.publisher.Mono;

public interface FinanceiroService {
    Mono<Void> enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal);
}
