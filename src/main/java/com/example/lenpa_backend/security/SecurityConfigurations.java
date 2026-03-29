package com.example.lenpa_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    // Injetando o nosso filtro recém-criado aqui!
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita CSRF porque nossa API é REST e vamos usar Tokens, então não sofremos esse tipo de ataque
                .csrf(csrf -> csrf.disable())
                // Diz pro Spring não guardar sessão (nada de cookies de login), a autenticação será STATELESS (via Token)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Libera a rota de login para o seu modal do front-end conseguir bater aqui sem tomar bloqueio
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // Exemplo: se você for ter uma rota pública de cadastro, libera ela aqui também:
                        .requestMatchers(HttpMethod.POST, "/auth/registrar").permitAll()
                        // Qualquer outra requisição que chegar no back-end, o usuário OBRIGATORIAMENTE tem que estar logado
                        .anyRequest().authenticated()
                )
                // AQUI ESTÁ A MUDANÇA: Colocamos o nosso filtro ANTES do filtro padrão de login do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Ensina o Spring Security a injetar o AuthenticationManager (que é quem vai de fato testar se o email/senha estão corretos)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Ensina o Spring a usar o BCrypt para encriptar e validar as senhas no banco de dados
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}