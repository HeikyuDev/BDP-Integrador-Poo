package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.interfaces.Activable;
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
public class Pelicula implements Activable {

    @Id
    @Column(name = "id_pelicula", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPelicula;

    @Column(name = "titulo", length = 50, nullable = false)
    private String titulo;
    
    @Column(name = "duracion", nullable = false)
    private double duracion;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    // Relación uno a muchos con Proyeccion
    @OneToMany(mappedBy = "pelicula")
    private List<Proyeccion> proyecciones;

    // Constructores

    public Pelicula() {
        this.activo = true; // Por defecto activo
    }

    public Pelicula(int idPelicula, String titulo, double duracion, boolean activo, List<Proyeccion> proyecciones) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.activo = true;
        this.proyecciones = proyecciones;
    }
    
    public Pelicula(int idPelicula, String titulo, double duracion, boolean activo) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.activo = true;        
    }


    // Getters y Setters
    public int getIdPelicula() {
        return idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (titulo.trim().length() > 50) {
            throw new IllegalArgumentException("El título no puede exceder los 100 caracteres");
        }

        this.titulo = titulo.trim();
    }

    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }

      // Metodos interfaz Activable
    @Override
    public void activar() {
        this.activo = true;
    }

    @Override
    public void desactivar() {
        this.activo = false;
    }

    @Override
    public Boolean getActivo() {
        return activo;
    }

    @Override
    public void setActivo(Boolean activo) {
        // Activo no puede ser null
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

}
