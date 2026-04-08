package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.dto.agendar.DadosCadastroAgendamento;
import com.example.lenpa_backend.dto.agendar.DadosDetalhamentoAgendamento;
import com.example.lenpa_backend.service.AgendarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/agendamentos")
public class AgendarController {

    @Autowired
    private AgendarService service;

    /**
     * EFETUAR AGENDAMENTO
     * Aqui é onde as validações de vagas e duplicidade acontecem.
     */
    @PostMapping
    public ResponseEntity agendar(@RequestBody @Valid DadosCadastroAgendamento dados, UriComponentsBuilder uriBuilder) {
        var dto = service.agendar(dados);
        var uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(dto.idAgendamento()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /**
     * LISTAGEM GERAL
     * Retorna todos os agendamentos feitos no sistema.
     */
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoAgendamento>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    /**
     * CONFIRMAR PRESENÇA (Check-in)
     * Rota específica para o dia do evento.
     */
    @PutMapping("/{id}/confirmar")
    public ResponseEntity confirmarPresenca(@PathVariable Long id) {
        service.confirmarPresenca(id);
        return ResponseEntity.ok().build();
    }

    /**
     * CANCELAR AGENDAMENTO (Soft Delete)
     * O registro não some do banco, apenas muda o boolean 'agendamento' para false.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}