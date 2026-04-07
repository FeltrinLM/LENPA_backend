package com.example.lenpa_backend.mapper;

import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import com.example.lenpa_backend.dto.atividade.DadosDetalhamentoAtividade;
import com.example.lenpa_backend.model.Atividade;
import org.springframework.stereotype.Component;

@Component
public class AtividadeMapper {

    /**
     * Converte o DTO vindo do Admin (Front) para a Entidade que vai pro Banco.
     */
    public Atividade toEntity(DadosCadastroAtividade dados) {
        // Usamos o construtor que criamos no Model que já seta 'ativo' como true
        return new Atividade(dados);
    }

    /**
     * Converte a Entidade do Banco para o DTO que o Front-end vai exibir.
     */
    public DadosDetalhamentoAtividade toDetalhamentoDTO(Atividade atividade) {
        return new DadosDetalhamentoAtividade(
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