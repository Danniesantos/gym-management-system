package com.academia.gym.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(int status,
                               String message,
                               String path,
                               LocalDateTime timestamp,
                               List<ErroCampo> erros) {

    public static ErrorResponseDTO badRequest(String mensagem, String path, List<ErroCampo> erros) {
        return new ErrorResponseDTO(
                400,
                mensagem,
                path,
                LocalDateTime.now(),
                erros
        );
    }

    public static ErrorResponseDTO badRequest(String mensagem, String path) {
        return new ErrorResponseDTO(
                400,
                mensagem,
                path,
                LocalDateTime.now(),
                List.of(new ErroCampo("erro", mensagem))
        );
    }

    public static ErrorResponseDTO conflict(String mensagem, String path) {
        return new ErrorResponseDTO(
                409,
                mensagem,
                path,
                LocalDateTime.now(),
                List.of(new ErroCampo("erro", mensagem))
        );

    }

    public static ErrorResponseDTO notFound(String mensagem, String path) {
        return new ErrorResponseDTO(
                404,
                mensagem,
                path,
                LocalDateTime.now(),
                List.of(new ErroCampo("erro", mensagem))
        );
    }

    public static ErrorResponseDTO unprocessableEntity(String mensagem, String path) {
        return new ErrorResponseDTO(
                422,
                mensagem,
                path,
                LocalDateTime.now(),
                List.of(new ErroCampo("erro", mensagem))
        );
    }


}

