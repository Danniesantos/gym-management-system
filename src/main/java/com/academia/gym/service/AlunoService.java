package com.academia.gym.service;

import com.academia.gym.dto.aluno.AlunoFiltroDTO;
import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.exception.NotFoundException;
import com.academia.gym.mapper.aluno.AlunoMapper;
import com.academia.gym.mapper.aluno.EnderecoMapper;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.model.aluno.Endereco;
import com.academia.gym.repository.AlunoRepository;
import com.academia.gym.repository.AlunoSpecification;
import com.academia.gym.service.validator.AlunoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlunoService {

    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final EnderecoMapper enderecoMapper;
    private final AlunoValidator alunoValidator;

    private static final String ERRO_NOTFOUND = "Aluno não encontrado";


    public AlunoResponseDTO salvarAluno(AlunoRequestDTO dto) {
        alunoValidator.validarCadastro(dto.getEmail(), dto.getCpf());
        Aluno aluno = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(aluno));
    }


    public Page<AlunoResponseDTO> buscar(AlunoFiltroDTO filtro, Pageable pageable) {
        Pageable pageableSeguro = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 50)
        );
        return repository.findAll(
                        AlunoSpecification.filtro(filtro),
                        pageableSeguro)
                .map(mapper::toDTO);
    }

    public void desativarAluno(Long id) {
        Aluno aluno = buscarAluno(id);
        aluno.desativar();
        repository.save(aluno);
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = buscarAluno(id);
        return mapper.toDTO(aluno);
    }

    public AlunoResponseDTO updateAluno(Long id, AlunoRequestDTO dto) {

        Aluno aluno = buscarAluno(id);

        alunoValidator.validarUpdate(aluno, dto.getEmail(), dto.getCpf());

        mapper.updateAluno(aluno, dto);

        if (dto.getEndereco() != null) {

            if (aluno.getEndereco() == null) {
                aluno.setEndereco(new Endereco());
            }

            enderecoMapper.updateEndereco(aluno.getEndereco(), dto.getEndereco());
        }

        return mapper.toDTO(aluno);
    }

    private Aluno buscarAluno(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ERRO_NOTFOUND));
    }
}


