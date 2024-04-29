package br.com.itau.geradornotafiscal.entrypoint.controller.config;

import br.com.itau.geradornotafiscal.core.exception.IntegrationException;
import br.com.itau.geradornotafiscal.core.exception.PedidoInconsistenteException;
import br.com.itau.geradornotafiscal.core.exception.RegimeTributacaoException;
import br.com.itau.geradornotafiscal.core.model.ErroPedidoInconsistente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(PedidoInconsistenteException.class)
    public ResponseEntity<ErroPedidoInconsistente> handlePedidoInconsistenteErro(PedidoInconsistenteException erro){
        ErroPedidoInconsistente erroPedidoInconsistente = new ErroPedidoInconsistente(erro.getMessage(),erro.getIdPedido(), erro.getValorTotalPedido(), erro.getSomaValorTotalItens());
        return new ResponseEntity<>(erroPedidoInconsistente, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<String> handleIntegrationException(IntegrationException erro){
        IntegrationException integrationException = new IntegrationException(erro.getMessage());
        return new ResponseEntity<>(integrationException.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(RegimeTributacaoException.class)
    public ResponseEntity<String> handlePedidoInconsistenteErro(RegimeTributacaoException erro){
        RegimeTributacaoException erroPedidoInconsistente = new RegimeTributacaoException(erro.getRegimeTributacaoPJ());
        return new ResponseEntity<>(erroPedidoInconsistente.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
