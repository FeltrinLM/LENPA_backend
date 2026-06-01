package com.example.lenpa_backend.dto.agendar;

import com.example.lenpa_backend.model.Agendar;

public record DadosDetalhamentoAgendamento(
        Long idAgendamento,
        Long idAtividade,
        String nomeAtividade,
        Long idVisitante,
        String nomeVisitante,

        // A MÁGICA AQUI: O campo que o Angular estava implorando para receber!
        String cidadeVisitante,

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

                // Puxa a cidade direto da Entidade Visitante que está dentro do Agendar
                agendar.getVisitante().getCidade(),

                agendar.getQuantidade(),
                agendar.getPresenca(),
                agendar.getAgendamento()
        );
    }
}