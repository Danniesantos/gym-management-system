package com.academia.gym.exception;

import com.academia.gym.dto.ErroCampo;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessException extends RuntimeException {

    private final List<ErroCampo> erros;

    public BusinessException(String message) {
        super(message);
        this.erros = List.of();
    }

    public BusinessException(String message, List<ErroCampo> erros) {
        super(message);
        this.erros = erros;
    }
}

