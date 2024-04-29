package br.com.itau.geradornotafiscal.dataprovider;

import br.com.itau.geradornotafiscal.core.exception.BaixaEstoqueException;
import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class EstoqueServiceImpl implements EstoqueService {
    @Override
    public Mono<Void> enviarNotaFiscalParaBaixaEstoque(NotaFiscal notaFiscal) {
        return Mono.fromRunnable(() -> {
            try {
                //Simula envio de nota fiscal para baixa de estoque
                Thread.sleep(380);
            } catch (InterruptedException e) {
                throw new BaixaEstoqueException(notaFiscal);
            }
        })
                .doOnError( throwable -> log.error("Erro ao enviar para o serviço de baixa no estoque"))
                .doOnSuccess( success -> log.info("Sucesso ao enviar para o serviço de baixa no estoque"))
                .then();
    }
}
