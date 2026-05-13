package com.academia.gym.service.validator;

import com.academia.gym.dto.ErroCampo;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlunoValidatorTest {

    @Mock
    AlunoRepository alunoRepository;
    @InjectMocks
    AlunoValidator alunoValidator;

    String cpf;
    String email;
    Aluno aluno;

    @BeforeEach
    void setup() {
        aluno = new Aluno();
        aluno.setId(1L);
        cpf = "12345678901";
        email = "teste@gmail.com";
    }


    @Test
    void deveLancarExcecaoQuandoCpfJaExistir() {
        when(alunoRepository.existsByCpf(cpf)).thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class,
                () -> alunoValidator.validarCadastro(email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(1, exception.getErros().size());
        ErroCampo erro = exception.getErros().get(0);
        assertEquals("cpf", erro.field());
        assertEquals("CPF já em uso", erro.message());
        assertEquals("CPF_DUPLICADO", erro.code());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        when(alunoRepository.existsByEmail(email)).thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class,
                () -> alunoValidator.validarCadastro(email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(1, exception.getErros().size());
        ErroCampo erro = exception.getErros().get(0);
        assertEquals("email", erro.field());
        assertEquals("Email já em uso", erro.message());
        assertEquals("EMAIL_DUPLICADO", erro.code());
    }

    @Test
    void deveLancarExcecaoQuandoCpfEmailJaExistir() {
        when(alunoRepository.existsByCpf(cpf)).thenReturn(true);
        when(alunoRepository.existsByEmail(email)).thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class,
                () -> alunoValidator.validarCadastro(email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(2, exception.getErros().size());
    }

    @Test
    void deveValidarCadastroComSucesso() {
        when(alunoRepository.existsByCpf(cpf)).thenReturn(false);
        when(alunoRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> alunoValidator.validarCadastro(email, cpf));
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarInativo() {
        aluno.setAtivo(false);

        assertThatThrownBy(() -> alunoValidator.validarUpdate(aluno, cpf, email))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Aluno inativo não pode ser atualizado");
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarCpfDuplicado() {
        aluno.setAtivo(true);
        when(alunoRepository.existsByCpfAndIdNot(cpf, aluno.getId())).thenReturn(true);
        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> alunoValidator.validarUpdate(aluno, email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(1, exception.getErros().size());

        ErroCampo erro = exception.getErros().get(0);

        assertEquals("cpf", erro.field());
        assertEquals("CPF já em uso", erro.message());
        assertEquals("CPF_DUPLICADO", erro.code());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarEmailDuplicado() {
        aluno.setAtivo(true);
        when(alunoRepository.existsByEmailAndIdNot(email, aluno.getId())).thenReturn(true);
        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> alunoValidator.validarUpdate(aluno, email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(1, exception.getErros().size());

        ErroCampo erro = exception.getErros().get(0);

        assertEquals("email", erro.field());
        assertEquals("Email já em uso", erro.message());
        assertEquals("EMAIL_DUPLICADO", erro.code());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarCpfEmailDuplicado() {
        aluno.setAtivo(true);
        when(alunoRepository.existsByCpfAndIdNot(cpf, aluno.getId())).thenReturn(true);
        when(alunoRepository.existsByEmailAndIdNot(email, aluno.getId())).thenReturn(true);
        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> alunoValidator.validarUpdate(aluno, email, cpf));

        assertEquals("Dados inválidos", exception.getMessage());
        assertEquals(2, exception.getErros().size());

    }

    @Test
    void deveValidarUpdateComSucesso() {
        aluno.setAtivo(true);
        when(alunoRepository.existsByCpfAndIdNot(cpf, aluno.getId())).thenReturn(false);
        when(alunoRepository.existsByEmailAndIdNot(email, aluno.getId())).thenReturn(false);

        assertDoesNotThrow(() -> alunoValidator.validarUpdate(aluno, email, cpf));
    }

}