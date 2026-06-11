package com.joserodriguezdeveloper.analiticas.modelo;

public enum TipoEvento {
    HOME_VISITADA,
    ARTICULO_VISTO,
    PROYECTOS_VISITADO,
    LECTURA_PROFUNDA, // Cuando abandonan, registramos hasta qué bloque llegaron
    CATEGORIA_VISTADA,
    TRAMPA_BOT_ACTIVADA // ¡Cazado en el Honeypot!
}