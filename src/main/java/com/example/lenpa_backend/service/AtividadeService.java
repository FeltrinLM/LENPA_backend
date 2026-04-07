package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.atividade.DadosAtualizacaoAtividade;
import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import com.example.lenpa_backend.dto.atividade.DadosDetalhamentoAtividade;
import com.example.lenpa_backend.mapper.AtividadeMapper;
import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository repository;

    @Autowired
    private AtividadeMapper mapper;

    /**
     * Cadastra uma nova atividade (Workshop ou Evento).
     * Toda atividade nasce com o campo 'ativo' como true.
     */
    @Transactional
    public DadosDetalhamentoAtividade cadastrar(DadosCadastroAtividade dados) {
        var atividade = mapper.toEntity(dados);
        repository.save(atividade);
        return mapper.toDetalhamentoDTO(atividade);
    }

    /**
     * Lista apenas as atividades que NÃO foram excluídas pelo Admin.
     * Útil para o front-end mostrar o que está disponível para agendamento.
     */
    public Page<DadosDetalhamentoAtividade> listar(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(mapper::toDetalhamentoDTO);
    }

    /**
     * Busca os detalhes de uma atividade específica pelo ID.
     */
    public DadosDetalhamentoAtividade buscarPorId(Long id) {
        var atividade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada!"));
        return mapper.toDetalhamentoDTO(atividade);
    }

    /**
     * REGRA DE NEGÓCIO: Soft Delete.
     * Em vez de apagar a linha do banco, apenas marcamos como inativo.
     * Isso preserva o histórico de quem participou da atividade no passado.
     */
    @Transactional
    public void excluir(Long id) {
        var atividade = repository.getReferenceById(id);
        atividade.excluir(); // Chama o método no Model que seta ativo = false
    }

    @Transactional
    public DadosDetalhamentoAtividade atualizar(DadosAtualizacaoAtividade dados) {
        var atividade = repository.getReferenceById(dados.idAtividade());

        if (dados.nome() != null) atividade.setNome(dados.nome());
        if (dados.vagas() != null) atividade.setVagas(dados.vagas());
        if (dados.data() != null) atividade.setData(dados.data());
        if (dados.horario() != null) atividade.setHorario(dados.horario());
        if (dados.descricao() != null) atividade.setDescricao(dados.descricao());
        if (dados.imagem() != null) atividade.setImagem(dados.imagem());
        if (dados.tipo() != null) atividade.setTipo(dados.tipo());

        return mapper.toDetalhamentoDTO(atividade);
    }
}