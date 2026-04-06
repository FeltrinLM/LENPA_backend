package com.example.lenpa_backend.dto.visitante;

import com.example.lenpa_backend.model.TipoVisitante;
import com.example.lenpa_backend.model.Visitante;

public record DadosDetalhamentoVisitante(Long id, String nome, String cidade, TipoVisitante tipo) {
    public DadosDetalhamentoVisitante(Visitante visitante) {
        this(visitante.getId(), visitante.getNome(), visitante.getCidade(), visitante.getTipo());
    }
}