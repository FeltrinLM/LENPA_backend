package com.example.lenpa_backend.model;

import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "atividades")
@Entity(name = "Atividade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAtividade")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade") // Batendo com Id_atividade do ER
    private Long idAtividade;

    private String nome;
    private Integer vagas;
    private LocalDate data;
    private LocalTime horario;
    private String descricao;
    private String imagem;

    @Enumerated(EnumType.STRING)
    private TipoAtividade tipo;

    // Campo essencial para o Admin "excluir" sem apagar do banco (preserva relatórios)
    private Boolean ativo;

    // Construtor para facilitar a criação via Service
    public Atividade(DadosCadastroAtividade dados) {
        this.ativo = true; // Toda atividade nasce ativa
        this.nome = dados.nome();
        this.vagas = dados.vagas();
        this.data = dados.data();
        this.horario = dados.horario();
        this.descricao = dados.descricao();
        this.imagem = dados.imagem();
        this.tipo = dados.tipo();
    }

    // Método para o Admin "excluir"
    public void excluir() {
        this.ativo = false;
    }
}