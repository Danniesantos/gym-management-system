package com.academia.gym.controller;

import com.academia.gym.dto.aluno.*;
import com.academia.gym.mapper.aluno.AlunoMapper;
import com.academia.gym.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final AlunoMapper alunoMapper;

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> salvarAluno(@Validated(OnCreate.class) @RequestBody AlunoRequestDTO alunoDto) {

        AlunoResponseDTO response = alunoService.salvarAluno(alunoDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<AlunoResponseDTO>> buscar(@ModelAttribute AlunoFiltroDTO filtro,
                                                         @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(alunoService.buscar(filtro, pageable));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarAluno(@PathVariable Long id) {
        alunoService.desativarAluno(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<AlunoResponseDTO> updateAluno(@PathVariable Long id,
                                                        @Validated(OnUpdate.class) @RequestBody AlunoRequestDTO updateDTO) {
        return ResponseEntity.ok(alunoService.updateAluno(id, updateDTO));
    }


}
