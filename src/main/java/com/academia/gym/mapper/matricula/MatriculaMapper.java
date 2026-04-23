package com.academia.gym.mapper.matricula;

import com.academia.gym.dto.matricula.MatriculaRequestDTO;
import com.academia.gym.dto.matricula.MatriculaResponseDTO;
import com.academia.gym.model.matricula.Matricula;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {

    public Matricula toEntity(MatriculaRequestDTO dto) {

        if (dto == null) return null;

        return Matricula.builder().build();
    }

    public MatriculaResponseDTO toDTO(Matricula matricula) {
        if (matricula == null) return null;

        return MatriculaResponseDTO.builder()
                .id(matricula.getId())
                .alunoId(matricula.getAluno().getId())
                .nomeAluno(matricula.getAluno().getNome())
                .planoId(matricula.getPlano().getId())
                .tipoPlano(matricula.getPlano().getTipoPlano())
                .dataInicio(matricula.getDataInicio())
                .dataFim(matricula.getDataFim())
                .status(matricula.getStatus())
                .build();
    }
}
