package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.TipoAtividade;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record DadosAtualizacaoAtividade(
        @NotNull
        Long idAtividade,

        String nome,

        Integer vagas,

        @FutureOrPresent(message = "A data da atividade não pode estar no passado")
        LocalDate data,

        String horario,

        String local,

        String descricao,

        String imagem,

        TipoAtividade tipo
) {}