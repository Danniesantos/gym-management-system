package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Sexo;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
public record AlunoResponseDTO(Long id,
                               String cpf,
                               String nome,
                               String email,
                               LocalDate dataNascimento,
                               Sexo sexo,
                               String telefone,
                               EnderecoDTO endereco,
                               Boolean ativo,
                               LocalDateTime dataCadastro) {
}
