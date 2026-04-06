package com.example.lenpa_backend.dto.funcionario;

import com.example.lenpa_backend.model.NivelPermissao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FuncionarioRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email // Já valida se tem @ e formato de email certinho
        String email,

        @NotBlank
        String senha,

        @NotNull // Usamos NotNull para Enums/Objetos, NotBlank é só pra String
        NivelPermissao nivelPermissao
) {
}