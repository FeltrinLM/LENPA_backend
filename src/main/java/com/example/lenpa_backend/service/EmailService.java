package com.example.lenpa_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Async
    public void notificarNovoAgendamento(String nomeAtividade, String nomeVisitante, Integer quantidade, String emailVisitante) {
        // ... (Seu código original continua aqui intacto)
    }

    // 🔥 NOVO MÉTODO PARA NOTIFICAR FALHAS
    @Async
    public void notificarFalhaAgendamento(String nomeAtividade, String nomeVisitante, Integer quantidade, String emailVisitante, String cidadeVisitante, String motivoErro) {

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(remetente); // Auto-envio para a equipe LENPA
        mensagem.setSubject("⚠️ Tentativa de Agendamento Falhou - LENPA");

        // Formata a string de acompanhantes (ex: "individual" ou "10 pessoas")
        String tipoAcompanhante = (quantidade != null && quantidade > 1) ? quantidade + " pessoas" : "individual";

        // Proteção contra dados nulos
        String emailContato = (emailVisitante != null && !emailVisitante.isBlank()) ? emailVisitante : "Não informado";
        String cidadeContato = (cidadeVisitante != null && !cidadeVisitante.isBlank()) ? cidadeVisitante : "Não informada";

        String texto = String.format(
                "Olá Equipe LENPA,\n\n" +
                        "%s tentou agendar sua presença na atividade '%s' junto com (%s), mas a tentativa foi bloqueada pelo sistema.\n\n" +
                        "Motivo do bloqueio: %s\n\n" +
                        "Dados do Visitante para Contato:\n" +
                        "- E-mail: %s\n" +
                        "- Cidade: %s\n\n" +
                        "Avalie se é possível abrir uma exceção ou aumentar a quantidade de vagas para esta atividade.",
                nomeVisitante, nomeAtividade, tipoAcompanhante, motivoErro, emailContato, cidadeContato
        );

        mensagem.setText(texto);

        mailSender.send(mensagem);

        System.out.println("⚠️ E-mail de alerta de falha de agendamento enviado com sucesso! (Em Background)");
    }
}