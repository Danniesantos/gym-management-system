package com.academia.gym.dto.matricula;

import com.academia.gym.model.matricula.StatusMatricula;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MatriculaResponseDTO(Long id,
                                   Long alunoId,
                                   String nomeAluno,
                                   Long planoId,
                                   String nomePlano,
                                   LocalDate dataInicio,
                                   LocalDate dataFim,
                                   StatusMatricula status) {
}
