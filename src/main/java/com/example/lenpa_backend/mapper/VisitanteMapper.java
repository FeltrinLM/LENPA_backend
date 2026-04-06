package com.example.lenpa_backend.mapper;

import com.example.lenpa_backend.dto.visitante.DadosCadastroVisitante;
import com.example.lenpa_backend.dto.visitante.DadosDetalhamentoVisitante;
import com.example.lenpa_backend.model.Visitante;
import org.springframework.stereotype.Component;

@Component
public class VisitanteMapper {

    /**
     * Transforma o DTO que veio do Front em uma Entidade para o Banco.
     */
    public Visitante toEntity(DadosCadastroVisitante dados) {
        return new Visitante(
                null, // O ID é nulo porque o banco vai gerar
                dados.nome(),
                dados.cidade(),
                dados.tipo()
        );
    }

    /**
     * Transforma a Entidade que veio do Banco em um DTO para o Front.
     */
    public DadosDetalhamentoVisitante toDetalhamentoDTO(Visitante visitante) {
        return new DadosDetalhamentoVisitante(
                visitante.getId(),
                visitante.getNome(),
                visitante.getCidade(),
                visitante.getTipo()
        );
    }
}