package com.academia.gym.repository;

import com.academia.gym.dto.aluno.AlunoFiltroDTO;
import com.academia.gym.model.aluno.Aluno;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlunoSpecification {

    public static Specification<Aluno> filtro(AlunoFiltroDTO filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + filtro.nome().toLowerCase() + "%"
                        )
                );
            }

            if (filtro.cpf() != null && !filtro.cpf().isBlank()) {
                predicates.add(
                        cb.equal(root.get("cpf"), filtro.cpf())
                );
            }

            if (filtro.ativo() != null) {
                predicates.add(
                        cb.equal(root.get("ativo"), filtro.ativo())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
