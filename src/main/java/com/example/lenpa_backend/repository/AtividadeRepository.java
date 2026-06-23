package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Atividade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    // Busca apenas as atividades que o Admin não "deletou" (ativo = true)
    Page<Atividade> findAllByAtivoTrue(Pageable paginacao);

    // 🔥 NOVO: Busca por ID, mas SÓ se a atividade estiver ativa
    Optional<Atividade> findByIdAtividadeAndAtivoTrue(Long idAtividade);
}