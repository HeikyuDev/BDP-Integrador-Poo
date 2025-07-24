package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Clase que representa una Película en el sistema.
 * Una película puede tener múltiples proyecciones asociadas.
 * Implementa la interfaz Activable para permitir activar/desactivar la película.
 */
@Entity
@Table(name = "peliculas")
public class Pelicula implements Activable {


    /** Identificador único de la película (clave primaria, autoincremental) */
    @Id
    @Column(name = "id_pelicula", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPelicula;


    /** Título de la película (máximo 50 caracteres, no nulo) */
    @Column(name = "titulo", length = 50, nullable = false)
    private String titulo;
    

    /** Duración de la película en minutos (no nulo) */
    @Column(name = "duracion", nullable = false)
    private double duracion;


    /** Indica si la película está activa (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;
   
    // Constructores

    /**
     * Constructor por defecto. Marca la película como activa.
     */
    public Pelicula() {
        this.activo = true; // Por defecto activo
    }

    /**
     * Constructor completo para inicializar todos los campos de la película.
     * @param idPelicula Identificador único
     * @param titulo Título de la película
     * @param duracion Duración en minutos
     * @param activo Estado de la película
     * @param proyecciones Lista de proyecciones asociadas
     */
    public Pelicula(int idPelicula, String titulo, double duracion, boolean activo) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.activo = true;        
    }
    
    // Getters y Setters

    /**
     * Devuelve el identificador único de la película.
     */
    public int getIdPelicula() {
        return idPelicula;
    }

    /**
     * Devuelve el título de la película.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Asigna el título de la película, validando que no sea nulo ni exceda 50 caracteres.
     * @param titulo Título de la película
     */
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (titulo.trim().length() > 50) {
            throw new IllegalArgumentException("El título no puede exceder los 100 caracteres");
        }
        this.titulo = titulo.trim();
    }



    /**
     * Devuelve la duración de la película en minutos.
     */
    public double getDuracion() {
        return duracion;
    }

    /**
     * Asigna la duración de la película, validando que sea positiva.
     * @param duracion Duración en minutos
     */
    public void setDuracion(double duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        this.duracion = duracion;
    }
   
            
    // Métodos de la interfaz Activable

    /**
     * Activa la película (la marca como activa).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la película (la marca como inactiva).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si la película está activa.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo de la película. No permite null.
     * @param activo true para activo, false para inactivo
     */
    @Override
    public void setActivo(Boolean activo) {
        // Activo no puede ser null
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

}
