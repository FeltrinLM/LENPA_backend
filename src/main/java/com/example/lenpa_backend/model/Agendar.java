package com.example.lenpa_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "agendar") // Exatamente como o nome da tabela no ER
@Entity(name = "Agendar") // Nome da entidade: AGENDAR
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAgendamento")
public class Agendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento") // Batendo com Id_agendamento do diagrama
    private Long idAgendamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atividade") // Batendo com Id_atividade
    private Atividade atividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_visitante") // Batendo com Id_visitante
    private Visitante visitante;

    // --- OS ATRIBUTOS DO DIAGRAMA ---

    private Boolean presenca;    // Presenca
    private Boolean agendamento; // Agendamento
    private Integer quantidade;  // Quantidade

    // --------------------------------

    @PrePersist
    protected void onCreate() {
        if (this.agendamento == null) this.agendamento = true;
        if (this.presenca == null) this.presenca = false;
    }
}