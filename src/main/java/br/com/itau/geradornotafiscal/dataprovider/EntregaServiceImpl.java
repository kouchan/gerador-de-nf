package br.com.itau.geradornotafiscal.dataprovider;

import br.com.itau.geradornotafiscal.core.exception.AgendamentoEntregaException;
import br.com.itau.geradornotafiscal.core.model.NotaFiscal;
import br.com.itau.geradornotafiscal.core.service.EntregaService;
import br.com.itau.geradornotafiscal.dataprovider.out.EntregaIntegrationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class EntregaServiceImpl implements EntregaService {
    private final EntregaIntegrationPort entregaIntegrationPort;
    @Override
    public Mono<Void> agendarEntrega(NotaFiscal notaFiscal) {
        return Mono.fromRunnable(() -> {
            try {
                //Simula o agendamento da entrega
                Thread.sleep(150);
                entregaIntegrationPort.criarAgendamentoEntrega(notaFiscal);
            } catch (InterruptedException e) {
                throw new AgendamentoEntregaException(notaFiscal);
            }
        })
                .doOnError( throwable -> log.error("Erro ao enviar para o serviço de agendamento de entrega"))
                .doOnSuccess( success -> log.info("Sucesso ao enviar para o serviço de agendamento de  entrega"))
                .then();
    }
}
