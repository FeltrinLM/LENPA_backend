package com.example.lenpa_backend.dto.relatorio;

import com.example.lenpa_backend.repository.projection.CidadeRankingProjection;
import com.example.lenpa_backend.repository.projection.EventoRelatorioProjection;
import java.util.List;

public record DadosRelatorioDTO(
        Integer totalVisitantesGeral,
        List<EventoRelatorioProjection> eventosRealizados,
        List<CidadeRankingProjection> dadosCidades
) {}