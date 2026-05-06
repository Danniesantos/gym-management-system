package com.academia.gym.service.validator;

import com.academia.gym.dto.ErroCampo;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AlunoValidator {


    private final AlunoRepository alunoRepository;

    private static final String EMAIL_DUPLICADO = "EMAIL_DUPLICADO";
    private static final String CPF_DUPLICADO = "CPF_DUPLICADO";
    private static final String VALIDATION_ERROR = "Dados inválidos";

    public void validarCadastro(String email, String cpf) {

        List<ErroCampo> erros = new ArrayList<>();

        String emailNormalizado = email != null ? email.trim() : null;

        if (emailNormalizado != null &&
                alunoRepository.existsByEmail(emailNormalizado)) {
            erros.add(erro("email", "Email já cadastrado", EMAIL_DUPLICADO));
        }

        if (cpf != null) {
            String cpfLimpo = limparCpf(cpf);

            if (alunoRepository.existsByCpf(cpfLimpo)) {
                erros.add(erro("cpf", "CPF já cadastrado", CPF_DUPLICADO));
            }
        }

        if (!erros.isEmpty()) {
            throw new BusinessException(VALIDATION_ERROR, erros);
        }
    }

    public void validarUpdate(String email, String cpf, Long id) {

        List<ErroCampo> erros = new ArrayList<>();

        String emailNormalizado = email != null ? email.trim() : null;

        if (emailNormalizado != null &&
                alunoRepository.existsByEmailAndIdNot(emailNormalizado, id)) {

            erros.add(erro("email", "Email já em uso", EMAIL_DUPLICADO));
        }

        if (cpf != null) {
            String cpfLimpo = limparCpf(cpf);

            if (alunoRepository.existsByCpfAndIdNot(cpfLimpo, id)) {
                erros.add(erro("cpf", "CPF já em uso", CPF_DUPLICADO));
            }
        }

        if (!erros.isEmpty()) {
            throw new BusinessException(VALIDATION_ERROR, erros);
        }
    }

    private String limparCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    public void validarAlunoAtivo(Aluno aluno) {
        if (!aluno.getAtivo()) {
            throw new ConflictException("Aluno inativo não pode ser atualizado");
        }
    }

    private ErroCampo erro(String campo, String mensagem, String code) {
        return new ErroCampo(campo, mensagem, code);
    }
}





