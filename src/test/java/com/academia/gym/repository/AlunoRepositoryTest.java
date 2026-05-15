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
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class AlunoRepositoryTest {

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Autowired
    AlunoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Aluno aluno;
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
                null);

        aluno = criarAluno(
                "38954693008",
                "teste",
                "teste@gmail.com");

        entityManager.persist(aluno);
        entityManager.flush();
    }

    @Test
    void deveSalvarAlunoComSucesso() {
        Aluno novoAluno = criarAluno(
                "11122233344",
                "Maria",
                "maria@gmail.com");

        Aluno alunoSalvo = repository.save(novoAluno);

        assertThat(alunoSalvo).isNotNull();
        assertThat(alunoSalvo.getId()).isNotNull();
    }

    @Test
    void deveLancarErroAoSalvarAlunoSemCpf() {

        Aluno alunoSemCpf = criarAluno(
                null,
                "teste",
                "teste@gmail.com"
        );

        assertThatThrownBy(() -> {
            entityManager.persist(alunoSemCpf);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deveLancarErroAoSalvarAlunoSemCampos() {

        Aluno alunoVazio = new Aluno();

        assertThatThrownBy(() -> {
            entityManager.persist(alunoVazio);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deveBuscarAlunoPorNome() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO("teste", null, null)
        );

        validarResultado(resultado);
        assertThat(resultado.getContent().get(0).getNome())
                .isEqualTo("teste");
    }

    @Test
    void deveBuscarAlunoPorCpf() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO(null, "38954693008", null)
        );

        validarResultado(resultado);
        assertThat(resultado.getContent().get(0).getCpf())
                .isEqualTo("38954693008");
    }

    @Test
    void deveBuscarAlunoAtivo() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO(null, null, true)
        );

        validarResultado(resultado);
        assertThat(resultado.getContent().get(0).getAtivo())
                .isTrue();
    }

    @Test
    void deveBuscarAlunoPorNomeECpf() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO("teste", "38954693008", null)
        );

        validarResultado(resultado);

        Aluno alunoEncontrado = resultado.getContent().get(0);

        assertThat(alunoEncontrado.getNome()).isEqualTo("teste");
        assertThat(alunoEncontrado.getCpf()).isEqualTo("38954693008");
    }

    @Test
    void deveRetornarTodosQuandoFiltroEstiverVazio() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO(null, null, null)
        );

        assertThat(resultado.getContent()).hasSize(1);
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoEncontrar() {

        Page<Aluno> resultado = buscar(
                new AlunoFiltroDTO("naoExiste", "00000000000", null)
        );

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }

    @Test
    void deveBuscarAlunoPorId() {

        Optional<Aluno> found = repository.findById(aluno.getId());

        assertThat(found)
                .isPresent()
                .hasValueSatisfying(alunoEncontrado -> {

                    assertThat(alunoEncontrado.getId())
                            .isEqualTo(aluno.getId());

                    assertThat(alunoEncontrado.getEmail())
                            .isEqualTo(aluno.getEmail());
                });
    }

    @Test
    void deveVerificarSeEmailExiste() {

        boolean existe = repository.existsByEmail(aluno.getEmail());

        assertThat(existe).isTrue();
    }

    @Test
    void deveVerificarSeCpfExiste() {
        Boolean existe = repository.existsByCpf(aluno.getCpf());

        assertThat(existe).isTrue();

    }

    @Test
    void deveVerificarSeCpfExisteNoId() {

        boolean existe = repository.existsByCpfAndIdNot(
                aluno.getCpf(),
                aluno.getId()
        );

        assertThat(existe).isFalse();
    }

    @Test
    void deveVerificarSeEmailExisteNoId() {

        boolean existe = repository.existsByEmailAndIdNot(
                aluno.getEmail(),
                aluno.getId()
        );

        assertThat(existe).isFalse();
    }

    private Page<Aluno> buscar(AlunoFiltroDTO filtro) {

        return repository.findAll(
                AlunoSpecification.filtro(filtro),
                PAGEABLE
        );
    }

    private void validarResultado(Page<Aluno> resultado) {

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
    }

    private Aluno criarAluno(String cpf, String nome, String email) {

        Aluno aluno = new Aluno();

        aluno.setCpf(cpf);
        aluno.setNome(nome);
        aluno.setEmail(email);
        aluno.setSenha("123456789");
        aluno.setTelefone("11999999999");
        aluno.setAtivo(true);
        aluno.setDataNascimento(LocalDate.of(2026, 5, 5));
        aluno.setEndereco(endereco);

        return aluno;
    }
}