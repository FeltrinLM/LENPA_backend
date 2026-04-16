package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.model.TipoAtividade;
import java.time.LocalDate;

public record DadosDetalhamentoAtividade(
        Long idAtividade,
        String nome,
        Integer vagas,
        LocalDate data,
        String horario,
        String local, // NOVO CAMPO
        String descricao,
        String imagem,
        TipoAtividade tipo
) {
    public DadosDetalhamentoAtividade(Atividade atividade) {
        this(
                atividade.getIdAtividade(),
                atividade.getNome(),
                atividade.getVagas(),
                atividade.getData(),
                atividade.getHorario(),
                atividade.getLocal(), // MAPEAMENTO DO NOVO CAMPO
                atividade.getDescricao(),
                atividade.getImagem(),
                atividade.getTipo()
        );
    }
}