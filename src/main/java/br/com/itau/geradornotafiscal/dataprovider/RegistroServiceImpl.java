package br.com.itau.geradornotafiscal.dataprovider;

import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.service.RegistroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistroServiceImpl implements RegistroService {
    @Override
    public Mono<Void> registrarNotaFiscal(NotaFiscal notaFiscal) {
        return Mono.fromRunnable(() -> {
            try {
                //Simula o registro da nota fiscal
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        })
                .doOnError( throwable -> log.error("Erro ao enviar para o serviço de registro de nota fiscal"))
                .doOnSuccess( success -> log.info("Sucesso ao enviar para o serviço de registro de nota fiscal"))
                .then();
    }
}
