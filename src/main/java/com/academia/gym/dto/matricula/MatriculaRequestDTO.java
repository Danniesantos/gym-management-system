package com.academia.gym.dto.matricula;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaRequestDTO {

    @NotNull(message = "Aluno é Obrigatório")
    private Long alunoId;
    @NotNull(message = "Plano é Obrigatório")
    private Long planoId;

}
