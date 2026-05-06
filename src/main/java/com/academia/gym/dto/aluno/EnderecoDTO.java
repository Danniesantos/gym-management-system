package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

    @NotBlank(groups = OnCreate.class, message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{8}$", groups = {OnCreate.class, OnUpdate.class}, message = "CEP deve conter 8 dígitos")
    private String cep;
    @Size(min = 3, max = 100, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = OnCreate.class, message = "Rua é obrigatória")
    private String rua;
    @Size(max = 5, groups = {OnCreate.class, OnUpdate.class})
    private String numero;
    @Size(min = 3,max = 50, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = OnCreate.class, message = "Bairro é obrigatorio")
    private String bairro;
    @Size(min = 3,max = 50, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = OnCreate.class, message = "Cidade é obrigatória")
    private String cidade;
    @Size(min = 2)
    @NotNull(groups = OnCreate.class, message = "Estado é obrigatório")
    private Estado estado;
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String complemento;
}
