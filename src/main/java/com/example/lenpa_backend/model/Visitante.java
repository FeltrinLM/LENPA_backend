package com.example.lenpa_backend.model;

import com.example.lenpa_backend.dto.visitante.DadosCadastroVisitante;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "visitantes")
@Entity(name = "Visitante")
@Getter
@Setter // Gera o setNome, setCidade e setTipo
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cidade; // <-- AQUI: mudei de 'city' para 'cidade' para bater com o Service

    @Enumerated(EnumType.STRING)
    private TipoVisitante tipo;

    // Construtor para o DTO de cadastro (Ajustado para 'cidade')
    public Visitante(DadosCadastroVisitante dados) {
        this.nome = dados.nome();
        this.cidade = dados.cidade();
        this.tipo = dados.tipo();
    }
}