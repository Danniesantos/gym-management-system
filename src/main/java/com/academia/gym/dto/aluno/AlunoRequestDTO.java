package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Sexo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoRequestDTO {

    @CPF
    @NotBlank(message = "CPF é obrigatorio")
    private String cpf;
    @NotBlank(message = "Nome é obrigatorio")
    @Size(min = 3, max = 100)
    private String nome;
    @NotBlank(message = "Email é obrigatorio")
    @Email(message = "Email invalido")
    private String email;
    @NotBlank(message = "Senha é obrigatorio")
    @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
    private String senha;
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    @NotNull(message = "Sexo é obrigatorio")
    private Sexo sexo;

    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Telefone deve conter 10 ou 11 dígitos"
    )
    private String telefone;

    @Valid
    private EnderecoDTO endereco;
}



