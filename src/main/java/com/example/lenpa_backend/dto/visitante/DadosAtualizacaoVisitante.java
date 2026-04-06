package com.example.lenpa_backend.dto.visitante;

import com.example.lenpa_backend.model.TipoVisitante;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoVisitante(
        @NotNull Long id, // Precisamos do ID para saber quem editar
        String nome,
        String cidade,
        TipoVisitante tipo
) {}