package com.academia.gym.mapper.aluno;

import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.model.aluno.Aluno;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, MapperUtil.class})
public interface AlunoMapper {

    @Named("limparCpf")
    default String limparCpf(String cpf) {
        if (cpf == null || cpf.trim().isBlank()) {
            return null;
        }

        return cpf.replaceAll("\\D", "");
    }

    @Named("limparEmail")
    default String normalizarEmail(String email) {
        if (email == null || email.trim().isBlank()) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    @Named("limparTelefone")
    default String limparTelefone(String telefone) {
        if (telefone == null || telefone.trim().isBlank()) {
            return null;
        }

        return telefone.replaceAll("\\D", "");
    }

    @Mapping(target = "cpf", expression = "java(limparCpf(dto.getCpf()))")
    @Mapping(target = "email", expression = "java(normalizarEmail(dto.getEmail()))")
    @Mapping(target = "telefone", expression = "java(limparTelefone(dto.getTelefone()))")
    Aluno toEntity(AlunoRequestDTO dto);

    AlunoResponseDTO toDTO(Aluno entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(
            target = "cpf",
            expression =
                    "java(dto.getCpf() != null && !dto.getCpf().trim().isBlank() ? limparCpf(dto.getCpf()) : aluno.getCpf())"
    )

    @Mapping(
            target = "nome",
            expression =
                    "java(dto.getNome() != null && !dto.getNome().trim().isBlank() ? dto.getNome().trim() : aluno.getNome())"
    )

    @Mapping(
            target = "email",
            expression =
                    "java(dto.getEmail() != null && !dto.getEmail().trim().isBlank() ? normalizarEmail(dto.getEmail()) : aluno.getEmail())"
    )

    @Mapping(
            target = "telefone",
            expression =
                    "java(dto.getTelefone() != null && !dto.getTelefone().trim().isBlank() ? limparTelefone(dto.getTelefone()) : aluno.getTelefone())"
    )

    @Mapping(target = "endereco", ignore = true)
    void updateAluno(@MappingTarget Aluno aluno, AlunoRequestDTO dto);
}
