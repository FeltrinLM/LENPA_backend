package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.agendar.DadosCadastroAgendamento;
import com.example.lenpa_backend.dto.agendar.DadosDetalhamentoAgendamento;
import com.example.lenpa_backend.mapper.AgendarMapper;
import com.example.lenpa_backend.model.Agendar;
import com.example.lenpa_backend.repository.AgendarRepository;
import com.example.lenpa_backend.repository.AtividadeRepository;
import com.example.lenpa_backend.repository.VisitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendarService {

    @Autowired
    private AgendarRepository repository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private VisitanteRepository visitanteRepository;

    @Autowired
    private AgendarMapper mapper;

    @Transactional
    public DadosDetalhamentoAgendamento agendar(DadosCadastroAgendamento dados) {
        // 1. Busca a Atividade e o Visitante (Garante que existem)
        var atividade = atividadeRepository.findById(dados.idAtividade())
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada!"));

        var visitante = visitanteRepository.findById(dados.idVisitante())
                .orElseThrow(() -> new RuntimeException("Visitante não encontrado!"));

        // 2. VALIDAÇÃO: Duplicidade (A regra do "Fulano já está aqui")
        if (repository.existsByAtividadeIdAtividadeAndVisitanteId(atividade.getIdAtividade(), visitante.getId())) {
            throw new RuntimeException("O visitante " + visitante.getNome() + " já está agendado nesta atividade!");
        }

        // 3. VALIDAÇÃO: Lotação (Tem vaga?)
        var ocupadas = repository.somarQuantidadeReservada(atividade.getIdAtividade());
        if (ocupadas == null) ocupadas = 0; // Se for o primeiro agendamento

        if ((ocupadas + dados.quantidade()) > atividade.getVagas()) {
            throw new RuntimeException("Capacidade máxima excedida! Vagas restantes: " + (atividade.getVagas() - ocupadas));
        }

        // 4. Cria e Salva
        var agendamento = mapper.toEntity(dados.quantidade(), atividade, visitante);
        repository.save(agendamento);

        return mapper.toDetalhamentoDTO(agendamento);
    }

    /**
     * Confirma a presença (O "Check-in" do dia do evento)
     */
    @Transactional
    public void confirmarPresenca(Long id) {
        var agendamento = repository.getReferenceById(id);
        agendamento.setPresenca(true);
    }

    /**
     * Cancela o agendamento (Seta o boolean agendamento para false)
     */
    @Transactional
    public void cancelar(Long id) {
        var agendamento = repository.getReferenceById(id);
        agendamento.setAgendamento(false);
    }

    public Page<DadosDetalhamentoAgendamento> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(mapper::toDetalhamentoDTO);
    }
}