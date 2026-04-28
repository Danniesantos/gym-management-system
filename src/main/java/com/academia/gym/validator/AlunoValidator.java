package com.academia.gym.validator;

import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AlunoValidator {

    private final AlunoRepository alunoRepository;

    public void validarCadastro(String email, String cpf) {

        String cpfLimpo = limparCpf(cpf);

        if (alunoRepository.existsByEmail(email)) {
            throw new ConflictException("Email já cadastrado");
        }

        if (alunoRepository.existsByCpf(cpfLimpo)) {
            throw new ConflictException("Cpf já cadastrado");
        }

        validarFormatoCpf(cpfLimpo);
    }

    public void validarUpdate(String email, String cpf, Long id) {

        if (email != null && !email.isBlank() &&
                alunoRepository.existsByEmailAndIdNot(email, id)) {
            throw new ConflictException("Email já cadastrado");
        }

        if (cpf != null && !cpf.isBlank()) {

            String cpfLimpo = limparCpf(cpf);

            if (alunoRepository.existsByCpfAndIdNot(cpfLimpo, id)) {
                throw new ConflictException("Cpf já cadastrado");
            }

            validarFormatoCpf(cpfLimpo);
        }
    }

    private String limparCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private void validarFormatoCpf(String cpf) {
        if (!cpf.matches("\\d{11}")) {
            throw new BusinessException("CPF inválido");
        }

        if (cpf.chars().distinct().count() == 1) {
            throw new BusinessException("CPF inválido");
        }
    }

    public void validarAlunoAtivo(Aluno aluno) {
        if (!aluno.getAtivo()) {
            throw new BusinessException("Aluno inativo não pode ser atualizado");
        }
    }


}


