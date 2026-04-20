package com.academia.gym.model.aluno;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {

    @Column(length = 8, nullable = false)
    private String cep;
    @Column(length = 100,nullable = false)
    private String rua;
    @Column(length = 5)
    private String numero;
    @Column(length = 50,nullable = false)
    private String bairro;
    @Column(length = 50, nullable = false)
    private String cidade;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Column(length = 50)
    private String complemento;
}
