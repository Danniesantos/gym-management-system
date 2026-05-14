package com.academia.gym.controller;

import com.academia.gym.dto.aluno.AlunoFiltroDTO;
import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.dto.aluno.EnderecoDTO;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.NotFoundException;
import com.academia.gym.model.aluno.Estado;
import com.academia.gym.model.aluno.Sexo;
import com.academia.gym.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlunoController.class)
class AlunoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AlunoService alunoService;

    AlunoRequestDTO requestDto;
    AlunoResponseDTO responseDto;
    EnderecoDTO enderecoDTO;
    String url;
    String URL_ID;
    AlunoFiltroDTO filtro;
    Pageable pageable;
    Page<AlunoResponseDTO> paginaAluno;

    @BeforeEach
    void setup() {
        url = "/alunos";
        URL_ID = "/alunos/{id}";
        enderecoDTO = new EnderecoDTO(
                "13735999",
                "Rua: das flores",
                "3",
                "Jardim Boa vista",
                "São Paulo",
                Estado.SP,
                null);
        requestDto = new AlunoRequestDTO(
                "389.546.930-08",
                "teste",
                "teste@gmail.com",
                "teste123",
                LocalDate.of(2021, 01, 01),
                Sexo.MASCULINO,
                "19991280033",
                enderecoDTO);

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
        filtro = new AlunoFiltroDTO(
                "teste",
                "93244698000",
                true);
    }

    @Test
    void deveSalvarAlunoComSucesso() throws Exception {

        given(alunoService.salvarAluno(any(AlunoRequestDTO.class))).willReturn(responseDto);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("teste"));

        then(alunoService).should().salvarAluno(any());
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarBusinessExceptionQuandoDadosInvalidos() throws Exception {

        given(alunoService.salvarAluno(any(AlunoRequestDTO.class))).willThrow(new BusinessException("Um ou mais campos estão inválidos"));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Um ou mais campos estão inválidos"));

        then(alunoService).should().salvarAluno(any());
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarAlunoPorId() throws Exception {
        given(alunoService.buscarPorId(responseDto.id())).willReturn(responseDto);

        mockMvc.perform(get(URL_ID, responseDto.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()));

        then(alunoService).should().buscarPorId(responseDto.id());
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarNotFoundExceptionQuandoNaoEncontrado() throws Exception {
        given(alunoService.buscarPorId(responseDto.id())).willThrow(new NotFoundException("Aluno não encontrado"));

        mockMvc.perform(get(URL_ID, responseDto.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        then(alunoService).should().buscarPorId(responseDto.id());
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarBuscaPaginada() throws Exception {
        pageable = PageRequest.of(0, 10);
        paginaAluno = new PageImpl<>(List.of(responseDto), pageable, 1);
        given(alunoService.buscar(any(), any(Pageable.class))).willReturn(paginaAluno);

        mockMvc.perform(get(url)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseDto.id()))
                .andExpect(jsonPath("$.content[0].cpf").value(responseDto.cpf()))
                .andExpect(jsonPath("$.content[0].email").value(responseDto.email()))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        then(alunoService).should().buscar(any(), any(Pageable.class));
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarBadRequestQuandoEmailForInvalido() throws Exception {
        requestDto.setEmail("email-invalido");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        then(alunoService).shouldHaveNoInteractions();
    }

    @Test
    void deveRetornarBadRequestQuandoSexoForInvalido() throws Exception {

        String json = """
                {
                  "cpf":"38954693008",
                  "nome":"teste",
                  "email":"teste@gmail.com",
                  "senha":"123456",
                  "dataNascimento":"2020-01-01",
                  "sexo":"INVALIDO",
                  "telefone":"19999999999"
                }
                """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        then(alunoService).shouldHaveNoInteractions();
    }

    @Test
    void deveDesativarAlunoPorId() throws Exception {
        mockMvc.perform(patch(URL_ID, responseDto.id() + "/" + "desativar"))
                .andExpect(status().isNoContent());

        then(alunoService).should().desativarAluno(responseDto.id());
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarNotFoundQuandoAlunoNaoExistir() throws Exception {
        willThrow(new NotFoundException("Aluno não encontrado")).given(alunoService).desativarAluno(7L);

        mockMvc.perform(patch(URL_ID, 7L + "/" + "desativar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aluno não encontrado"));

        then(alunoService).should().desativarAluno(7L);
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveAtualizarComSucesso() throws Exception {
        given(alunoService.updateAluno(eq(responseDto.id()), any(AlunoRequestDTO.class))).willReturn(responseDto);

        mockMvc.perform(put(URL_ID, responseDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()));

        then(alunoService).should().updateAluno(eq(responseDto.id()), any(AlunoRequestDTO.class));
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarAlunoInexistente() throws Exception {
        given(alunoService.updateAluno(eq(responseDto.id()), any(AlunoRequestDTO.class)))
                .willThrow(new NotFoundException("Aluno não encontrado"));

        mockMvc.perform(put(URL_ID, responseDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aluno não encontrado"));

        then(alunoService).should().updateAluno(eq(responseDto.id()), any(AlunoRequestDTO.class));
        then(alunoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRetornarBadRequestQuandoAtualizarComEmailInvalido() throws Exception {

        requestDto.setEmail("email-invalido");

        mockMvc.perform(put(URL_ID, responseDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        then(alunoService).shouldHaveNoInteractions();
    }
}

