package com.academia.gym.model.aluno;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;//futuramente hash
    private LocalDate dataNascimento;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Column(length = 11)
    private String telefone;
    @Embedded
    private Endereco endereco;
    private Boolean ativo;
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
}
