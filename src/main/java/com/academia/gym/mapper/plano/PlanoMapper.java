package com.academia.gym.mapper.plano;

import com.academia.gym.dto.plano.PlanoRequestDTO;
import com.academia.gym.dto.plano.PlanoResponseDTO;
import com.academia.gym.model.plano.Plano;
import org.springframework.stereotype.Component;

@Component
public class PlanoMapper {

    public Plano toEntity(PlanoRequestDTO dto) {
        if (dto == null) return null;

        return Plano.builder()
                .tipoPlano(dto.getTipoPlano())
                .duracaoDias(dto.getDuracaoDias())
                .preco(dto.getPreco())
                .build();
    }

    public PlanoResponseDTO toDTO(Plano entity) {
        if (entity == null) return null;

        return PlanoResponseDTO.builder()
                .id(entity.getId())
                .tipoPlano(entity.getTipoPlano())
                .duracaoDias(entity.getDuracaoDias())
                .preco(entity.getPreco())
                .build();
    }
}
