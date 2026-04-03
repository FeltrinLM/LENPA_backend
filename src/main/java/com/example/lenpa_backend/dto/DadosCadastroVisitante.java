package com.example.lenpa_backend.dto;

import com.example.lenpa_backend.model.TipoVisitante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroVisitante(
        @NotBlank String nome,
        @NotBlank String cidade,
        @NotNull TipoVisitante tipo
) {}