package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.TipoAtividade;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record DadosAtualizacaoAtividade(
        @NotNull
        Long idAtividade,
        String nome,
        Integer vagas,
        LocalDate data,
        LocalTime horario,
        String descricao,
        String imagem,
        TipoAtividade tipo
) {}