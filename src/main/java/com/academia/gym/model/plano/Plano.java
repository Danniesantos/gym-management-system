package com.academia.gym.model.plano;

import com.academia.gym.model.matricula.Matricula;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "planos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDePlanoEnum tipo;
    @Column(name = "duracao_dias")
    private Integer duracaoDias;
    @Column(nullable = false,precision = 8, scale = 2)
    private BigDecimal preco;

    @OneToMany(mappedBy = "plano", fetch = FetchType.LAZY)
    private List<Matricula> matriculas = new ArrayList<>();
}
