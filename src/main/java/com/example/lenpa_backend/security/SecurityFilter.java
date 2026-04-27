package com.example.lenpa_backend.security;

import com.example.lenpa_backend.repository.FuncionarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FuncionarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                // Tenta validar o token e autenticar o usuário
                var subject = tokenService.getSubject(tokenJWT);
                var usuario = repository.findByEmail(subject).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (RuntimeException e) {
                // 🔥 O PULO DO GATO 🔥
                // Se o token estiver vencido ou inválido, o erro cai aqui.
                // Não deixamos o servidor dar Erro 500. Apenas logamos e o usuário segue "não autenticado".
                System.err.println("Aviso no SecurityFilter: Token rejeitado (" + e.getMessage() + "). Seguindo como visitante anônimo.");
            }
        }

        // Segue o baile!
        // - Se a rota é pública (/agendamentos), funciona.
        // - Se a rota é protegida e o token caiu no catch ali em cima, o Spring devolve um 403 Forbidden limpo.
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        // Retorna apenas a parte do código do token (pula os 7 caracteres de "Bearer ")
        return authorizationHeader.substring(7);
    }
}