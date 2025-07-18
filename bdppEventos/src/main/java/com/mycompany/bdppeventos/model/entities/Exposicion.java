package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Clase que representa una Exposición, subclase de Evento.
 * Agrega el atributo unTipoArte para especificar el tipo de arte de la exposición.
 */
@Entity
@Table(name = "exposiciones")
public final class Exposicion extends Evento {


    /** Tipo de arte asociado a la exposición (relación muchos a uno) */
    @ManyToOne
    @JoinColumn(name = "id_tipo_arte")
    TipoDeArte unTipoArte;


    // Constructores

    /**
     * Constructor por defecto. Inicializa la exposición como activa.
     */
    public Exposicion() {
        super();
    }

    /**
     * Constructor que recibe solo el tipo de arte.
     * @param unTipoArte Tipo de arte de la exposición
     */
    public Exposicion(TipoDeArte unTipoArte) {
        super();
        this.setUnTipoArte(unTipoArte);
    }

    /**
     * Constructor completo para inicializar todos los campos de la exposición.
     * @param unTipoArte Tipo de arte de la exposición
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
    public Exposicion(TipoDeArte unTipoArte, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setUnTipoArte(unTipoArte);
    }

    
    
    // Getters y Setters

    /**
     * Devuelve el tipo de arte asociado a la exposición.
     */
    public TipoDeArte getUnTipoArte() {
        return unTipoArte;
    }

    /**
     * Asigna el tipo de arte a la exposición, validando que no sea null.
     * @param unTipoArte Tipo de arte a asignar
     */
    public void setUnTipoArte(TipoDeArte unTipoArte) {
        if(unTipoArte == null) {
            throw new IllegalArgumentException("unTipoArte no puede ser null");
        } else {
            this.unTipoArte = unTipoArte;
        }
    }
}
