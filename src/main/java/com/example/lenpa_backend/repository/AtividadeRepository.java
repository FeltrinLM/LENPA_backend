package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Atividade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    // Busca apenas as atividades que o Admin não "deletou" (ativo = true)
    // Usamos Page para garantir que o front-end receba os dados paginados
    Page<Atividade> findAllByAtivoTrue(Pageable paginacao);
}