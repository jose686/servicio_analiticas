package com.joserodriguezdeveloper.analiticas.modelo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloEngagementDTO {
    private String url;
    private String aliasVisitante;
    private Integer porcentajeLectura;
    private String ciudad;
    private String dispositivo;
}