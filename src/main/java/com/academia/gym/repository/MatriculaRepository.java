package com.academia.gym.repository;

import com.academia.gym.model.matricula.Matricula;
import com.academia.gym.model.matricula.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByAlunoIdAndStatus(Long alunoId, StatusMatricula status);
}
