package com.joserodriguezdeveloper.analiticas.modelo.Dto;


import lombok.Data;

@Data
public class EventoTrackerDTO {
    private String usuarioId; // El UUID (cookie o temporal)
    private String tipoEvento; // PAGE_VIEW, BLOCK_VIEW, etc.
    private String url;
    private String titulo;
    private String bloqueTotal;
    private String articuloSlug;
    private String bloqueOrden;
    private String bloqueTipo;
    private String ip;
}