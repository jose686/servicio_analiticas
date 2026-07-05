package com.joserodriguezdeveloper.analiticas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joserodriguezdeveloper.analiticas.modelo.Dto.EventoTrackerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest

@Transactional
@Testcontainers
public class AnaliticasIntegrationTest {

    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void deberiaRegistrarEventoYDevolver200() throws Exception {
        EventoTrackerDTO evento = new EventoTrackerDTO();
        evento.setUsuarioId("user123");
        evento.setTipoEvento("PAGE_VIEW");
        evento.setUrl("/home");

        mockMvc.perform(post("/api/tracker/evento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento registrado en el Kernel"));
    }

    @Test
    @WithMockUser
    void deberiaAtraparBotYDevolver404() throws Exception {
        mockMvc.perform(get("/api/tracker/trampa-kernel")
                .header("User-Agent", "BotMalicioso/1.0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error 404: Not Found"));
    }

    @Test
    @WithMockUser
    void deberiaListarAnaliticasYDevolver200() throws Exception {
        mockMvc.perform(get("/api/analitica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void deberiaDevolverUltimosVisitantesCon200() throws Exception {
        mockMvc.perform(get("/api/analitica/visitantes-recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void deberiaFallarCon400AlEnviarCuerpoVacioAEvento() throws Exception {
        mockMvc.perform(post("/api/tracker/evento")
                .contentType(MediaType.APPLICATION_JSON))
                // Falla porque requiere un RequestBody
                .andExpect(status().isBadRequest());
    }
}
