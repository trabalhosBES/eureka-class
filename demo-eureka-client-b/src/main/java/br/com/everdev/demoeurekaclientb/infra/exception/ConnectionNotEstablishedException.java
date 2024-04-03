package br.com.everdev.demoeurekaclientb.infra.exception;

public class ConnectionNotEstablishedException extends Exception{

    private final Integer responseCode;

    public ConnectionNotEstablishedException(String message) {
        super(message);
        this.responseCode = -2;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

}
