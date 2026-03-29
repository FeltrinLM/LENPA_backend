package com.example.lenpa_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.lenpa_backend.model.Funcionario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // O Spring vai puxar essa senha secreta lá do seu application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Funcionario funcionario) {
        try {
            // Define o algoritmo de criptografia usando a nossa senha secreta
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("LENPA_API") // Quem está emitindo o token
                    .withSubject(funcionario.getEmail()) // O "dono" do token (vamos usar o email)
                    .withExpiresAt(dataExpiracao()) // Quando o token vence
                    .sign(algoritmo);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("LENPA_API")
                    .build()
                    .verify(tokenJWT) // Descriptografa e verifica se é válido
                    .getSubject(); // Pega o email que guardamos lá no gerarToken()

        } catch (JWTVerificationException exception) {
            return ""; // Se o token for inválido, expirado ou zoado, retorna vazio
        }
    }

    // Define que o crachá vale por 2 horas a partir do momento do login
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}