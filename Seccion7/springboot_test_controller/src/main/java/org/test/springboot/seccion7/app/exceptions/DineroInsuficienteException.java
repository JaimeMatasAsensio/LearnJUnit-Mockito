package org.test.springboot.seccion7.app.exceptions;

public class DineroInsuficienteException extends RuntimeException{

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
