package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.relatorio.DadosRelatorioDTO;
import com.example.lenpa_backend.repository.AgendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RelatorioService {

    @Autowired
    private AgendarRepository agendarRepository;

    public DadosRelatorioDTO gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        // 1. Total Geral de Visitantes Confirmados
        Integer totalVisitantes = agendarRepository.totalVisitantesConfirmadosPeriodo(dataInicio, dataFim);

        // 2. Eventos Realizados e seus visitantes
        var eventos = agendarRepository.findEventosRealizadosPeriodo(dataInicio, dataFim);

        // 3. Top 10 Cidades (O PageRequest(0, 10) limita o resultado a apenas os 10 primeiros)
        var rankingCidades = agendarRepository.findRankingCidadesPeriodo(dataInicio, dataFim, PageRequest.of(0, 1000));

        // Empacota tudo no DTO e devolve
        return new DadosRelatorioDTO(totalVisitantes, eventos, rankingCidades);
    }
}