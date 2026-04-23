package com.academia.gym.model.matricula;

import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.model.plano.Plano;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatricula status;
}
