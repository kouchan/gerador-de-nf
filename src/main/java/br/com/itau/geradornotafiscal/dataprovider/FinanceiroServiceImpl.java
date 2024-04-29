package br.com.itau.geradornotafiscal.dataprovider;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.service.FinanceiroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class FinanceiroServiceImpl implements FinanceiroService {
    @Override
    public Mono<Void> enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal) {
        return Mono.fromRunnable(() -> {
            try {
                //Simula o envio da nota fiscal para o contas a receber
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        })
                .doOnError( throwable -> log.error("Erro ao enviar para o serviço de contas a receber"))
                .doOnSuccess( success -> log.info("Sucesso ao enviar para o serviço de contas a receber"))
                .then();
    }
}
