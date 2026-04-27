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

import java.util.List;

@Service
public class VisitanteService {

    @Autowired
    private VisitanteRepository repository;

    @Autowired
    private VisitanteMapper mapper;

    // NOVO: Busca por E-mail (usado pelo autocompletar do site)
    public DadosDetalhamentoVisitante buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDetalhamentoDTO) // Reutilizando seu mapper
                .orElse(null); // Retorna nulo se não achar, para o Controller devolver 404
    }

    // NOVO: Busca parcial por Nome (usado pelo autocompletar do painel do funcionário)
    public List<DadosDetalhamentoVisitante> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toDetalhamentoDTO)
                .toList();
    }

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

        // Dica: Se quiser permitir a edição do e-mail no painel depois, basta adicionar:
        // if (dados.email() != null) visitante.setEmail(dados.email());

        // O Spring salva automaticamente ao final do método por causa do @Transactional
        return mapper.toDetalhamentoDTO(visitante);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Visitante não encontrado!");
        }
        repository.deleteById(id);
    }
}