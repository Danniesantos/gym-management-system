package com.academia.gym.mapper.aluno;

import com.academia.gym.dto.aluno.EnderecoDTO;
import com.academia.gym.model.aluno.Endereco;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    Endereco toEntity(EnderecoDTO dto);

    EnderecoDTO toDTO(Endereco entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEndereco(@MappingTarget Endereco endereco, EnderecoDTO enderecoDTO);
}
