package com.academia.gym.service;

import com.academia.gym.dto.aluno.AlunoFiltroDTO;
import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.dto.aluno.EnderecoDTO;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.exception.NotFoundException;
import com.academia.gym.mapper.aluno.AlunoMapper;
import com.academia.gym.mapper.aluno.EnderecoMapper;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.model.aluno.Endereco;
import com.academia.gym.model.aluno.Estado;
import com.academia.gym.model.aluno.Sexo;
import com.academia.gym.repository.AlunoRepository;
import com.academia.gym.service.validator.AlunoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    AlunoRepository repository;
    @Mock
    AlunoMapper mapper;
    @Mock
    EnderecoMapper enderecoMapper;
    @Mock
    AlunoValidator alunoValidator;

    @InjectMocks
    AlunoService alunoService;

    Aluno aluno;
    EnderecoDTO enderecoDTO;
    AlunoResponseDTO responseDto;
    AlunoRequestDTO requestDto;
    Endereco endereco;
    AlunoFiltroDTO filtro;
    Pageable pageable;
    Page<Aluno> paginaAluno;

    @BeforeEach
    void setup() {
        endereco = new Endereco(
                "13500855",
                "Rua: das arvores",
                "7",
                "Jardim Bela vista",
                "São Paulo",
                Estado.SP,
                null);
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setCpf("389.546.930-08");
        aluno.setEmail("teste@gmail.com");
        aluno.setEndereco(endereco);
        enderecoDTO = new EnderecoDTO(
                "13735999",
                "Rua: das flores",
                "3",
                "Jardim Boa vista",
                "São Paulo",
                Estado.SP,
                null);
        responseDto = new AlunoResponseDTO(
                1L,
                "389.546.930-08",
                "teste",
                "teste@gmail.com",
                LocalDate.of(2021, 01, 01),
                Sexo.MASCULINO,
                "19991280033",
                enderecoDTO,
                true,
                LocalDateTime.of(2026, 05, 05, 10, 0),
                LocalDateTime.of(2026, 05, 05, 10, 0),
                null);
        requestDto = new AlunoRequestDTO();
        requestDto.setCpf(aluno.getCpf());
        requestDto.setEmail(aluno.getEmail());
        requestDto.setEndereco(enderecoDTO);
        filtro = new AlunoFiltroDTO(
                "teste",
                "93244698000",
                true);
    }

    @Test
    void deveSalvarComSucesso() {
        given(mapper.toEntity(requestDto)).willReturn(aluno);
        given(repository.save(aluno)).willReturn(aluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        AlunoResponseDTO salvar = alunoService.salvarAluno(requestDto);

        assertThat(salvar).isNotNull();
        assertThat(salvar.email()).isEqualTo(requestDto.getEmail());
        assertThat(salvar.cpf()).isEqualTo(requestDto.getCpf());
        verify(mapper).toEntity(requestDto);
        verify(mapper).toDTO(aluno);
        verify(repository).save(aluno);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarErroQuandoCpfEmailDuplicado() {
        willThrow(new BusinessException("Dados inválidos"))
                .given(alunoValidator)
                .validarCadastro(any(), any());

        assertThatThrownBy(() ->
                alunoService.salvarAluno(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Dados inválidos");

        verifyNoMoreInteractions(alunoValidator);
        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveBuscarAlunosComSucesso() {
        pageable = PageRequest.of(0, 10);
        paginaAluno = new PageImpl<>(List.of(aluno));
        given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(paginaAluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        Page<AlunoResponseDTO> resultado = alunoService.buscar(filtro, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);

        assertThat(resultado.getContent().get(0).id()).isEqualTo(aluno.getId());

        verify(repository).findAll(any(Specification.class), any(Pageable.class));

        verify(mapper).toDTO(aluno);
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoEncontrarAlunos() {
        pageable = PageRequest.of(0, 10);
        paginaAluno = new PageImpl<>(Collections.emptyList());
        given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(paginaAluno);

        Page<AlunoResponseDTO> resultado = alunoService.buscar(filtro, pageable);

        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getContent()).hasSize(0);

        verify(repository).findAll(any(Specification.class), any(Pageable.class));

        verifyNoInteractions(mapper);
    }

    @Test
    void deveLimitarPageSizePara50() {
        pageable = PageRequest.of(0, 200);
        paginaAluno = new PageImpl<>(List.of(aluno));
        given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(paginaAluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        alunoService.buscar(filtro, pageable);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(repository).findAll(any(Specification.class), pageableCaptor.capture());
        Pageable pageableCapturado = pageableCaptor.getValue();
        assertThat(pageableCapturado.getPageSize()).isEqualTo(50);
        verify(mapper).toDTO(aluno);
    }

    @Test
    void deveBuscarAlunoPorId() {
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        AlunoResponseDTO find = alunoService.buscarPorId(aluno.getId());

        assertThat(find.id()).isEqualTo(aluno.getId());
        assertThat(find).isNotNull();
        verify(repository).findById(aluno.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarNotFoundQuandoAlunoNaoExistir() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> alunoService.buscarPorId(aluno.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aluno não encontrado");

        verify(repository).findById(aluno.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveAtualizarAlunoComSucesso() {
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));
        given(repository.save(aluno)).willReturn(aluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        AlunoResponseDTO update = alunoService.updateAluno(aluno.getId(), requestDto);

        assertThat(update).isNotNull();
        verify(mapper).updateAluno(aluno, requestDto);
        verify(mapper).toDTO(aluno);
        verify(repository).findById(aluno.getId());
        verify(repository).save(aluno);
    }

    @Test
    void deveLancarErroQuandoAtualizarCpfEmailDuplicado() {
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));
        willThrow(new BusinessException("Dados inválidos"))
                .given(alunoValidator)
                .validarUpdate(aluno, aluno.getEmail(), aluno.getCpf());

        assertThatThrownBy(() -> alunoService.updateAluno(aluno.getId(), requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Dados inválidos");

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(alunoValidator);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarErroQuandoAtualizarAlunoDesativado() {
        aluno.setAtivo(false);
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));

        willThrow(new ConflictException(
                "Aluno inativo não pode ser atualizado"))
                .given(alunoValidator)
                .validarUpdate(aluno, aluno.getEmail(), aluno.getCpf());

        assertThatThrownBy(() -> alunoService.updateAluno(aluno.getId(), requestDto))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void deveLancarErroQuandoAtualizarAlunoInexistente() {
        given(repository.findById(aluno.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> alunoService.updateAluno(aluno.getId(), requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aluno não encontrado");

        verify(repository).findById(aluno.getId());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(alunoValidator);
        verifyNoInteractions(mapper);
        verifyNoInteractions(enderecoMapper);
    }

    @Test
    void deveDesativarAluno() {
        aluno.setAtivo(true);
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));

        alunoService.desativarAluno(aluno.getId());

        verify(repository).save(aluno);
        assertThat(aluno.getAtivo()).isFalse();
        assertThat(aluno.getDataDesativacao()).isNotNull();
    }

    @Test
    void deveLancarNotFoundAoDesativarAluno() {
        given(repository.findById(aluno.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> alunoService.desativarAluno(aluno.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aluno não encontrado");
        verify(repository).findById(aluno.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveAtualizarEnderecoQuandoExistir() {
        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));
        given(repository.save(aluno)).willReturn(aluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        AlunoResponseDTO update = alunoService.updateAluno(aluno.getId(), requestDto);

        assertThat(update).isNotNull();
        verify(repository).findById(aluno.getId());
        verify(mapper).updateAluno(aluno, requestDto);
        verify(enderecoMapper).updateEndereco(aluno.getEndereco(), requestDto.getEndereco());
        verify(repository).save(aluno);
        verify(mapper).toDTO(aluno);
    }

    @Test
    void deveCriarEnderecoQuandoAlunoNaoPossuirEndereco() {
        aluno.setEndereco(null);
        requestDto.setEndereco(enderecoDTO);

        given(repository.findById(aluno.getId())).willReturn(Optional.of(aluno));
        given(repository.save(aluno)).willReturn(aluno);
        given(mapper.toDTO(aluno)).willReturn(responseDto);

        AlunoResponseDTO update = alunoService.updateAluno(aluno.getId(), requestDto);

        assertThat(update).isNotNull();
        assertThat(aluno.getEndereco()).isNotNull();
        verify(enderecoMapper).updateEndereco(aluno.getEndereco(), requestDto.getEndereco());
        verify(repository).save(aluno);
    }
}
