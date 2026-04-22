package com.academia.gym.repository;

import com.academia.gym.model.matricula.Matricula;
import com.academia.gym.model.matricula.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByAlunoId(Long alunoId);

    boolean existsByAlunoIdAndStatus(Long alunoId, StatusMatricula status);

    Optional<Matricula> findByAlunoIdAndStatus(Long alunoId, StatusMatricula status);
}
