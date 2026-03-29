package com.example.lenpa_backend.mapper;

import com.example.lenpa_backend.dto.FuncionarioRequestDTO;
import com.example.lenpa_backend.dto.FuncionarioResponseDTO;
import com.example.lenpa_backend.model.Funcionario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// O componentModel = "spring" permite que você use o @Autowired pra chamar o mapper depois
@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    // 1. Converte o que veio do front para a nossa Entidade
    // Ignoramos o ID (banco que gera) e a Senha (vamos criptografar no Service antes de setar)
    @Mapping(target = "idFuncionario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    Funcionario toEntity(FuncionarioRequestDTO requestDTO);

    // 2. Converte a Entidade salva no banco para devolver pro front
    // Como os nomes dos campos são idênticos, não precisa de @Mapping, o MapStruct se vira!
    FuncionarioResponseDTO toResponseDTO(Funcionario funcionario);

}