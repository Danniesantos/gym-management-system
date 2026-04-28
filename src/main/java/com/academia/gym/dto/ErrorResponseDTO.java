package com.academia.gym.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(int status,
                               String message,
                               String path,
                               LocalDateTime timestamp) {
}
