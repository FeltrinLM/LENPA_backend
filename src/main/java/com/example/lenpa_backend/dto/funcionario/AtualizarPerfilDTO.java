package com.example.lenpa_backend.dto.funcionario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtualizarPerfilDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        String nome,

        @NotBlank(message = "O e-mail não pode estar vazio")
        @Email(message = "Formato de e-mail inválido")
        String email
) {
}