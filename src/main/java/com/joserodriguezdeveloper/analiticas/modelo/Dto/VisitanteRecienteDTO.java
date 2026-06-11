package com.joserodriguezdeveloper.analiticas.modelo.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisitanteRecienteDTO {
    private String alias;
    private String evento;
    private String ciudad;
    private String dispositivo;
    private String fecha;
}
