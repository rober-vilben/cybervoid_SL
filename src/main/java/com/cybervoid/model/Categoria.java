package com.cybervoid.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String nombre;

    @NotBlank
    @Column(unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    private CategoriaTipo tipo;

    protected Categoria() {
    }

    public Categoria(String nombre, String slug, CategoriaTipo tipo) {
        this.nombre = nombre;
        this.slug = slug;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public CategoriaTipo getTipo() { return tipo; }
    public void setTipo(CategoriaTipo tipo) { this.tipo = tipo; }
}
