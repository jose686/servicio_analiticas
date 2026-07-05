package com.joserodriguezdeveloper.analiticas.servicios;

import com.joserodriguezdeveloper.analiticas.modelo.Dto.ArticuloEngagementDTO;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteFiltroDTO;

import java.util.List;

public interface AnaliticaQueryService {
    // --- EL NUEVO MOTOR DE FILTRADO DIRECTO A DTO ---
    List<ArticuloEngagementDTO> obtenerVisitantesFiltrados(VisitanteFiltroDTO filtro);
}
