package br.com.everdev.demoeurekaclienta.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ConnectionNotEstablishedException.class)
    public ResponseEntity connectionNotEstablishedException() {
        return ResponseEntity.badRequest().body(-1);
    }

}
