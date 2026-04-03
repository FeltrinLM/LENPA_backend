package com.example.lenpa_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "atividades")
@Entity(name = "Atividade")
@Getter
@Setter // Adicionado para facilitar atualizações
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer vagas;
    private LocalDate data;
    private LocalTime horario;
    private String descricao;

    // Guardaremos o caminho da imagem (ex: "assets/images/banner1.jpg" ou uma URL)
    private String imagem;

    @Enumerated(EnumType.STRING)
    private TipoAtividade tipo;
}