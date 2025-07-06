package com.mycompany.bdppeventos.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad que representa una Proyección
 * Una proyección pertenece a un CicloDeCine y está asociada a una Película
 */
@Entity
@Table(name = "proyecciones")
public class Proyeccion {

    @Id
    @Column(name = "id_proyeccion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProyeccion;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    // Relación muchos a uno con CicloDeCine
    @ManyToOne
    @JoinColumn(name = "id_ciclo_cine", nullable = false)
    private CicloDeCine cicloDeCine;

    // Relación muchos a uno con Pelicula
    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    // Constructor por defecto
    public Proyeccion() {
        this.activo = true; // Por defecto activo
    }

    public Proyeccion(int idProyeccion, boolean activo, CicloDeCine cicloDeCine, Pelicula pelicula) {
        this.idProyeccion = idProyeccion;
        this.activo = activo;
        this.cicloDeCine = cicloDeCine;
        this.pelicula = pelicula;
    }

    // Getters y Setters

    public int getIdProyeccion() {
        return idProyeccion;
    }

    public void setIdProyeccion(int idProyeccion) {
        this.idProyeccion = idProyeccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
