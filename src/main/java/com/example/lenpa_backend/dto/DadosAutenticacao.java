package com.example.lenpa_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}