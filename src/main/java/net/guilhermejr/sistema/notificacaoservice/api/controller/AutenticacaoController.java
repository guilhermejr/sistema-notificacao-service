package net.guilhermejr.sistema.notificacaoservice.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.api.request.RecuperarSenhaRequest;
import net.guilhermejr.sistema.notificacaoservice.service.AutenticacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/enviar-recuperar-senha-fila")
    public ResponseEntity<Void> enviarRecuperarSenhaFila(@RequestBody RecuperarSenhaRequest recuperarSenhaRequest) {

        autenticacaoService.enviarRecuperarSenhaFila(recuperarSenhaRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
