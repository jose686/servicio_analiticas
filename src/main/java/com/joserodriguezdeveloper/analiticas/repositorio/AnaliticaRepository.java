package com.joserodriguezdeveloper.analiticas.repositorio;


import com.joserodriguezdeveloper.analiticas.modelo.Dto.ArticuloEngagementDTO;
import com.joserodriguezdeveloper.analiticas.modelo.EventoAnalitica;
import com.joserodriguezdeveloper.analiticas.modelo.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnaliticaRepository extends JpaRepository<EventoAnalitica, Long>, JpaSpecificationExecutor<EventoAnalitica> {
    long countByTipoEvento(TipoEvento tipo);
    List<EventoAnalitica> findByCookieUuidOrderByFechaRegistroDesc(String uuid);

    @Query("SELECT DISTINCT e.ciudad FROM EventoAnalitica e WHERE e.ciudad IS NOT NULL")
    List<String> findDistinctCiudades();
    @Query("SELECT DISTINCT e.navegador FROM EventoAnalitica e WHERE e.navegador IS NOT NULL")
    List<String> findDistinctNavegador();

    @Query("SELECT DISTINCT e.dispositivo FROM EventoAnalitica e WHERE e.dispositivo IS NOT NULL")
    List<String> findDistinctDispositivos();

    @Query("SELECT DISTINCT e.pais FROM EventoAnalitica e WHERE e.pais IS NOT NULL")
    List<String> findDistinctPais();
    @Query("SELECT DISTINCT e.region FROM EventoAnalitica e WHERE e.region IS NOT NULL")
    List<String> findDistinctRegion();
    @Query("SELECT DISTINCT e.sistemaOperativo FROM EventoAnalitica e WHERE e.sistemaOperativo IS NOT NULL")
    List<String> findDistinctSistemaOperativo();
/*
    @Query("SELECT new com.example.demo.modelo.Dto.ArticuloEngagementDTO(e.url, e.aliasVisitante, e.porcentajeLectura, e.ciudad, e.dispositivo) " +
            "FROM EventoAnalitica e WHERE e.tipoEvento = 'VISTA_PAGINA' ORDER BY e.fechaRegistro DESC")
              List<ArticuloEngagementDTO> obtenerEngagementReciente();
        */


    List<EventoAnalitica> findTop10ByOrderByFechaRegistroDesc();
}