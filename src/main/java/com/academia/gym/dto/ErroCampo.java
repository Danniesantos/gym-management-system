package com.academia.gym.dto;

public record ErroCampo(String field,
                        String message,
                        String code) {

    public static ErroCampo erro(String field, String message, String code) {
        return new ErroCampo(field, message, code);
    }
}
