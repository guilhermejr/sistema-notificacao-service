package net.guilhermejr.sistema.notificacaoservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.component.EmailComponent;
import net.guilhermejr.sistema.notificacaoservice.dto.EmailDTO;
import net.guilhermejr.sistema.notificacaoservice.dto.EsqueciMinhaSenhaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class RecuperarSenhaConsumer {

    @Value("${sistema.url}")
    private String url;

    private final EmailComponent emailComponent;

    @SqsListener("${cloud.aws.fila.esqueci-minha-senha.url}")
    public void enviarLink(String payload) {

        ObjectMapper mapper = new ObjectMapper();
        EsqueciMinhaSenhaDTO esqueciMinhaSenhaDTO = null;

        try {

            esqueciMinhaSenhaDTO = mapper.readValue(payload, EsqueciMinhaSenhaDTO.class);

            String texto = "Olá " + esqueciMinhaSenhaDTO.getNome() + "\n\nPara criar uma nova senha click no link a seguir: "+ url +"/autenticacao-service/trocar-senha/"+ esqueciMinhaSenhaDTO.getHash();
            EmailDTO emailDTO = EmailDTO
                    .builder()
                    .destinatario(esqueciMinhaSenhaDTO.getEmail())
                    .assunto("Criação de nova senha")
                    .texto(texto)
                    .build();
            emailComponent.enviar(emailDTO);
            log.info("Senha com link de recuperação enviada com sucesso para {}", emailDTO.getDestinatario());
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter String em JSON - {}", e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao processar fila - {}", e.getMessage());
        }

    }

}
