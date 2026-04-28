package com.academia.gym.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensagem) {
        super(mensagem);
    }

    public NotFoundException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
