package net.guilhermejr.sistema.notificacaoservice.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.notificacaoservice.api.request.GenericoRequest;
import net.guilhermejr.sistema.notificacaoservice.api.request.NFERequest;
import net.guilhermejr.sistema.notificacaoservice.service.SupermercadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/supermercado")
public class SupermercadoController {

    private final SupermercadoService supermercadoService;

    @PostMapping("/enviar-notificacao-nfe")
    public ResponseEntity<Void> enviarNotificacaoNFE(@RequestBody NFERequest nfeRequest) {

        supermercadoService.enviarNotificacaoNFE(nfeRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/enviar-notificacao-generica")
    public ResponseEntity<Void> enviarNotificacaoGenerica(@RequestBody GenericoRequest genericoRequest) {

        supermercadoService.enviarNotificacaoGenerica(genericoRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
