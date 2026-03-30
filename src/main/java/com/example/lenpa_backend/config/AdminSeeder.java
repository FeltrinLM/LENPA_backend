package com.example.lenpa_backend.config;

import com.example.lenpa_backend.model.Funcionario;
import com.example.lenpa_backend.model.NivelPermissao;
import com.example.lenpa_backend.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// O @Component avisa o Spring: "Ei, gerencie essa classe e rode ela pra mim"
@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String emailAdmin = "admin@lenpa.com.br";

        // Só cria se não existir (pra não dar erro de email duplicado caso você reinicie)
        if (repository.findByEmail(emailAdmin).isEmpty()) {

            Funcionario admin = new Funcionario();
            admin.setNome("Administrador Mestre");
            admin.setEmail(emailAdmin);
            // Aqui é a mágica: O Spring usa o BCrypt pra salvar a senha criptografada certinha
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setNivelPermissao(NivelPermissao.ADMINISTRADOR);

            repository.save(admin);

            System.out.println("==================================================");
            System.out.println("✅ USUÁRIO ADMIN TEMPORÁRIO CRIADO COM SUCESSO!");
            System.out.println("E-mail: " + emailAdmin);
            System.out.println("Senha:  123456");
            System.out.println("==================================================");
        }
    }
}