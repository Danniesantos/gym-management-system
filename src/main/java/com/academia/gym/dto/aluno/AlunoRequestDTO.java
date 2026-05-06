package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Sexo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoRequestDTO {

    @NotBlank(groups = OnCreate.class, message = "CPF é obrigatorio")
    @CPF(groups = {OnCreate.class, OnUpdate.class}, message = "CPF inválido")
    private String cpf;
    @NotBlank(groups = OnCreate.class, message = "Nome é obrigatorio")
    @Size(min = 3, max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String nome;
    @NotBlank(groups = OnCreate.class, message = "Email é obrigatorio")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Email invalido")
    private String email;
    @NotBlank(groups = OnCreate.class, message = "Senha é obrigatorio")
    @Size(min = 6, max = 100, groups = {OnCreate.class, OnUpdate.class}, message = "Senha deve ter no minimo 6 caracteres")
    private String senha;
    @Past(groups = {OnCreate.class, OnUpdate.class}, message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    @NotNull(groups = OnCreate.class, message = "Sexo é obrigatorio")
    private Sexo sexo;

    @Pattern(
            regexp = "^\\d{10,11}$",
            groups = {OnCreate.class, OnUpdate.class},
            message = "Telefone deve conter 10 ou 11 dígitos"
    )
    private String telefone;

    @Valid
    private EnderecoDTO endereco;
}



