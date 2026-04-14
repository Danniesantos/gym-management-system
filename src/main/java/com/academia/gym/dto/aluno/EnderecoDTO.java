package com.academia.gym.dto.aluno;

import com.academia.gym.model.aluno.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoDTO {

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{8}$", message = "CEP deve conter 8 dígitos")
    private String cep;
    @Size(min = 1, max = 100)
    @NotBlank(message = "Rua é obrigatória")
    private String rua;
    @Size(max = 5)
    private String numero;
    @Size(max = 50)
    @NotBlank(message = "Bairro é obrigatorio")
    private String bairro;
    @Size(max = 50)
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    @NotNull(message = "Estado é obrigatório")
    private Estado estado;
    @Size(max = 50)
    private String complemento;
}
