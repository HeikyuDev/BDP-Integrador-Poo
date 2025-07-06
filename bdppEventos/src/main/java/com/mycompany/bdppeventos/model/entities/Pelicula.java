package com.mycompany.bdppeventos.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 * Entidad que representa una Película
 * Una película puede tener múltiples proyecciones
 */
@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @Column(name = "id_pelicula", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPelicula;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    // Relación uno a muchos con Proyeccion
    @OneToMany(mappedBy = "pelicula")
    private List<Proyeccion> proyecciones;

    // Constructores

    public Pelicula() {
        this.activo = true; // Por defecto activo
    }

    // Constructor para crear una película nueva sin proyecciones
    public Pelicula(int idPelicula, String titulo, boolean activo) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.activo = activo;
    }

    // Constructor completo para crear una película con proyecciones
    public Pelicula(int idPelicula, String titulo, boolean activo, List<Proyeccion> proyecciones) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.activo = activo;
        this.proyecciones = proyecciones;
    }

    // Getters y Setters

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }

}
