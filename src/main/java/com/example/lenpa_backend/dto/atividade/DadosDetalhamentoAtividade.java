package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.model.TipoAtividade;
import java.time.LocalDate;
import java.time.LocalTime;

public record DadosDetalhamentoAtividade(
        Long idAtividade,
        String nome,
        Integer vagas,
        LocalDate data,
        LocalTime horario,
        String descricao,
        String imagem,
        TipoAtividade tipo
) {
    // Construtor que converte a Entidade diretamente para o DTO
    public DadosDetalhamentoAtividade(Atividade atividade) {
        this(
                atividade.getIdAtividade(),
                atividade.getNome(),
                atividade.getVagas(),
                atividade.getData(),
                atividade.getHorario(),
                atividade.getDescricao(),
                atividade.getImagem(),
                atividade.getTipo()
        );
    }
}