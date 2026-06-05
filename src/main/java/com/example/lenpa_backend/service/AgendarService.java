package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.agendar.DadosCadastroAgendamento;
import com.example.lenpa_backend.dto.agendar.DadosDetalhamentoAgendamento;
import com.example.lenpa_backend.mapper.AgendarMapper;
import com.example.lenpa_backend.model.Agendar;
import com.example.lenpa_backend.model.TipoVisitante;
import com.example.lenpa_backend.model.Visitante;
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

    @Autowired
    private EmailService emailService;

    @Transactional
    public DadosDetalhamentoAgendamento agendar(DadosCadastroAgendamento dados) {

        // 1. Busca a Atividade
        var atividade = atividadeRepository.findById(dados.idAtividade())
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada!"));

        // 2. CONCILIAÇÃO DE IDENTIDADE
        Visitante visitante = null;

        // Estratégia A: Busca pelo E-mail (Se foi informado)
        if (dados.emailVisitante() != null && !dados.emailVisitante().isBlank()) {
            visitante = visitanteRepository.findFirstByEmail(dados.emailVisitante()).orElse(null);

            // CORREÇÃO DA CIDADE: Se achou o usuário e ele tem uma cidade nova, atualiza no banco!
            if (visitante != null && dados.cidadeVisitante() != null && !dados.cidadeVisitante().isBlank()) {
                visitante.setCidade(dados.cidadeVisitante());
                visitanteRepository.save(visitante);
            }
        }

        // Estratégia B: Se não achou pelo e-mail, busca por Nome + Cidade
        if (visitante == null) {
            visitante = visitanteRepository.findFirstByNomeAndCidade(dados.nomeVisitante(), dados.cidadeVisitante()).orElse(null);

            // O "Link": Achou a pessoa, ela não tinha e-mail, mas agora digitou um? Vamos atualizar o perfil dela!
            if (visitante != null && visitante.getEmail() == null && dados.emailVisitante() != null) {
                visitante.setEmail(dados.emailVisitante());
                visitanteRepository.save(visitante);
            }
        }

        // Estratégia C: É alguém totalmente novo. Vamos cadastrar do zero!
        if (visitante == null) {
            TipoVisitante tipo = dados.quantidade() > 1 ? TipoVisitante.INSTITUICAO : TipoVisitante.INDIVIDUAL;
            visitante = new Visitante(dados.nomeVisitante(), dados.cidadeVisitante(), dados.emailVisitante(), tipo);
            visitanteRepository.save(visitante);
        }

        // 3. VALIDAÇÃO: Duplicidade (Atualizado para olhar apenas agendamentos ativos)
        if (repository.existsByAtividadeIdAtividadeAndVisitanteIdAndAgendamentoTrue(atividade.getIdAtividade(), visitante.getId())) {
            String motivo = "O visitante já possui um agendamento ativo nesta atividade.";

            // 🔥 Dispara o alerta por e-mail antes de travar a requisição
            emailService.notificarFalhaAgendamento(
                    atividade.getNome(), dados.nomeVisitante(), dados.quantidade(),
                    dados.emailVisitante(), dados.cidadeVisitante(), motivo
            );

            throw new RuntimeException(motivo);
        }

        // 4. VALIDAÇÃO: Lotação (Tem vaga?)
        var ocupadas = repository.somarQuantidadeReservada(atividade.getIdAtividade());
        if (ocupadas == null) ocupadas = 0;

        if ((ocupadas + dados.quantidade()) > atividade.getVagas()) {
            String motivo = "Capacidade máxima excedida! Vagas restantes: " + (atividade.getVagas() - ocupadas);

            // 🔥 Dispara o alerta por e-mail antes de travar a requisição
            emailService.notificarFalhaAgendamento(
                    atividade.getNome(), dados.nomeVisitante(), dados.quantidade(),
                    dados.emailVisitante(), dados.cidadeVisitante(), motivo
            );

            throw new RuntimeException(motivo);
        }

        // 5. Cria e Salva o Agendamento (Reserva confirmada!)
        var agendamento = mapper.toEntity(dados.quantidade(), atividade, visitante);
        agendamento.setAgendamento(true);
        repository.save(agendamento);

        // 6. DISPARO DO E-MAIL DE CONFIRMAÇÃO
        try {
            emailService.notificarNovoAgendamento(
                    atividade.getNome(),
                    visitante.getNome(),
                    dados.quantidade(),
                    visitante.getEmail()
            );
        } catch (Exception e) {
            System.err.println("Aviso: Agendamento salvo, mas falha ao disparar e-mail: " + e.getMessage());
        }

        return mapper.toDetalhamentoDTO(agendamento);
    }

    @Transactional
    public void confirmarPresenca(Long id) {
        // 🔥 Usando findById no lugar da casca (getReferenceById)
        var agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setPresenca(true);
    }

    @Transactional
    public void cancelar(Long id) {
        // 🔥 Usando findById no lugar da casca (getReferenceById)
        var agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setAgendamento(false);
    }

    public Page<DadosDetalhamentoAgendamento> listar(Pageable paginacao) {
        // 🔥 Trocado findAll() por findByAgendamentoTrue()
        return repository.findByAgendamentoTrue(paginacao).map(mapper::toDetalhamentoDTO);
    }
}