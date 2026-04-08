package com.example.lenpa_backend.mapper;

import com.example.lenpa_backend.dto.agendar.DadosDetalhamentoAgendamento;
import com.example.lenpa_backend.model.Agendar;
import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.model.Visitante;
import org.springframework.stereotype.Component;

@Component
public class AgendarMapper {

    /**
     * Converte a Entidade 'Agendar' para o DTO de Detalhamento.
     * Esse é o que o seu Front vai usar para mostrar a lista de presença.
     */
    public DadosDetalhamentoAgendamento toDetalhamentoDTO(Agendar agendar) {
        return new DadosDetalhamentoAgendamento(agendar);
    }

    /**
     * Auxilia na criação da Entidade Agendar.
     * Como o agendamento depende de objetos que já existem no banco (Atividade e Visitante),
     * o Service passa esses objetos para o Mapper montar a estrutura.
     */
    public Agendar toEntity(Integer quantidade, Atividade atividade, Visitante visitante) {
        var agendar = new Agendar();
        agendar.setAtividade(atividade);
        agendar.setVisitante(visitante);
        agendar.setQuantidade(quantidade);

        // agendamento e presenca serão setados automaticamente pelo @PrePersist no Model
        return agendar;
    }
}