package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.funcionario.AtualizarPerfilDTO;
import com.example.lenpa_backend.dto.funcionario.FuncionarioRequestDTO;
import com.example.lenpa_backend.dto.funcionario.FuncionarioResponseDTO;
import com.example.lenpa_backend.dto.funcionario.TrocarSenhaDTO;
import com.example.lenpa_backend.mapper.FuncionarioMapper;
import com.example.lenpa_backend.model.Funcionario;
import com.example.lenpa_backend.repository.FuncionarioRepository;
import com.example.lenpa_backend.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map; // <-- Importante para o novo retorno

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private FuncionarioMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService; // <-- Injetando o gerador de token

    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um funcionário cadastrado com este e-mail.");
        }

        Funcionario novoFuncionario = mapper.toEntity(dto);
        novoFuncionario.setSenha(passwordEncoder.encode(dto.senha()));

        Funcionario funcionarioSalvo = repository.save(novoFuncionario);
        return mapper.toResponseDTO(funcionarioSalvo);
    }

    public FuncionarioResponseDTO atualizarFuncionario(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado."));

        if (!funcionarioExistente.getEmail().equals(dto.email()) && repository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está em uso por outro funcionário.");
        }

        funcionarioExistente.setNome(dto.nome());
        funcionarioExistente.setEmail(dto.email());
        funcionarioExistente.setNivelPermissao(dto.nivelPermissao());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            funcionarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Funcionario funcionarioAtualizado = repository.save(funcionarioExistente);
        return mapper.toResponseDTO(funcionarioAtualizado);
    }

    public void excluirFuncionario(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado.");
        }
        repository.deleteById(id);
    }

    // 🔥 O MÉTODO MODIFICADO AQUI 🔥
    public Map<String, Object> atualizarPerfil(String emailLogado, AtualizarPerfilDTO dto) {
        Funcionario funcionario = repository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (!funcionario.getEmail().equals(dto.email()) && repository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está em uso.");
        }

        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());

        Funcionario atualizado = repository.save(funcionario);

        // A MÁGICA: Gera um token novinho em folha com os dados recém-salvos
        String novoToken = tokenService.gerarToken(atualizado);
        FuncionarioResponseDTO dtoResposta = mapper.toResponseDTO(atualizado);

        // Devolvemos tanto os dados quanto o token em um Map
        return Map.of(
                "usuario", dtoResposta,
                "token", novoToken
        );
    }

    public void alterarSenha(String emailLogado, TrocarSenhaDTO dto) {
        Funcionario funcionario = repository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (!passwordEncoder.matches(dto.senhaAtual(), funcionario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta.");
        }

        funcionario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        repository.save(funcionario);
    }

    public List<FuncionarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}