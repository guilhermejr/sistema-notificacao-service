package net.guilhermejr.sistema.notificacaoservice.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailDTO  implements Serializable {

    private String destinatario;
    private String assunto;
    private String texto;

}
