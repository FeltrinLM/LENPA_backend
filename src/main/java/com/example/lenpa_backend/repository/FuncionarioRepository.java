package com.example.lenpa_backend.repository; // Ajuste o pacote conforme o seu projeto

import com.example.lenpa_backend.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Já deixei esse método pronto porque você com certeza vai usar muito!
    Optional<Funcionario> findByEmail(String email);

}