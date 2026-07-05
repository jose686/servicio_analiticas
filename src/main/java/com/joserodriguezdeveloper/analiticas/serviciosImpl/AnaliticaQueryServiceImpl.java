package com.joserodriguezdeveloper.analiticas.serviciosImpl;

import com.joserodriguezdeveloper.analiticas.modelo.Dto.AnaliticaDashboardDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.ArticuloEngagementDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteFiltroDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteRecienteDTO;
import com.joserodriguezdeveloper.analiticas.modelo.EventoAnalitica;
import com.joserodriguezdeveloper.analiticas.repositorio.AnaliticaRepository;
import com.joserodriguezdeveloper.analiticas.servicios.AnaliticaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AnaliticaQueryServiceImpl implements AnaliticaQueryService {

    @Autowired
    private AnaliticaRepository analiticaRepository;

    public List<String> obtenerCiudadesUnicas() {
        return analiticaRepository.findDistinctCiudades();
    }

    public List<String> obtenerDispositivosUnicos() {
        return analiticaRepository.findDistinctDispositivos();
    }

    public List<String> obtenerNavegadoresUnicos() {
        return analiticaRepository.findDistinctNavegador();
    }

    public List<String> obtenerPaisUnicos() {
        return analiticaRepository.findDistinctPais();
    }

    public List<String> obtenerRegionUnicos() {
        return analiticaRepository.findDistinctRegion();
    }

    public List<String> obtenerSistemaOperativoUnicos() {
        return analiticaRepository.findDistinctSistemaOperativo();
    }

    public List<VisitanteRecienteDTO> obtenerUltimosVisitantes() {
        List<EventoAnalitica> eventos = analiticaRepository.findTop10ByOrderByFechaRegistroDesc();

        return eventos.stream().map(e -> VisitanteRecienteDTO.builder()
                        .alias(e.getAliasVisitante())
                        .evento(e.getTipoEvento().toString())
                        .ciudad(e.getCiudad())
                        .dispositivo(e.getDispositivo())
                        .fecha(e.getFechaRegistro().toString()) // O usa un formateador si prefieres
                        .build())
                .collect(Collectors.toList());
    }

    public AnaliticaDashboardDTO listarAnaliticas (){

        AnaliticaDashboardDTO analiticaDashboardDTO = new AnaliticaDashboardDTO();

        analiticaDashboardDTO.setCiudades(analiticaRepository.findDistinctCiudades());
        analiticaDashboardDTO.setDispositivos(analiticaRepository.findDistinctDispositivos());
        analiticaDashboardDTO.setNavegadores(analiticaRepository.findDistinctNavegador());
        analiticaDashboardDTO.setPais(analiticaRepository.findDistinctPais());
        analiticaDashboardDTO.setRegion(analiticaRepository.findDistinctRegion());
        analiticaDashboardDTO.setSistemaOperativo(analiticaRepository.findDistinctSistemaOperativo());

        return analiticaDashboardDTO;

    }

    // --- EL NUEVO MOTOR DE FILTRADO DIRECTO A DTO ---
    @Override
    public List<ArticuloEngagementDTO> obtenerVisitantesFiltrados(VisitanteFiltroDTO filtro) {
        System.out.println("--- DEBUG: Ejecutando filtro JPQL dinámico en el servicio ---");

        // Llamamos directamente al método del repositorio que evalúa los nulos
        // y construye el ArticuloEngagementDTO desde la base de datos.
        return analiticaRepository.buscarEngagementFiltrado(
                filtro.getCiudad(),
                filtro.getDispositivo(),
                filtro.getNavegador(),
                filtro.getPais(),
                filtro.getRegion(),
                filtro.getSistemaOperativo()
        );
    }
}