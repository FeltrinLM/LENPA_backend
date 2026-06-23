package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.model.TipoAtividade;
import java.time.LocalDate;

public record DadosDetalhamentoAtividade(
        Long idAtividade,
        String nome,
        Integer vagas,            // Vagas totais (capacidade)
        Integer vagasDisponiveis, // <-- NOVO CAMPO QUE O ANGULAR VAI LER
        LocalDate data,
        String horario,
        String local,
        String descricao,
        String imagem,
        TipoAtividade tipo,
        Boolean ativo
) {
    // Ajuste no construtor para receber o valor calculado externamente
    public DadosDetalhamentoAtividade(Atividade atividade, Integer vagasDisponiveis) {
        this(
                atividade.getIdAtividade(),
                atividade.getNome(),
                atividade.getVagas(),
                vagasDisponiveis, // <-- MAPEAMENTO DO VALOR CALCULADO
                atividade.getData(),
                atividade.getHorario(),
                atividade.getLocal(),
                atividade.getDescricao(),
                atividade.getImagem(),
                atividade.getTipo(),
                atividade.getAtivo()
        );
    }
}