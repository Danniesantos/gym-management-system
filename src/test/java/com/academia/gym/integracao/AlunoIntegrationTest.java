package com.academia.gym.integracao;

import com.academia.gym.dto.aluno.AlunoRequestDTO;
import com.academia.gym.dto.aluno.AlunoResponseDTO;
import com.academia.gym.dto.aluno.EnderecoDTO;
import com.academia.gym.model.aluno.Estado;
import com.academia.gym.model.aluno.Sexo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AlunoIntegrationTest {

    private static final String URL = "/alunos";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private EnderecoDTO endereco;
    private AlunoRequestDTO requestDTO;
    private AlunoRequestDTO outroAluno;

    @BeforeEach
    void setup() {
        endereco = new EnderecoDTO(
                "13500855",
                "Rua teste",
                "10",
                "Centro",
                "São Paulo",
                Estado.SP,
                null);
        requestDTO = criarRequestAluno(
                "38954693008",
                "teste",
                "teste@gmail.com");
        outroAluno = criarRequestAluno(
                "78771510044", "outro", "outro@gmail.com");
    }

    @Test
    void deveCadastrarAlunoComSucesso() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(requestDTO.getNome()))
                .andExpect(jsonPath("$.email").value(requestDTO.getEmail()));
    }

    @Test
    void deveRetornar400QuandoCamposNullos() throws Exception {
        AlunoRequestDTO alunoInvalido = new AlunoRequestDTO();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(alunoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar422QuandoCamposDuplicados() throws Exception {
        criarAluno(requestDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveAtualizarAlunoComSucesso() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);
        AlunoRequestDTO requestAtualizacao = criarRequestAluno(
                requestDTO.getCpf(), "testando", requestDTO.getEmail());

        mockMvc.perform(put(URL + "/" + alunoSalvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("testando"));
    }

    @Test
    void deveRetornar422QuandoAtualizarCamposExistentes() throws Exception {
        AlunoResponseDTO aluno1 = criarAluno(requestDTO);
        AlunoResponseDTO aluno2 = criarAluno(outroAluno);
        outroAluno.setCpf(aluno1.cpf());

        mockMvc.perform(put(URL + "/" + aluno2.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(outroAluno)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar404QuandoAlunoInexistente() throws Exception {
        mockMvc.perform(put(URL + "/" + 900L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDesativarAlunoComSucesso() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);

        mockMvc.perform(patch(URL + "/" + alunoSalvo.id() + "/desativar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDesativarAlunoInexistente() throws Exception {
        mockMvc.perform(patch(URL + "/" + 999L + "/desativar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarAlunoPorId() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);

        mockMvc.perform(get(URL + "/" + alunoSalvo.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(alunoSalvo.nome()))
                .andExpect(jsonPath("$.cpf").value(alunoSalvo.cpf()));
    }

    @Test
    void deveRetornar404QuandoAlunoNaoEncontrado() throws Exception {
        mockMvc.perform(get(URL + "/" + 100L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarAlunoPaginadoPorNome() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);

        mockMvc.perform(get(URL)
                        .param("nome", requestDTO.getNome())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].cpf").value(hasItem(alunoSalvo.cpf())));
    }

    @Test
    void deveBuscarAlunoPaginadoPorCpf() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);

        mockMvc.perform(get(URL)
                        .param("cpf", requestDTO.getCpf())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].cpf").value(hasItem(alunoSalvo.cpf())));
    }

    @Test
    void deveBuscarAlunoComFiltros() throws Exception {
        AlunoResponseDTO alunoSalvo = criarAluno(requestDTO);

        mockMvc.perform(get(URL).param("cpf", requestDTO.getCpf())
                        .param("nome", requestDTO.getNome())
                        .param("ativo", "true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].cpf").value(hasItem(alunoSalvo.cpf())));
    }

    private AlunoRequestDTO criarRequestAluno(String cpf, String nome, String email) {
        return new AlunoRequestDTO(
                cpf,
                nome,
                email,
                "123456",
                LocalDate.of(2000, 1, 1),
                Sexo.FEMININO,
                "11999999999",
                endereco);
    }

    private AlunoResponseDTO criarAluno(AlunoRequestDTO dto) throws Exception {
        String response = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, AlunoResponseDTO.class);
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}



