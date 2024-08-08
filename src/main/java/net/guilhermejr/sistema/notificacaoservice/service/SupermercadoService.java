package net.guilhermejr.sistema.notificacaoservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.api.request.GenericoRequest;
import net.guilhermejr.sistema.notificacaoservice.api.request.NFERequest;
import net.guilhermejr.sistema.notificacaoservice.component.NotificacaoPushComponent;
import net.guilhermejr.sistema.notificacaoservice.dto.NotificacaoPushDTO;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class SupermercadoService {
    
    private final NotificacaoPushComponent notificacaoPushComponent;

    public void enviarNotificacaoNFE(NFERequest nfeRequest) {

        String titulo = "NFE inserida na base de dados";
        String mensagem = "A compra realizado no "+ nfeRequest.getNome() +" em "+ nfeRequest.getData() +" no valor de "+ nfeRequest.getTotal() +" foi inserida na base de dados.";

        NotificacaoPushDTO notificacaoPushDTO = NotificacaoPushDTO
                .builder()
                .titulo(titulo)
                .mensagem(mensagem)
                .build();
        notificacaoPushComponent.enviar(notificacaoPushDTO);

        log.info("Mensagem enviada com sucesso");

    }

    public void enviarNotificacaoGenerica(GenericoRequest genericoRequest) {

        NotificacaoPushDTO notificacaoPushDTO = NotificacaoPushDTO
                .builder()
                .titulo(genericoRequest.getTitulo())
                .mensagem(genericoRequest.getMensagem())
                .build();
        notificacaoPushComponent.enviar(notificacaoPushDTO);

    }
}
