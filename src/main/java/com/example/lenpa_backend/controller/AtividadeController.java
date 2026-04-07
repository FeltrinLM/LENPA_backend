package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.dto.atividade.DadosAtualizacaoAtividade;
import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import com.example.lenpa_backend.dto.atividade.DadosDetalhamentoAtividade;
import com.example.lenpa_backend.service.AtividadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    @Autowired
    private AtividadeService service;

    /**
     * CADASTRO (Apenas ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Trava de segurança para Admins
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAtividade dados, UriComponentsBuilder uriBuilder) {
        var dto = service.cadastrar(dados);
        var uri = uriBuilder.path("/atividades/{id}").buildAndExpand(dto.idAtividade()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /**
     * LISTAGEM (Pública/Bolsistas)
     * Retorna apenas as atividades com 'ativo = true'
     */
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoAtividade>> listar(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    /**
     * DETALHAMENTO
     */
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * EXCLUSÃO LÓGICA (Apenas ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Trava de segurança para Admins
    public ResponseEntity excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ATUALIZAÇÃO (Apenas ADMIN)
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoAtividade dados) {
        var dto = service.atualizar(dados);
        return ResponseEntity.ok(dto);
    }
}