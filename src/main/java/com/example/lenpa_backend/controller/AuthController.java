package com.example.lenpa_backend.controller;

import com.example.lenpa_backend.model.Funcionario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.example.lenpa_backend.security.TokenService;
import com.example.lenpa_backend.dto.funcionario.DadosAutenticacao;
import com.example.lenpa_backend.dto.funcionario.DadosTokenJWT;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // Pega o email e senha que vieram do JSON do front-end
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // O manager vai lá no AutenticacaoService, busca o usuário e compara a senha criptografada
        var authentication = manager.authenticate(authenticationToken);

        // Se a senha bater, a gente gera o Token JWT
        var tokenJWT = tokenService.gerarToken((Funcionario) authentication.getPrincipal());

        // Devolve o token empacotado num JSON bonitinho pro front-end guardar no navegador
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}