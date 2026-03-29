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
        // 1. Pega o token da requisição
        var tokenJWT = recuperarToken(request);

        // 2. Se tiver um token, a gente valida
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // Retorna o email se estiver tudo certo

            // 3. Busca o usuário no banco pelo email
            var usuario = repository.findByEmail(subject).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // 4. Cria o objeto de autenticação e força o login dele no contexto do Spring
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Manda a requisição seguir o fluxo dela (seja pro Controller ou pro bloqueio)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        // O padrão da web é enviar o token com a palavra "Bearer " antes, então a gente remove ela pra ler só o código
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}