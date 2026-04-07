package com.example.lenpa_backend.dto.atividade;

import com.example.lenpa_backend.model.TipoAtividade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.time.LocalTime;

public record DadosCadastroAtividade(
        @NotBlank
        String nome,

        @NotNull
        Integer vagas,

        @NotNull
        @FutureOrPresent
        LocalDate data,

        @NotNull
        LocalTime horario,

        String descricao,

        String imagem,

        @NotNull
        TipoAtividade tipo
) {}