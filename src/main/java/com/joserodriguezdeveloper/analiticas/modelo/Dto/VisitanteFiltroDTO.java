package com.joserodriguezdeveloper.analiticas.modelo.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitanteFiltroDTO {

    private String ciudad;
    private String dispositivo;
    private String navegador;
    private String pais;
    private String region;
    private String sistemaOperativo;

}

