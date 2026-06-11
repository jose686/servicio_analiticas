package com.joserodriguezdeveloper.analiticas.modelo.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AnaliticaDashboardDTO
{
    private List<String> ciudades;
    private List<String> dispositivos;
    private List<String> navegadores;
    private List<String> pais;
    private List<String> region;
    private List<String> SistemaOperativo;

}
