package net.guilhermejr.sistema.notificacaoservice.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificacaoPushDTO implements Serializable {

    private String titulo;
    private String mensagem;

}
