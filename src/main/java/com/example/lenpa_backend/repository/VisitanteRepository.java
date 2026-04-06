package com.example.lenpa_backend.repository;


import com.example.lenpa_backend.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    boolean existsByNomeAndCidade(String nome, String cidade);
    Optional<Visitante> findByNomeAndCidade(String nome, String cidade);
}