package com.example.lenpa_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "visitantes")
@Entity(name = "Visitante")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cidade;

    @Enumerated(EnumType.STRING)
    private TipoVisitante tipo;

    // Construtor para o DTO de cadastro
    public Visitante(DadosCadastroVisitante dados) {
        this.nome = dados.nome();
        this.cidade = dados.cidade();
        this.tipo = dados.tipo();
    }
}