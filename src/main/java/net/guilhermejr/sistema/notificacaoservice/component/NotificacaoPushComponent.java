package net.guilhermejr.sistema.notificacaoservice.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.dto.NotificacaoPushDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import net.pushover.client.*;

@Log4j2
@RequiredArgsConstructor
@Component
public class NotificacaoPushComponent {

    @Value("${sistema.pushover.token}")
    private String token;

    @Value("${sistema.pushover.user}")
    private String user;

    public void enviar (NotificacaoPushDTO notificacaoPushDTO) {

        try {
            PushoverClient pushoverClient = new PushoverRestClient();
            Status status = pushoverClient.pushMessage(PushoverMessage.builderWithApiToken(token)
                    .setUserId(user)
                    .setTitle(notificacaoPushDTO.getTitulo())
                    .setMessage(notificacaoPushDTO.getMensagem())
                    .build());
            log.info("Notificação push enviada com sucesso");
            log.info("Status: {}, request id: {}", status.getStatus(), status.getRequestId());
        } catch (PushoverException e) {
            throw new RuntimeException(e);
        }

    }

}
