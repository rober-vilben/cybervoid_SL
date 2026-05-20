package com.cybervoid.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "informes")
public class Informe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String tipo;
    private LocalDateTime generadoEn = LocalDateTime.now();

    @Column(length = 4000)
    private String contenido;

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getGeneradoEn() { return generadoEn; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}
