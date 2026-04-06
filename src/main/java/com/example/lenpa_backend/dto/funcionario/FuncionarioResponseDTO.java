package com.example.lenpa_backend.dto.funcionario;

import com.example.lenpa_backend.model.NivelPermissao;

// Aqui não precisa de validação (@NotBlank) porque somos nós (o banco) que estamos devolvendo os dados
public record FuncionarioResponseDTO(
        Long idFuncionario,
        String nome,
        String email,
        NivelPermissao nivelPermissao
        // Reparou que a SENHA não está aqui? É de propósito!
) {
}