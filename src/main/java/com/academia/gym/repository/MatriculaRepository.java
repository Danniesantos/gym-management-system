package com.academia.gym.repository;

import com.academia.gym.model.matricula.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}
