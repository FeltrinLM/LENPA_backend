package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.dto.FuncionarioRequestDTO;
import com.example.lenpa_backend.dto.FuncionarioResponseDTO;
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

    // CREATE (Criar conta) - Exclusivo ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO response = service.cadastrarFuncionario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // UPDATE (Editar conta) - Exclusivo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO response = service.atualizarFuncionario(id, dto);
        return ResponseEntity.ok(response);
    }

    // DELETE (Excluir conta) - Exclusivo ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}