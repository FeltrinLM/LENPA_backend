package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.TipoAtividade;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DadosAtualizacaoAtividade(
        @NotNull
        Long idAtividade,
        String nome,
        Integer vagas,
        LocalDate data,
        String horario,
        String descricao,
        String imagem,
        TipoAtividade tipo
) {}