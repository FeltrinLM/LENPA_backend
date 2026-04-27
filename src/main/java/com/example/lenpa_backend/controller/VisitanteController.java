package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.dto.visitante.*;
import com.example.lenpa_backend.service.VisitanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/visitantes")
public class VisitanteController {

    @Autowired
    private VisitanteService service;

    /**
     * NOVO: Busca de perfil pelo E-mail (Site)
     * Ex: GET /visitantes/buscar-email?email=joao@email.com
     */
    @GetMapping("/buscar-email")
    public ResponseEntity<DadosDetalhamentoVisitante> buscarPorEmail(@RequestParam String email) {
        var dto = service.buscarPorEmail(email);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    /**
     * NOVO: Busca de perfis pelo Nome (Painel do Funcionário)
     * Ex: GET /visitantes/buscar-nome?nome=Mar
     */
    @GetMapping("/buscar-nome")
    public ResponseEntity<List<DadosDetalhamentoVisitante>> buscarPorNome(@RequestParam String nome) {
        var lista = service.buscarPorNome(nome);
        return ResponseEntity.ok(lista);
    }

    /**
     * CENÁRIO 2: Cadastro manual de visitante para uso futuro.
     */
    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroVisitante dados, UriComponentsBuilder uriBuilder) {
        var dto = service.cadastrar(dados);
        var uri = uriBuilder.path("/visitantes/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /**
     * CENÁRIO 1: O funcionário precisa listar os visitantes
     */
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoVisitante>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoVisitante dados) {
        var dto = service.atualizar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}