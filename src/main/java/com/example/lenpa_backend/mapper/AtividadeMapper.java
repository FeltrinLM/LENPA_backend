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

    // Passamos a receber o cálculo de 'vagasDisponiveis' como parâmetro
    public DadosDetalhamentoAtividade toDetalhamentoDTO(Atividade atividade, Integer vagasDisponiveis) {
        return new DadosDetalhamentoAtividade(
                atividade.getIdAtividade(),
                atividade.getNome(),
                atividade.getVagas(),
                vagasDisponiveis, // INSERIDO O NOVO CAMPO AQUI
                atividade.getData(),
                atividade.getHorario(),
                atividade.getLocal(),
                atividade.getDescricao(),
                atividade.getImagem(),
                atividade.getTipo()
        );
    }
}