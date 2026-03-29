package com.example.lenpa_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuncionario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    // Usando o Enum que criamos no arquivo separado
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_permissao", nullable = false)
    private NivelPermissao nivelPermissao;

    @Column(nullable = false, unique = true)
    private String email;
}