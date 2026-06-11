package com.joserodriguezdeveloper.analiticas.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "analitica_eventos")
public class EventoAnalitica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTIDAD ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClaseVisitante claseVisitante;

    @Column(nullable = false, length = 50)
    private String cookieUuid; // La matrícula del navegador (Generada en Django/Frontend)

    @Column(nullable = true)
    private Long usuarioId; // Se rellena solo si hace Login (Para cruzar datos después)

    // --- QUÉ HA HECHO ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEvento tipoEvento;

    @Column(nullable = true)
    private Long referenciaId; // ID del Post, Producto, etc. (Polimórfico)


    private String url;

    @Column(nullable = true)
    private Long valorMetrica;
    @Column(nullable = true)
    private Integer porcentajeLectura;
    // --- AUTOAUDITORÍA ---
    @CreationTimestamp
    private LocalDateTime fechaRegistro;
    @Column(length = 50)
    private String navegador;
    @Column(length = 50)
    private String sistemaOperativo;
    @Column(nullable = true, length = 150)
    private String aliasVisitante;
    @Column(length = 50)
    private String dispositivo;
    @Column(length = 45)
    private String ip;
    @Column(length = 100)
    private String pais;
    @Column(length = 100)
    private String region;
    @Column(length = 100)
    private String ciudad;


}
