package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Agendar;
import com.example.lenpa_backend.repository.projection.CidadeRankingProjection;
import com.example.lenpa_backend.repository.projection.EventoRelatorioProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendarRepository extends JpaRepository<Agendar, Long> {

    /**
     * NOVA MÁGICA AQUI: Filtra a listagem geral para trazer apenas quem NÃO está cancelado
     */
    Page<Agendar> findByAgendamentoTrue(Pageable paginacao);

    /**
     * Validação de Duplicidade:
     * Verifica se já existe um agendamento ATIVO para este visitante nesta atividade.
     * (Adicionado 'AndAgendamentoTrue' para permitir que a pessoa remarque caso tenha sido cancelada antes)
     */
    boolean existsByAtividadeIdAtividadeAndVisitanteIdAndAgendamentoTrue(Long idAtividade, Long idVisitante);

    /**
     * Lista todos os agendamentos ATIVOS de uma atividade específica.
     */
    List<Agendar> findAllByAtividadeIdAtividadeAndAgendamentoTrue(Long idAtividade);

    /**
     * Soma de Vagas Ocupadas:
     * Soma o campo 'quantidade' de todos os agendamentos de uma atividade.
     * 🔥 CORREÇÃO IMPORTANTE: Agora só soma se 'agendamento = true' (vagas canceladas voltam a ficar livres)
     */
    @Query("SELECT COALESCE(SUM(a.quantidade), 0) FROM Agendar a WHERE a.atividade.idAtividade = :idAtividade AND a.agendamento = true")
    Integer somarQuantidadeReservada(Long idAtividade);

    /**
     * Listagem geral paginada (caso queira ver todos os agendamentos do sistema)
     */
    Page<Agendar> findAll(Pageable paginacao);

    // ==========================================
    // QUERIES DE RELATÓRIO (MÁGICA DO BANCO DE DADOS)
    // ==========================================

    /**
     * 1. TOTAL DE VISITANTES GERAL:
     * Soma todos os visitantes confirmados em atividades ativas dentro do período.
     */
    @Query("SELECT COALESCE(SUM(a.quantidade), 0) FROM Agendar a " +
            "WHERE a.atividade.data BETWEEN :dataInicio AND :dataFim " +
            "AND a.presenca = true AND a.atividade.ativo = true AND a.agendamento = true")
    Integer totalVisitantesConfirmadosPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    /**
     * 2. RANKING DE CIDADES:
     * Agrupa os visitantes confirmados por cidade, soma a quantidade e ordena do maior para o menor.
     */
    @Query("SELECT v.cidade AS localidade, SUM(a.quantidade) AS total " +
            "FROM Agendar a JOIN a.visitante v " +
            "WHERE a.atividade.data BETWEEN :dataInicio AND :dataFim " +
            "AND a.presenca = true AND a.atividade.ativo = true AND a.agendamento = true " +
            "GROUP BY v.cidade ORDER BY total DESC")
    List<CidadeRankingProjection> findRankingCidadesPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, Pageable pageable);

    /**
     * 3. EVENTOS REALIZADOS:
     * Busca o nome, imagem e soma de visitantes de cada atividade realizada no período.
     */
    @Query("SELECT at.nome AS nome, at.imagem AS imagem, COALESCE(SUM(a.quantidade), 0) AS visitantes " +
            "FROM Agendar a JOIN a.atividade at " +
            "WHERE at.data BETWEEN :dataInicio AND :dataFim " +
            "AND a.presenca = true AND at.ativo = true AND a.agendamento = true " +
            "GROUP BY at.idAtividade, at.nome, at.imagem " +
            "ORDER BY at.data ASC")
    List<EventoRelatorioProjection> findEventosRealizadosPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}