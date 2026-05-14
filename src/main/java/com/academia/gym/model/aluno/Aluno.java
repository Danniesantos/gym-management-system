package com.academia.gym.model.aluno;

import com.academia.gym.model.matricula.Matricula;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @Column(nullable = false)
    private LocalDate dataNascimento;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Column(length = 11)
    private String telefone;
    @Embedded
    private Endereco endereco;
    @Column(nullable = false)
    private Boolean ativo = true;
    @CreatedDate
    private LocalDateTime dataCadastro;
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataDesativacao;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Matricula> matriculas = new ArrayList<>();

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
        this.dataDesativacao = LocalDateTime.now();
    }
}
