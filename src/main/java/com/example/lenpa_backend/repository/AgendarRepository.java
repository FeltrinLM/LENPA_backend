package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Agendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendarRepository extends JpaRepository<Agendar, Long> {

    /**
     * Validação de Duplicidade:
     * Verifica se já existe um agendamento para este visitante nesta atividade.
     */
    boolean existsByAtividadeIdAtividadeAndVisitanteId(Long idAtividade, Long idVisitante);

    /**
     * Para os Relatórios:
     * Lista todos os agendamentos de uma atividade específica.
     */
    List<Agendar> findAllByAtividadeIdAtividade(Long idAtividade);

    /**
     * Soma de Vagas Ocupadas:
     * Soma o campo 'quantidade' de todos os agendamentos de uma atividade.
     * Útil para saber se ainda tem vaga antes de confirmar um novo.
     */
    @Query("SELECT SUM(a.quantidade) FROM Agendar a WHERE a.atividade.idAtividade = :idAtividade")
    Integer somarQuantidadeReservada(Long idAtividade);

    /**
     * Listagem geral paginada (caso queira ver todos os agendamentos do sistema)
     */
    Page<Agendar> findAll(Pageable paginacao);
}