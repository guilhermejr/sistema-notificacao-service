package net.guilhermejr.sistema.notificacaoservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.api.request.RecuperarSenhaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class AutenticacaoService {

    private final SqsTemplate sqsTemplate;

    @Value("${cloud.aws.fila.esqueci-minha-senha.url}")
    private String esqueciMinhaSenhaUrl;

    public void enviarRecuperarSenhaFila(RecuperarSenhaRequest recuperarSenhaRequest) {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(recuperarSenhaRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        sqsTemplate.send(esqueciMinhaSenhaUrl, json);
        log.info("Envio de nova senha gravado na fila com sucesso");

    }

}
