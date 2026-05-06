package com.academia.gym.mapper.aluno;

import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.model.aluno.Aluno;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = EnderecoMapper.class)
public interface AlunoMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataDesativacao", ignore = true)
    Aluno toEntity(AlunoRequestDTO dto);

    AlunoResponseDTO toDTO(Aluno entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "endereco", ignore = true)
    void updateAluno(@MappingTarget Aluno aluno, AlunoRequestDTO dto);


}
