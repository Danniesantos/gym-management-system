package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Endereco;
import com.academia.gym.model.aluno.Sexo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlunoResponseDTO(Long id,
                               String cpf,
                               String nome,
                               String email,
                               LocalDate dataNascimento,
                               Sexo sexo,
                               String telefone,
                               Endereco endereco,
                               Boolean ativo,
                               LocalDateTime dataCadastro) {
}
