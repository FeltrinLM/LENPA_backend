package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.dto.AtualizarPerfilDTO;
import com.example.lenpa_backend.dto.FuncionarioRequestDTO;
import com.example.lenpa_backend.dto.FuncionarioResponseDTO;
import com.example.lenpa_backend.dto.TrocarSenhaDTO;
import com.example.lenpa_backend.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    // --- ROTAS DE AUTOGESTÃO (VEM PRIMEIRO) ---

    @PutMapping("/meu-perfil")
    public ResponseEntity<FuncionarioResponseDTO> atualizarPerfil(@RequestBody @Valid AtualizarPerfilDTO dto) {
        var emailLogado = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        var response = service.atualizarPerfil(emailLogado, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/minha-senha")
    public ResponseEntity<Void> trocarSenha(@RequestBody @Valid TrocarSenhaDTO dto) {
        var emailLogado = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        service.alterarSenha(emailLogado, dto);
        return ResponseEntity.noContent().build();
    }

    // --- ROTAS ADMINISTRATIVAS (VEM DEPOIS) ---

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO response = service.cadastrarFuncionario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO response = service.atualizarFuncionario(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}