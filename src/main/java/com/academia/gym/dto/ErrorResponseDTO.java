package com.academia.gym.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(int status,
                               String message,
                               String path,
                               LocalDateTime timestamp,
                               List<ErroCampo> errors) {

    public static ErrorResponseDTO of(
            int status,
            String message,
            String path,
            List<ErroCampo> errors
    ) {
        return new ErrorResponseDTO(
                status,
                message,
                path,
                LocalDateTime.now(),
                errors == null ? List.of() : errors
        );
    }

    public static ErrorResponseDTO of(
            int status,
            String message,
            String path
    ) {
        return of(status, message, path, List.of());
    }
}

