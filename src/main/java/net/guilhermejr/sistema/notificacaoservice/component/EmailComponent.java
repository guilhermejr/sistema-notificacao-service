package net.guilhermejr.sistema.notificacaoservice.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.dto.EmailDTO;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class EmailComponent {

    private final JavaMailSender mailSender;

    public void enviar(EmailDTO emailDTO) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDTO.getDestinatario());
            message.setSubject(emailDTO.getAssunto());
            message.setText(emailDTO.getTexto());
            mailSender.send(message);
            log.info("E-mail enviado com sucesso para {}", emailDTO.getDestinatario());
        } catch (MailException e) {
            log.error("Erro ao enviar e-mail para {} - {}", emailDTO.getDestinatario(), e.getMessage());
        }

    }
}
