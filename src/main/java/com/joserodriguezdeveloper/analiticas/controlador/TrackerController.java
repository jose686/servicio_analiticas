package com.joserodriguezdeveloper.analiticas.controlador;


import com.joserodriguezdeveloper.analiticas.modelo.Dto.EventoTrackerDTO;
import com.joserodriguezdeveloper.analiticas.serviciosImpl.AnaliticaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    @Autowired
    private AnaliticaServiceImpl analiticaService;

    @PostMapping("/evento")
    public ResponseEntity<String> recibirEvento(
            @RequestBody EventoTrackerDTO payload,
            @RequestHeader(value = "User-Agent", defaultValue = "Desconocido") String userAgent) {

        // Pasamos el JSON del frontend y la firma del navegador al servicio
        analiticaService.procesarEvento(payload, userAgent);
        System.out.println("Evento introducido");
        return ResponseEntity.ok("Evento registrado en el Kernel");
    }

    @GetMapping("/trampa-kernel")
    public ResponseEntity<String> atraparBot(
            @RequestHeader(value = "User-Agent", defaultValue = "Bot_Sin_Firma") String userAgent) {

        // 1. Creamos el paquete del delito manualmente (ya que no hay Javascript)
        EventoTrackerDTO delito = new EventoTrackerDTO();
        delito.setUsuarioId("bot_cazado_" + System.currentTimeMillis());
        delito.setTipoEvento("TRAMPA_BOT_ACTIVADA");
        delito.setUrl("/trampa-kernel");

        // 2. Lo mandamos a tu servicio de analítica para que lo guarde en la BBDD
        analiticaService.procesarEvento(delito, userAgent);

        System.out.println("¡BOT MALICIOSO CAZADO EN LA TRAMPA!");

        // 3. Le devolvemos un 404 falso para que crea que aquí no hay nada y se vaya
        return ResponseEntity.status(404).body("Error 404: Not Found");
    }
}