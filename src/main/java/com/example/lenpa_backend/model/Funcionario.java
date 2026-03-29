package com.example.lenpa_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario implements UserDetails { // <-- A MÁGICA COMEÇA AQUI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuncionario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_permissao", nullable = false)
    private NivelPermissao nivelPermissao;

    @Column(nullable = false, unique = true)
    private String email;

    // --- MÉTODOS OBRIGATÓRIOS DO USERDETAILS ABAIXO ---

    // Define as permissões (roles) do usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.nivelPermissao == NivelPermissao.ADMINISTRADOR) {
            // Se for admin, tem permissão de ADMIN e também as básicas de BOLSISTA
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_BOLSISTA"));
        } else {
            // Se for bolsista, tem só permissão de BOLSISTA
            return List.of(new SimpleGrantedAuthority("ROLE_BOLSISTA"));
        }
    }

    // O Spring pergunta: "Qual atributo é a senha?"
    @Override
    public String getPassword() {
        return senha;
    }

    // O Spring pergunta: "Qual atributo é o login principal?" (Usaremos o email)
    @Override
    public String getUsername() {
        return email;
    }

    // Os métodos abaixo são verificações de conta (bloqueada, expirada, etc).
    // Como não temos isso na regra de negócio agora, deixamos tudo retornando 'true' (ativo).
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}