package net.guilhermejr.sistema.notificacaoservice.api.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GenericoRequest {

    private String titulo;
    private String mensagem;

}
