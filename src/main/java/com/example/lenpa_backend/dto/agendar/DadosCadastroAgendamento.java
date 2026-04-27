package com.example.lenpa_backend.dto.agendar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroAgendamento(
        @NotNull(message = "A atividade é obrigatória")
        Long idAtividade,

        @NotBlank(message = "O nome do visitante é obrigatório")
        String nomeVisitante,

        // Pode ser nulo (se for o funcionário agendando alguém por telefone)
        String emailVisitante,

        // Pode ser nulo (se o usuário já estiver no banco e só o nome foi informado)
        String cidadeVisitante,

        @NotNull
        @Positive
        Integer quantidade
) {}