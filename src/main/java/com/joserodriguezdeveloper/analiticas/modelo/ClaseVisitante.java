package com.joserodriguezdeveloper.analiticas.modelo;

public enum ClaseVisitante {
    HUMANO_VISIBLE,  // Usuario normal (acepta cookies, JS)
    HUMANO_NINJA,    // Usuario real pero con AdBlock o privacidad alta
    BOT_SEO,         // Googlebot, Bingbot (¡VIPS! Dejar pasar)
    BOT_IA,          // ChatGPT, Claude (Para futura monetización)
    BOT_MALICIOSO    // Scrapers agresivos (A la lista negra)
}