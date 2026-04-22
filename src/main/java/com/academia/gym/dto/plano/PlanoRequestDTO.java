package com.academia.gym.dto.plano;

import com.academia.gym.model.plano.TipoDePlanoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanoRequestDTO {

    @NotNull(message = "Tipo do plano é obrigatório")
    private TipoDePlanoEnum tipo;
    private Integer duracaoDias;
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
