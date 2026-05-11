package com.academia.gym.mapper.aluno;

import com.academia.gym.dto.aluno.EnderecoDTO;
import com.academia.gym.model.aluno.Endereco;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = MapperUtil.class)
public interface EnderecoMapper {

    @Named("limparCep")
    default String limparCep(String cep) {
        if (cep == null || cep.trim().isBlank()) {
            return null;
        }

        return cep.replaceAll("\\D", "");
    }

    @Mapping(target = "cep", expression = "java(limparCep(dto.getCep()))")
    Endereco toEntity(EnderecoDTO dto);

    EnderecoDTO toDTO(Endereco entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cep", expression = "java(limparCep(dto.getCep()))")
    void updateEndereco(@MappingTarget Endereco endereco, EnderecoDTO dto);

}
