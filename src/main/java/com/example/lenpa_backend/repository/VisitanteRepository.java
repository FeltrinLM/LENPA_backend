package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {

    boolean existsByNomeAndCidade(String nome, String cidade);
    Optional<Visitante> findByNomeAndCidade(String nome, String cidade);

    // NOVO: Busca exata pelo e-mail (O Angular chama quando o visitante digita no site)
    Optional<Visitante> findByEmail(String email);

    // NOVO: Busca parcial pelo nome (O Angular chama pro autocompletar do funcionário)
    List<Visitante> findByNomeContainingIgnoreCase(String nome);
}