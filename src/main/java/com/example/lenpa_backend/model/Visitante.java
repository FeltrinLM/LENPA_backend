package com.example.lenpa_backend.model;

import com.example.lenpa_backend.dto.visitante.DadosCadastroVisitante;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "visitantes")
@Entity(name = "Visitante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cidade;
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoVisitante tipo;

    // Construtor legado
    public Visitante(DadosCadastroVisitante dados) {
        this.nome = dados.nome();
        this.cidade = dados.cidade();
        this.tipo = dados.tipo();
    }

    // NOVO: Construtor prático para o AgendarService usar na hora do "Auto-Agendamento"
    public Visitante(String nome, String cidade, String email, TipoVisitante tipo) {
        this.nome = nome;
        this.cidade = cidade;
        this.email = email;
        this.tipo = tipo;
    }
}