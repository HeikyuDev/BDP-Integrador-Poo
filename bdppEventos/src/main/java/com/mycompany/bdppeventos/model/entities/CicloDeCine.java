package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa un Ciclo de Cine, subclase de Evento.
 * Un ciclo de cine es un evento que agrupa múltiples proyecciones de películas y puede incluir charlas posteriores.
 */
@Entity
@Table(name = "ciclos_cine")

public final class CicloDeCine extends Evento {


    /** Indica si el ciclo de cine incluye charlas posteriores a las proyecciones */
    @Column(name = "charlas_posteriores", nullable = true)
    private boolean charlasPosteriores;


    /** Lista de proyecciones asociadas al ciclo de cine (relación uno a muchos) */
    @ManyToOne    
    @JoinColumn(name = "id_proyeccion", nullable = true)
    private Proyeccion unaProyeccion;


    // Constructores
    
    public CicloDeCine() {
        super();
    }

    public CicloDeCine(boolean charlasPosteriores, Proyeccion unaProyeccion) {
        super();
        this.charlasPosteriores = charlasPosteriores;
        this.unaProyeccion = unaProyeccion;
    }

    
    public CicloDeCine(boolean charlasPosteriores, Proyeccion unaProyeccion, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {    
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.charlasPosteriores = charlasPosteriores;
        this.unaProyeccion = unaProyeccion;
    }

               
    // Getters y Setters

    /**
     * Devuelve si el ciclo de cine incluye charlas posteriores.
     */
    public boolean isCharlasPosteriores() {
        return charlasPosteriores;
    }

    /**
     * Asigna si el ciclo de cine incluye charlas posteriores.
     * @param charlasPosteriores true si hay charlas posteriores
     */
    public void setCharlasPosteriores(boolean charlasPosteriores) {
        this.charlasPosteriores = charlasPosteriores;
    }

    public Proyeccion getUnaProyeccion() {
        return unaProyeccion;
    }

    public void setUnaProyeccion(Proyeccion unaProyeccion) {
        this.unaProyeccion = unaProyeccion;
    }

    
}
