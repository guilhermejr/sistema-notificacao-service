package net.guilhermejr.sistema.notificacaoservice.api.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NFERequest {

    private String url;
    private Boolean retornou;
    private String mensagem;
    private String data;
    private String total;
    private String cnpj;
    private String ie;
    private String nome;
    private String chaveDeAcesso;
    private String informacoesComplementares;

}
