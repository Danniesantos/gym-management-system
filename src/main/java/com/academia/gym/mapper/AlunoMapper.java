package com.academia.gym.mapper;

import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.model.aluno.Aluno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlunoMapper {

    private final EnderecoMapper enderecoMapper;

    public Aluno toEntity(AlunoRequestDTO dto) {
        if (dto == null) return null;

        return Aluno.builder()
                .cpf(dto.getCpf())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .dataNascimento(dto.getDataNascimento())
                .sexo(dto.getSexo())
                .telefone(dto.getTelefone())
                .endereco(enderecoMapper.toEntity(dto.getEndereco()))
                .build();

    }

    public AlunoResponseDTO toDTO(Aluno aluno) {
        if (aluno == null) return null;

        return AlunoResponseDTO.builder()
                .id(aluno.getId())
                .cpf(aluno.getCpf())
                .nome(aluno.getNome())
                .email(aluno.getEmail())
                .dataNascimento(aluno.getDataNascimento())
                .sexo(aluno.getSexo())
                .telefone(aluno.getTelefone())
                .endereco(enderecoMapper.toDTO(aluno.getEndereco()))
                .ativo(aluno.getAtivo())
                .dataCadastro(aluno.getDataCadastro())
                .build();

    }

    public Aluno updateToEntity(Aluno alunoEntity, AlunoRequestDTO requestDTO) {
        return Aluno.builder()
                .id(alunoEntity.getId())
                .cpf(requestDTO.getCpf() != null ? requestDTO.getCpf() : alunoEntity.getCpf())
                .nome(requestDTO.getNome() != null ? requestDTO.getNome() : alunoEntity.getNome())
                .email(requestDTO.getEmail() != null ? requestDTO.getEmail() : alunoEntity.getEmail())
                .senha(requestDTO.getSenha() != null ? requestDTO.getSenha() : alunoEntity.getSenha())
                .dataNascimento(requestDTO.getDataNascimento() != null ? requestDTO.getDataNascimento() : alunoEntity.getDataNascimento())
                .sexo(requestDTO.getSexo() != null ? requestDTO.getSexo() : alunoEntity.getSexo())
                .telefone(requestDTO.getTelefone() != null ? requestDTO.getTelefone() : alunoEntity.getTelefone())
                .endereco(requestDTO.getEndereco() != null ? enderecoMapper.toEntity(requestDTO.getEndereco()) : alunoEntity.getEndereco())
                .ativo(alunoEntity.getAtivo())
                .dataCadastro(alunoEntity.getDataCadastro())
                .build();
    }
}
