package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.visitante.DadosAtualizacaoVisitante;
import com.example.lenpa_backend.dto.visitante.DadosCadastroVisitante;
import com.example.lenpa_backend.dto.visitante.DadosDetalhamentoVisitante;
import com.example.lenpa_backend.mapper.VisitanteMapper;
import com.example.lenpa_backend.model.Visitante;
import com.example.lenpa_backend.repository.VisitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitanteService {

    @Autowired
    private VisitanteRepository repository;

    @Autowired
    private VisitanteMapper mapper;

    @Transactional
    public DadosDetalhamentoVisitante cadastrar(DadosCadastroVisitante dados) {
        if (repository.existsByNomeAndCidade(dados.nome(), dados.cidade())) {
            throw new RuntimeException("Este visitante já está cadastrado no sistema!");
        }

        var visitante = mapper.toEntity(dados);
        repository.save(visitante);
        return mapper.toDetalhamentoDTO(visitante);
    }

    @Transactional
    public Visitante obterOuCadastrar(DadosCadastroVisitante dados) {
        return repository.findByNomeAndCidade(dados.nome(), dados.cidade())
                .orElseGet(() -> repository.save(mapper.toEntity(dados)));
    }

    public Page<DadosDetalhamentoVisitante> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(mapper::toDetalhamentoDTO);
    }

    public DadosDetalhamentoVisitante buscarPorId(Long id) {
        var visitante = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitante não encontrado!"));
        return mapper.toDetalhamentoDTO(visitante);
    }

    @Transactional
    public DadosDetalhamentoVisitante atualizar(DadosAtualizacaoVisitante dados) {
        // Busca a referência do objeto no banco pelo ID
        var visitante = repository.getReferenceById(dados.id());

        // Atualiza apenas os campos que não vieram nulos no JSON
        if (dados.nome() != null) visitante.setNome(dados.nome());
        if (dados.cidade() != null) visitante.setCidade(dados.cidade());
        if (dados.tipo() != null) visitante.setTipo(dados.tipo());

        // O Spring salva automaticamente ao final do método por causa do @Transactional
        return mapper.toDetalhamentoDTO(visitante);
    }
}