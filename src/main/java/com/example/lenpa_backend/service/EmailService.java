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

    // METODO PARA NOTIFICAR NOVO AGENDAMENTO COM SUCESSO
    @Async
    public void notificarNovoAgendamento(String nomeAtividade, String dataAtividade, String horarioAtividade, String localAtividade, String nomeVisitante, Integer quantidade, String emailVisitante) {

        // Se o visitante nao tem e-mail (ex: agendamento anonimo ou presencial sem e-mail), aborta o envio silenciosamente
        if (emailVisitante == null || emailVisitante.isBlank()) {
            return;
        }

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(emailVisitante);
        mensagem.setSubject("Reserva Confirmada: " + nomeAtividade + " - LENPA");

        // Tratamento para exibir a quantidade de pessoas formatada
        String tipoAcompanhante;
        if (quantidade != null && quantidade > 1) {
            tipoAcompanhante = quantidade + " pessoas (incluindo você)";
        } else {
            tipoAcompanhante = "Visitante Individual";
            quantidade = 1; // Garante que nao fique nulo na exibicao
        }

        String texto = String.format(
                "Olá, %s!\n\n" +
                        "Sua reserva foi confirmada com sucesso pelo nosso sistema. Abaixo estão os detalhes da sua visita:\n\n" +
                        "RESUMO DO AGENDAMENTO:\n" +
                        "- Atividade: %s\n" +
                        "- Data: %s\n" +
                        "- Horário: %s\n" +
                        "- Local: %s\n" +
                        "- Quantidade de vagas reservadas: %d\n" +
                        "- Modalidade: %s\n\n" +
                        "Aguardamos a sua presença para explorarmos juntos a nossa natureza!\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe LENPA",
                nomeVisitante, nomeAtividade, dataAtividade, horarioAtividade, localAtividade, quantidade, tipoAcompanhante
        );

        mensagem.setText(texto);

        mailSender.send(mensagem);

        System.out.println("E-mail de confirmacao de agendamento enviado com sucesso para: " + emailVisitante);
    }

    // METODO PARA NOTIFICAR FALHAS
    @Async
    public void notificarFalhaAgendamento(String nomeAtividade, String nomeVisitante, Integer quantidade, String emailVisitante, String cidadeVisitante, String motivoErro) {

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(remetente); // Auto-envio para a equipe LENPA
        mensagem.setSubject("Tentativa de Agendamento Falhou - LENPA");

        // Formata a string de acompanhantes (ex: "individual" ou "10 pessoas")
        String tipoAcompanhante = (quantidade != null && quantidade > 1) ? quantidade + " pessoas" : "individual";

        // Protecao contra dados nulos
        String emailContato = (emailVisitante != null && !emailVisitante.isBlank()) ? emailVisitante : "Nao informado";
        String cidadeContato = (cidadeVisitante != null && !cidadeVisitante.isBlank()) ? cidadeVisitante : "Nao informada";

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

        System.out.println("E-mail de alerta de falha de agendamento enviado com sucesso! (Em Background)");
    }
}