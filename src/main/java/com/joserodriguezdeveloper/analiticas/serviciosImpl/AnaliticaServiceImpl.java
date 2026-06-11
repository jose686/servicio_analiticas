package com.joserodriguezdeveloper.analiticas.serviciosImpl;



import com.joserodriguezdeveloper.analiticas.modelo.ClaseVisitante;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.EventoTrackerDTO;
import com.joserodriguezdeveloper.analiticas.modelo.EventoAnalitica;
import com.joserodriguezdeveloper.analiticas.modelo.TipoEvento;
import com.joserodriguezdeveloper.analiticas.repositorio.AnaliticaRepository;
import com.joserodriguezdeveloper.analiticas.servicios.AnaliticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnaliticaServiceImpl implements AnaliticaService {

    @Autowired
    private AnaliticaRepository analiticaRepository;


    public void procesarEvento(EventoTrackerDTO dto, String userAgent) {

        ClaseVisitante claseVisitante = identificarVisitante(userAgent,dto.getUsuarioId());
        String[] infoDispositivo = extraerDetallesUserAgent(userAgent);
        TipoEvento tipo = mapearTipoEvento(dto);
        String ciudad = "Desconocida";

        String aliasGenerado = bautizarVisitante(dto.getUsuarioId(), infoDispositivo[0], ciudad);
        // --- LA CALCULADORA DEL ARQUITECTO ---
        Long metrica = null;
        Integer porcentaje = null; // Variable para el 0-100%

        if (dto.getBloqueOrden() != null && dto.getBloqueTotal() != null) {
            try {
                metrica = Long.parseLong(dto.getBloqueOrden()) + 1;
                double total = Double.parseDouble(dto.getBloqueTotal());

                // Si el artículo tiene 5 bloques y leo el 3: (3 / 5) * 100 = 60%
                if (total > 0) {
                    porcentaje = (int) Math.round((metrica / total) * 100);
                    if (porcentaje > 100) porcentaje = 100;
                }
            }
            catch (NumberFormatException e) { /* Ignorar si no es número */ }
        }


        String[] geoInfo = geolocalizarIp(dto.getIp());

        // Construir la entidad y guardarla
        EventoAnalitica evento = EventoAnalitica.builder()
                .claseVisitante(claseVisitante)
                .cookieUuid(dto.getUsuarioId() != null ? dto.getUsuarioId() : "ANONIMO")
                .aliasVisitante(aliasGenerado)
                .tipoEvento(tipo)
                .url(dto.getUrl())
                .valorMetrica(metrica)
                .porcentajeLectura(porcentaje)
                .navegador(infoDispositivo[0])
                .sistemaOperativo(infoDispositivo[1])
                .dispositivo(infoDispositivo[2])
                .ip(dto.getIp())
                .pais(geoInfo[0])
                .region(geoInfo[1])
                .ciudad(geoInfo[2])
                .build();

        analiticaRepository.save(evento);
    }
    // --- NUEVO: EL DECODIFICADOR DE USER-AGENT ---
    private String[] extraerDetallesUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return new String[]{"Desconocido", "Desconocido", "Desconocido"};
        }

        String ua = userAgent.toLowerCase();
        String navegador = "Otro";
        String os = "Otro";
        String dispositivo = "Desktop";

        // 1. Detectar Sistema Operativo y Dispositivo
        if (ua.contains("android")) {
            os = "Android";
            dispositivo = ua.contains("mobile") ? "Móvil" : "Tablet";
        } else if (ua.contains("iphone")) {
            os = "iOS";
            dispositivo = "Móvil";
        } else if (ua.contains("ipad")) {
            os = "iOS";
            dispositivo = "Tablet";
        } else if (ua.contains("windows")) {
            os = "Windows";
            dispositivo = "Desktop";
        } else if (ua.contains("mac os") || ua.contains("macintosh")) {
            os = "macOS";
            dispositivo = "Desktop";
        } else if (ua.contains("linux")) {
            os = "Linux";
            dispositivo = "Desktop";
        }

        // 2. Detectar Navegador
        if (ua.contains("edg/") || ua.contains("edge")) {
            navegador = "Edge";
        } else if (ua.contains("opr/") || ua.contains("opera")) {
            navegador = "Opera";
        } else if (ua.contains("chrome") && !ua.contains("chromium")) {
            navegador = "Chrome";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            navegador = "Safari";
        } else if (ua.contains("firefox")) {
            navegador = "Firefox";
        }

        return new String[]{navegador, os, dispositivo};
    }

    // --- EL FILTRO INTELIGENTE (ACTUALIZADO) ---
    private ClaseVisitante identificarVisitante(String userAgent, String trackerId) {
        if (userAgent == null) return ClaseVisitante.BOT_MALICIOSO; // Sin firma = Sospechoso

        String ua = userAgent.toLowerCase();

        // 1. ¿Es un Bot de IA entrenando con tu contenido?
        if (ua.contains("gptbot") || ua.contains("chatgpt") || ua.contains("claude") || ua.contains("anthropic") || ua.contains("perplexity")) {
            return ClaseVisitante.BOT_IA;
        }
        // 2. ¿Es un buscador que nos da SEO?
        else if (ua.contains("googlebot") || ua.contains("bingbot") || ua.contains("duckduckbot") || ua.contains("slurp")) {
            return ClaseVisitante.BOT_SEO;
        }
        // 3. ¿Es un bot malicioso o script automatizado (Scrapers)?
        else if (ua.contains("curl") || ua.contains("python-requests") || ua.contains("postman") || ua.contains("scrapy")) {
            return ClaseVisitante.BOT_MALICIOSO;
        }
        // 4. Si no es nada de lo anterior, es humano. Pero... ¿Qué tipo de humano?
        else {
            // ¡AQUÍ ESTÁ LA MAGIA! Si el ID empieza por "temp_", es que rechazó las cookies en el Frontend.
            if (trackerId != null && trackerId.startsWith("temp_")) {
                return ClaseVisitante.HUMANO_NINJA;
            }
            // Si tiene un UUID normal, es que aceptó el banner.
            return ClaseVisitante.HUMANO_VISIBLE;
        }
    }


    // --- TRADUCTOR DE EVENTOS (ACTUALIZADO) ---
    private TipoEvento mapearTipoEvento(EventoTrackerDTO dto) {
        String url = dto.getUrl();

        if ("BLOCK_VIEW".equals(dto.getTipoEvento())) {
            return TipoEvento.LECTURA_PROFUNDA;
        }

        if (url != null) {
            if (url.equals("/")) {
                return TipoEvento.HOME_VISITADA;
            } else if (url.contains("/blog/categoria")) { // Si tienes un endpoint para filtrar por categoría
                return TipoEvento.CATEGORIA_VISTADA;
            } else if (url.contains("/blog/")) {
                return TipoEvento.ARTICULO_VISTO;
            } else if (url.contains("trampa-kernel")) { // La URL invisible que pondremos en el frontend
                return TipoEvento.TRAMPA_BOT_ACTIVADA;
            }
        }

        return TipoEvento.PROYECTOS_VISITADO; // Por defecto
    }

    private String[] geolocalizarIp(String ip) {
        String pais = "Desconocido";
        String region = "Desconocida";
        String ciudad = "Desconocida";

        if (ip == null || ip.isEmpty()) return new String[]{pais, region, ciudad};

        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            String url = "http://ip-api.com/json/" + ip + "?lang=es";

            java.util.Map<String, Object> respuesta = restTemplate.getForObject(url, java.util.Map.class);

            if (respuesta != null && "success".equals(respuesta.get("status"))) {
                pais = (String) respuesta.get("country");
                region = (String) respuesta.get("regionName");
                ciudad = (String) respuesta.get("city");
            }
        } catch (Exception e) {
            System.err.println("[GEO-RADAR] Fallo al localizar IP: " + ip + " - " + e.getMessage());
        }
        return new String[]{pais, region, ciudad};
    }
    // --- EL BAUTIZO DIGITAL DEL ARQUITECTO ---
    private String bautizarVisitante(String cookieUuid, String navegador, String ciudad) {
        if (cookieUuid == null) return "Entidad Desconocida";

        // 1. Diccionario de Nombres de Pila
        String[] nombres = {"El Viajero", "La Sombra", "El Arquitecto", "El Centinela",
                "El Erudito", "El Lobo", "El Fantasma", "El Herrero",
                "El Explorador", "El Guardián"};

        // 2. Magia matemática: Convertimos el UUID en un número fijo
        int indiceNombre = Math.abs(cookieUuid.hashCode()) % nombres.length;
        String nombrePila = nombres[indiceNombre];

        // 3. Construimos el Apellido del Oficio (Navegador)
        String oficio = (navegador != null && !navegador.isEmpty() && !navegador.equals("Desconocido"))
                ? " de " + navegador
                : " sin Rastro";

        // 4. Construimos el Apellido del Señorío (Ciudad)
        String senorio = (ciudad != null && !ciudad.isEmpty() && !ciudad.equals("Desconocida"))
                ? " nacido en " + ciudad
                : " de Tierras Lejanas";

        // 5. ¡El nacimiento del Alias!
        return nombrePila + oficio + senorio;
    }
}