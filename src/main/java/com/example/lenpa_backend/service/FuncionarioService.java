package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.funcionario.AtualizarPerfilDTO;
import com.example.lenpa_backend.dto.funcionario.FuncionarioRequestDTO;
import com.example.lenpa_backend.dto.funcionario.FuncionarioResponseDTO;
import com.example.lenpa_backend.dto.funcionario.TrocarSenhaDTO;
import com.example.lenpa_backend.mapper.FuncionarioMapper;
import com.example.lenpa_backend.model.Funcionario;
import com.example.lenpa_backend.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private FuncionarioMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CRIAR
    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Já existe um funcionário cadastrado com este e-mail.");
        }

        Funcionario novoFuncionario = mapper.toEntity(dto);
        novoFuncionario.setSenha(passwordEncoder.encode(dto.senha()));

        Funcionario funcionarioSalvo = repository.save(novoFuncionario);
        return mapper.toResponseDTO(funcionarioSalvo);
    }

    // ATUALIZAR
    public FuncionarioResponseDTO atualizarFuncionario(Long id, FuncionarioRequestDTO dto) {
        // Busca o cara no banco primeiro
        Funcionario funcionarioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        // Regra: Se ele estiver tentando mudar o email para um que já existe em outra conta, bloqueia
        if (!funcionarioExistente.getEmail().equals(dto.email()) && repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso por outro funcionário.");
        }

        // Atualiza os dados manuais
        funcionarioExistente.setNome(dto.nome());
        funcionarioExistente.setEmail(dto.email());
        funcionarioExistente.setNivelPermissao(dto.nivelPermissao());

        // Só atualiza a senha se ele mandou uma nova
        if (dto.senha() != null && !dto.senha().isBlank()) {
            funcionarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Funcionario funcionarioAtualizado = repository.save(funcionarioExistente);
        return mapper.toResponseDTO(funcionarioAtualizado);
    }

    // EXCLUIR
    public void excluirFuncionario(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado.");
        }
        repository.deleteById(id);
    }
    // No FuncionarioService.java

    public FuncionarioResponseDTO atualizarPerfil(String emailLogado, AtualizarPerfilDTO dto) {
        Funcionario funcionario = repository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Valida se o novo email já existe em OUTRA conta
        if (!funcionario.getEmail().equals(dto.email()) && repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso.");
        }

        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());

        return mapper.toResponseDTO(repository.save(funcionario));
    }

    public void alterarSenha(String emailLogado, TrocarSenhaDTO dto) {
        Funcionario funcionario = repository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // O matches do PasswordEncoder é essencial aqui!
        if (!passwordEncoder.matches(dto.senhaAtual(), funcionario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        funcionario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        repository.save(funcionario);
    }
}