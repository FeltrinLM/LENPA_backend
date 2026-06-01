package com.example.lenpa_backend.repository;

import com.example.lenpa_backend.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {

    boolean existsByNomeAndCidade(String nome, String cidade);

    // ATUALIZADO: Traz apenas o primeiro se houver duplicidade de Nome e Cidade
    Optional<Visitante> findFirstByNomeAndCidade(String nome, String cidade);

    // ATUALIZADO: Traz apenas o primeiro se houver duplicidade de E-mail
    Optional<Visitante> findFirstByEmail(String email);

    // MANTIDO: Busca parcial pelo nome (O Angular chama pro autocompletar do funcionário)
    List<Visitante> findByNomeContainingIgnoreCase(String nome);
}