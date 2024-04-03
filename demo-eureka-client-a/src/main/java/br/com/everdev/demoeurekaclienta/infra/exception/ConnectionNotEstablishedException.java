package br.com.everdev.demoeurekaclienta.infra.exception;

public class ConnectionNotEstablishedException extends Exception{

    private final Integer responseCode;

    public ConnectionNotEstablishedException(String message) {
        super(message);
        this.responseCode = -1;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

}
