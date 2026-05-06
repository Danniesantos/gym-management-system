package com.academia.gym.dto;

public record ErroCampo(String campo,
                        String message, String code) {
}
