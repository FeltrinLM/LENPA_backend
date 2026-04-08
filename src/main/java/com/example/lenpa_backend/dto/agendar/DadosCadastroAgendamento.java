package com.example.lenpa_backend.dto.agendar;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroAgendamento(
        @NotNull
        Long idAtividade,

        @NotNull
        Long idVisitante,

        @NotNull
        @Positive
        Integer quantidade
) {}