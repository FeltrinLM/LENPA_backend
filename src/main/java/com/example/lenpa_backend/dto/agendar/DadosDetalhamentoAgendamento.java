package com.example.lenpa_backend.dto.agendar;

import com.example.lenpa_backend.model.Agendar;

public record DadosDetalhamentoAgendamento(
        Long idAgendamento,
        Long idAtividade,
        String nomeAtividade,
        Long idVisitante,
        String nomeVisitante,
        Integer quantidade,
        Boolean presenca,
        Boolean agendamento
) {
    public DadosDetalhamentoAgendamento(Agendar agendar) {
        this(
                agendar.getIdAgendamento(),
                agendar.getAtividade().getIdAtividade(),
                agendar.getAtividade().getNome(),
                agendar.getVisitante().getId(),
                agendar.getVisitante().getNome(),
                agendar.getQuantidade(),
                agendar.getPresenca(),
                agendar.getAgendamento()
        );
    }
}