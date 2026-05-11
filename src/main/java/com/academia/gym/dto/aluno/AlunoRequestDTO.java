package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Sexo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoRequestDTO {

    @NotBlank(message = "CPF é obrigatorio", groups = OnCreate.class)
    @CPF(message = "CPF inválido", groups = {OnCreate.class, OnUpdate.class})
    private String cpf;
    @NotBlank(message = "Nome é obrigatorio", groups = OnCreate.class)
    @Size(min = 3, max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String nome;
    @NotBlank(message = "Email é obrigatorio", groups = OnCreate.class)
    @Email(message = "Email invalido", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 5, max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String email;
    @NotBlank(message = "Senha é obrigatorio", groups = OnCreate.class)
    @Size(min = 6, max = 100, message = "Senha deve ter no minimo 6 caracteres",
            groups = {OnCreate.class, OnUpdate.class})
    private String senha;
    @NotNull(message = "Data de nascimento é obrigatória", groups = OnCreate.class)
    @Past(message = "Data de nascimento deve ser no passado", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate dataNascimento;
    private Sexo sexo;

    @NotBlank(groups = OnCreate.class)
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Telefone deve conter 10 ou 11 dígitos",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String telefone;

    @Valid
    private EnderecoDTO endereco;
}



