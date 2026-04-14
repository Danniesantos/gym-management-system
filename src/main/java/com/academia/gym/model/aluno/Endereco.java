package com.academia.gym.model.aluno;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {

    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private String complemento;
}
