package net.guilhermejr.sistema.notificacaoservice.api.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecuperarSenhaRequest implements Serializable {

    private String nome;
    private String email;
    private String senha;

}
