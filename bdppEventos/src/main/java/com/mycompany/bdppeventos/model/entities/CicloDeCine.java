package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad que representa un Ciclo de Cine como especialización de Evento
 * Un ciclo de cine es un evento que agrupa múltiples proyecciones de películas
 */
@Entity
@Table(name = "ciclos_cine")

public class CicloDeCine extends Evento {

    @Column(name = "charlas_posteriores", nullable = false)
    private boolean charlasPosteriores;

    // Relación uno a muchos con Proyeccion
    @OneToMany(mappedBy = "unCicloDeCine")
    private List<Proyeccion> proyecciones;

    // Constructores
    public CicloDeCine() {
        super();
    }

    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones) {
        this.setCharlasPosteriores(charlasPosteriores);
        this.setProyecciones(proyecciones);
    }

    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setCharlasPosteriores(charlasPosteriores);
        this.setProyecciones(proyecciones);
    }

    
    
    // Getters y Setters
    public boolean isCharlasPosteriores() {
        return charlasPosteriores;
    }

    public void setCharlasPosteriores(boolean charlasPosteriores) {
        this.charlasPosteriores = charlasPosteriores;
    }

    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }
}
