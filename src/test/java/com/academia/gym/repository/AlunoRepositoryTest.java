package com.academia.gym.repository;

import com.academia.gym.dto.aluno.AlunoFiltroDTO;
import com.academia.gym.model.aluno.Aluno;
import com.academia.gym.model.aluno.Endereco;
import com.academia.gym.model.aluno.Estado;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class AlunoRepositoryTest {

    @Autowired
    AlunoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Aluno aluno;
    Aluno aluno1;
    Endereco endereco;

    @BeforeEach
    void setup() {

        endereco = new Endereco(
                "13500855",
                "Rua: das arvores",
                "7",
                "Jardim Bela vista",
                "São Paulo",
                Estado.SP,
                null
        );

        aluno = new Aluno();

        aluno.setCpf("38954693008");
        aluno.setNome("teste");
        aluno.setEmail("teste@gmail.com");
        aluno.setSenha("123456789");
        aluno.setTelefone("11999999999");
        aluno.setAtivo(true);
        aluno.setDataNascimento(LocalDate.of(2026, 5, 5));

        aluno.setEndereco(endereco);

        entityManager.persist(aluno);
        entityManager.flush();

        aluno1 = new Aluno();
        aluno1.setNome("teste");
        aluno1.setEmail("teste@gmail.com");
        aluno1.setSenha("123456789");
        aluno1.setTelefone("11999999999");
        aluno1.setAtivo(true);
        aluno1.setDataNascimento(LocalDate.of(2026, 5, 5));
        aluno1.setEndereco(endereco);

    }

    @Test
    void deveSalvarAlunoComSucesso() {
        Aluno novoAluno = new Aluno();
        novoAluno.setCpf("11122233344");
        novoAluno.setNome("Maria");
        novoAluno.setEmail("maria@gmail.com");
        novoAluno.setSenha("123456");
        novoAluno.setTelefone("11999999998");
        novoAluno.setAtivo(true);
        novoAluno.setDataNascimento(LocalDate.of(2000, 1, 1));
        novoAluno.setEndereco(endereco);

        Aluno save = repository.save(novoAluno);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
    }

    @Test
    void deveLancarErroAoSalvarAlunoSemCpf() {

        assertThatThrownBy(() -> {
            entityManager.persist(aluno1);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deveLancarErroAoSalvarAlunoSemCampos() {

        Aluno aluno2 = new Aluno();
        assertThatThrownBy(() -> {
            entityManager.persist(aluno2);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deveBuscarAlunoPorNome() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                "teste",
                null,
                null
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("teste");
    }

    @Test
    void deveBuscarAlunoPorCpf() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                null,
                "38954693008",
                null
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getCpf()).isEqualTo("38954693008");
    }

    @Test
    void deveBuscarAlunoAtivo() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                null,
                null,
                true
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getAtivo()).isEqualTo(true);
    }

    @Test
    void deveBuscarAlunoPorNomeECpf() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                "teste",
                "38954693008",
                null
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("teste");
        assertThat(resultado.getContent().get(0).getCpf()).isEqualTo("38954693008");
    }

    @Test
    void deveRetornarTodosQuandoFiltroEstiverVazio() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                null,
                null,
                null
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoEncontrar() {
        AlunoFiltroDTO filtro = new AlunoFiltroDTO(
                "naoExiste",
                "00000000000",
                null
        );

        Page<Aluno> resultado = repository.findAll(AlunoSpecification.filtro(filtro),
                PageRequest.of(0, 10));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }

    @Test
    void deveBuscarAlunoPorId() {
        Optional<Aluno> found = repository.findById(aluno.getId());

        assertThat(found).isPresent()
                .hasValueSatisfying(alunoEncontrado -> {

                    assertThat(alunoEncontrado.getId())
                            .isEqualTo(aluno.getId());

                    assertThat(alunoEncontrado.getEmail())
                            .isEqualTo(aluno.getEmail());

                });
    }

    @Test
    void deveVerificarSeEmailExiste() {
        Boolean existe = repository.existsByEmail(aluno.getEmail());

        assertThat(existe).isTrue();

    }

    @Test
    void deveVerificarSeCpfExiste() {
        Boolean existe = repository.existsByCpf(aluno.getCpf());

        assertThat(existe).isTrue();

    }

    @Test
    void deveVerificarSeCpfExisteNoId() {
        Boolean existe = repository.existsByCpfAndIdNot(aluno.getCpf(), aluno.getId());

        assertThat(existe).isFalse();

    }

    @Test
    void deveVerificarSeEmailExisteNoId() {
        Boolean existe = repository.existsByEmailAndIdNot(aluno.getEmail(), aluno.getId());

        assertThat(existe).isFalse();

    }
}