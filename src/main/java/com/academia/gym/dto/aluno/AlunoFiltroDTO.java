package com.academia.gym.dto.aluno;

public record AlunoFiltroDTO(Long id,
                             String nome,
                             String cpf,
                             Boolean ativo) {
}
