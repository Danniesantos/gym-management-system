package com.academia.gym.dto.plano;

import com.academia.gym.model.plano.TipoDePlanoEnum;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PlanoResponseDTO(Long id,
                               TipoDePlanoEnum tipoPlano,
                               Integer duracaoDias,
                               BigDecimal preco) {


}
