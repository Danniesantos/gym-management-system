package com.academia.gym.service.validator;

import com.academia.gym.dto.ErroCampo;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.repository.AlunoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.academia.gym.dto.ErroCampo.erro;

@RequiredArgsConstructor
@Component
@Getter
public class AlunoValidator {

    private final AlunoRepository alunoRepository;

    private static final String EMAIL_DUPLICADO = "EMAIL_DUPLICADO";
    private static final String CPF_DUPLICADO = "CPF_DUPLICADO";

    public void validarCadastro(String email, String cpf) {
        validar(email, cpf, null);
    }

    public void validarUpdate(Aluno aluno, String email, String cpf) {

        if (!aluno.getAtivo()) {
            throw new ConflictException("Aluno inativo não pode ser atualizado");
        }

        validar(email, cpf, aluno.getId());
    }

    private void validar(String email, String cpf, Long id) {

        List<ErroCampo> erros = new ArrayList<>();

        validarCpfDuplicado(cpf, id, erros);
        validarEmailDuplicado(email, id, erros);

        if (!erros.isEmpty()) {
            throw new BusinessException("Dados inválidos", erros);
        }
    }

    private void validarCpfDuplicado(String cpf, Long id, List<ErroCampo> erros) {

        if (cpf == null) return;

        boolean existe = (id == null)
                ? alunoRepository.existsByCpf(cpf)
                : alunoRepository.existsByCpfAndIdNot(cpf, id);

        if (existe) {
            erros.add(erro("cpf", "CPF já em uso", CPF_DUPLICADO));
        }
    }

    private void validarEmailDuplicado(String email, Long id, List<ErroCampo> erros) {

        if (email == null) return;

        boolean existe = (id == null)
                ? alunoRepository.existsByEmail(email)
                : alunoRepository.existsByEmailAndIdNot(email, id);

        if (existe) {
            erros.add(erro("email", "Email já em uso", EMAIL_DUPLICADO));
        }
    }

}





