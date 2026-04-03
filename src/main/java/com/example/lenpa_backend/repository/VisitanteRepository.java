package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    // Aqui o Spring Data JPA já faz toda a mágica do CRUD sozinho.
}