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
                var subject = tokenService.getSubject(tokenJWT);

                // Defesa: E se o token for válido, mas o usuário foi deletado do banco ontem?
                var usuario = repository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("O usuário do token não existe mais no banco de dados."));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // 🔥 DEFESA MÁXIMA: Capturamos Exception genérica.
                // Se a leitura falhar por QUALQUER motivo, o servidor não crasha e limpa o contexto.
                System.err.println("🛡️ SecurityFilter interceptou um token problemático e bloqueou o acesso. Motivo: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        // Defesa 1: Verifica se existe e tem o tamanho mínimo viável
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() <= 7) {
            return null;
        }

        String token = authorizationHeader.substring(7).trim();

        // 🔥 Defesa 2 (O matador de bugs): Bloqueia strings literais de erro do Frontend
        if (token.isEmpty() || token.equals("null") || token.equals("undefined")) {
            return null;
        }

        return token;
    }
}