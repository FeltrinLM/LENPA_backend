package com.example.lenpa_backend.model;

import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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
    @Column(name = "id_atividade")
    private Long idAtividade;

    private String nome;
    private Integer vagas;
    private LocalDate data;
    private String horario;
    private String local; // NOVO CAMPO ADICIONADO AQUI
    private String descricao;
    private String imagem;

    @Enumerated(EnumType.STRING)
    private TipoAtividade tipo;

    private Boolean ativo;

    public Atividade(DadosCadastroAtividade dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.vagas = dados.vagas();
        this.data = dados.data();
        this.horario = dados.horario();
        this.local = dados.local(); // MAPEAMENTO DO NOVO CAMPO
        this.descricao = dados.descricao();
        this.imagem = dados.imagem();
        this.tipo = dados.tipo();
    }

    public void excluir() {
        this.ativo = false;
    }
}