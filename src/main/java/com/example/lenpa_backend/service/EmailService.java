package com.example.lenpa_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    // Atualizado para receber os dados completos da reserva confirmada
    public void notificarNovoAgendamento(String nomeAtividade, String nomeVisitante, Integer quantidade, String emailVisitante) {

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(remetente); // Auto-envio para a caixa do LENPA
        mensagem.setSubject("✅ Novo Agendamento Confirmado - LENPA");

        // Corpo do e-mail reformulado para cenário automatizado
        String emailContato = (emailVisitante != null && !emailVisitante.isBlank()) ? emailVisitante : "Não informado (Cadastrado via Painel)";

        String texto = String.format(
                "Olá Equipe LENPA,\n\n" +
                        "Um novo agendamento foi CONFIRMADO automaticamente no sistema!\n\n" +
                        "Detalhes da Reserva:\n" +
                        "- Atividade: %s\n" +
                        "- Visitante Responsável: %s\n" +
                        "- Vagas Reservadas: %d\n" +
                        "- E-mail de Contato: %s\n\n" +
                        "As vagas já foram abatidas do limite da atividade. Nenhuma ação manual de aprovação é necessária.",
                nomeAtividade, nomeVisitante, quantidade, emailContato
        );

        mensagem.setText(texto);

        mailSender.send(mensagem);

        System.out.println("✅ E-mail de confirmação automática enviado com sucesso!");
    }
}