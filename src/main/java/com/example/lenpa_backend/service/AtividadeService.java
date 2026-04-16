package com.example.lenpa_backend.service;

import com.example.lenpa_backend.dto.atividade.DadosAtualizacaoAtividade;
import com.example.lenpa_backend.dto.atividade.DadosCadastroAtividade;
import com.example.lenpa_backend.dto.atividade.DadosDetalhamentoAtividade;
import com.example.lenpa_backend.mapper.AtividadeMapper;
import com.example.lenpa_backend.model.Atividade;
import com.example.lenpa_backend.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository repository;

    @Autowired
    private AtividadeMapper mapper;

    @Transactional
    public DadosDetalhamentoAtividade cadastrar(DadosCadastroAtividade dados) {
        var atividade = mapper.toEntity(dados);
        repository.save(atividade);
        return mapper.toDetalhamentoDTO(atividade);
    }

    public Page<DadosDetalhamentoAtividade> listar(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(mapper::toDetalhamentoDTO);
    }

    public DadosDetalhamentoAtividade buscarPorId(Long id) {
        var atividade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada!"));
        return mapper.toDetalhamentoDTO(atividade);
    }

    @Transactional
    public void excluir(Long id) {
        var atividade = repository.getReferenceById(id);
        atividade.excluir();
    }

    @Transactional
    public DadosDetalhamentoAtividade atualizar(DadosAtualizacaoAtividade dados) {
        var atividade = repository.getReferenceById(dados.idAtividade());

        if (dados.nome() != null) atividade.setNome(dados.nome());
        if (dados.vagas() != null) atividade.setVagas(dados.vagas());
        if (dados.data() != null) atividade.setData(dados.data());
        if (dados.horario() != null) atividade.setHorario(dados.horario());
        if (dados.local() != null) atividade.setLocal(dados.local()); // ATUALIZAÇÃO DO NOVO CAMPO
        if (dados.descricao() != null) atividade.setDescricao(dados.descricao());
        if (dados.imagem() != null) atividade.setImagem(dados.imagem());
        if (dados.tipo() != null) atividade.setTipo(dados.tipo());

        return mapper.toDetalhamentoDTO(atividade);
    }

    public String salvarImagem(MultipartFile arquivo) {
        try {
            Path diretorioUpload = Paths.get("uploads").toAbsolutePath().normalize();

            if (!Files.exists(diretorioUpload)) {
                Files.createDirectories(diretorioUpload);
            }

            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path caminhoArquivo = diretorioUpload.resolve(nomeArquivo);

            arquivo.transferTo(caminhoArquivo.toFile());

            return "http://localhost:8080/uploads/" + nomeArquivo;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar a imagem", e);
        }
    }
}