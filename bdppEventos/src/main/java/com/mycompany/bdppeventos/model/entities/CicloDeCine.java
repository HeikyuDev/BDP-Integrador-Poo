package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa un Ciclo de Cine, subclase de Evento.
 * Un ciclo de cine es un evento que agrupa múltiples proyecciones de películas y puede incluir charlas posteriores.
 */
@Entity
@Table(name = "ciclos_cine")

public final class CicloDeCine extends Evento {


    /** Indica si el ciclo de cine incluye charlas posteriores a las proyecciones */
    @Column(name = "charlas_posteriores", nullable = false)
    private boolean charlasPosteriores;


    /** Lista de proyecciones asociadas al ciclo de cine (relación uno a muchos) */
    @OneToMany(mappedBy = "unCicloDeCine")
    private List<Proyeccion> proyecciones;


    // Constructores

    /**
     * Constructor por defecto. Inicializa el ciclo de cine como activo.
     */
    public CicloDeCine() {
        super();
    }

    /**
     * Constructor que recibe si hay charlas posteriores y la lista de proyecciones.
     * @param charlasPosteriores true si hay charlas posteriores, false si no
     * @param proyecciones Lista de proyecciones del ciclo
     */
    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones) {
        this.setCharlasPosteriores(charlasPosteriores);
        this.setProyecciones(proyecciones);
    }

    /**
     * Constructor completo para inicializar todos los campos del ciclo de cine.
     * @param charlasPosteriores true si hay charlas posteriores
     * @param proyecciones Lista de proyecciones
     * @param id Identificador único del evento
     * @param nombre Nombre del evento
     * @param fechaInicio Fecha de inicio
     * @param duracionEstimada Duración estimada
     * @param tieneCupo Si tiene cupo máximo
     * @param capacidadMaxima Capacidad máxima
     * @param tieneInscripcion Si requiere inscripción
     * @param ubicacion Ubicación física
     * @param estado Estado del evento
     * @param unaListaParticipacion Lista de participaciones
     */
    public CicloDeCine(boolean charlasPosteriores, List<Proyeccion> proyecciones, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setCharlasPosteriores(charlasPosteriores);
        this.setProyecciones(proyecciones);
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

    /**
     * Devuelve la lista de proyecciones asociadas al ciclo de cine.
     */
    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    /**
     * Asigna la lista de proyecciones al ciclo de cine.
     * @param proyecciones Lista de proyecciones
     */
    public void setProyecciones(List<Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }
}
