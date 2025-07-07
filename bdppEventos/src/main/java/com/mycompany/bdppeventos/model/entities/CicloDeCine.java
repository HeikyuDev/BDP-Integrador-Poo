package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

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
    @OneToMany(mappedBy = "cicloDeCine")
    private List<Proyeccion> proyecciones;

    // Constructor por defecto
    public CicloDeCine() {
        super();
    }

    // Constructor completo para crear ciclos de cine existentes

    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones) {
        this.charlasPosteriores = charlasPosteriores;
        this.proyecciones = proyecciones;
    }

    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones, int idEvento, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento unEstadoEvento, boolean esPago, double montoInscripcion, boolean activo, List<Participacion> unaListaParticipacion) {
        super(idEvento, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, unEstadoEvento, esPago, montoInscripcion, activo, unaListaParticipacion);
        this.charlasPosteriores = charlasPosteriores;
        this.proyecciones = proyecciones;
    }
    
    
    // Constructor simplificado para crear ciclos de cine nuevos
    

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
