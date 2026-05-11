package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

    @NotBlank(message = "CEP é obrigatório", groups = OnCreate.class)
    @Pattern(regexp = "^\\d{8}$", message = "CEP deve conter 8 dígitos",
            groups = {OnCreate.class, OnUpdate.class})
    private String cep;
    @Size(min = 3, max = 100, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Rua é obrigatória", groups = OnCreate.class)
    private String rua;
    @Size(max = 5, groups = {OnCreate.class, OnUpdate.class})
    private String numero;
    @Size(min = 3,max = 50, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Bairro é obrigatorio", groups = OnCreate.class)
    private String bairro;
    @Size(min = 3,max = 50, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Cidade é obrigatória", groups = OnCreate.class)
    private String cidade;
    @NotNull(message = "Estado é obrigatório", groups = OnCreate.class)
    private Estado estado;
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String complemento;
}
