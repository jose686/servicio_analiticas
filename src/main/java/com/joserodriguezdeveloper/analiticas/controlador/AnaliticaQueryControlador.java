package com.joserodriguezdeveloper.analiticas.controlador;



import com.joserodriguezdeveloper.analiticas.modelo.Dto.AnaliticaDashboardDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.ArticuloEngagementDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteFiltroDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteRecienteDTO;
import com.joserodriguezdeveloper.analiticas.serviciosImpl.AnaliticaQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analitica")
public class AnaliticaQueryControlador {


    @Autowired
    private AnaliticaQueryServiceImpl analiticaQueryService;



    @GetMapping
    public ResponseEntity<AnaliticaDashboardDTO> listarAnaliticas() {

        AnaliticaDashboardDTO datos = analiticaQueryService.listarAnaliticas();

        return ResponseEntity.ok(datos);
    }

    @GetMapping("/visitantes-recientes")
    public ResponseEntity<List<VisitanteRecienteDTO>> ultimosVisitanes() {
        System.out.println("10 ultimos visitantes");
        return ResponseEntity.ok(analiticaQueryService.obtenerUltimosVisitantes());
    }


    @GetMapping("/visitantes-filtrados")
    public ResponseEntity<List<ArticuloEngagementDTO>> obtenerVisitantesFiltrados(VisitanteFiltroDTO filtro) {
        System.out.println("visitantes filtrados");
        return ResponseEntity.ok(analiticaQueryService.obtenerVisitantesFiltrados(filtro));
    }
}
