package com.academia.gym.dto.matricula;

import com.academia.gym.model.matricula.StatusMatricula;
import com.academia.gym.model.plano.TipoDePlanoEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MatriculaResponseDTO(Long id,
                                   Long alunoId,
                                   String nomeAluno,
                                   Long planoId,
                                   TipoDePlanoEnum tipoPlano,
                                   LocalDate dataInicio,
                                   LocalDate dataFim,
                                   StatusMatricula status) {
}
