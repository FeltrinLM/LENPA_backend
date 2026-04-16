package com.example.lenpa_backend.mapper;

import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import com.example.lenpa_backend.dto.atividade.DadosDetalhamentoAtividade;
import com.example.lenpa_backend.model.Atividade;
import org.springframework.stereotype.Component;

@Component
public class AtividadeMapper {

    public Atividade toEntity(DadosCadastroAtividade dados) {
        return new Atividade(dados);
    }

    public DadosDetalhamentoAtividade toDetalhamentoDTO(Atividade atividade) {
        return new DadosDetalhamentoAtividade(
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