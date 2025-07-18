
/**
 * Clase que representa un Taller, subclase de Evento.
 * Agrega el atributo esPresencial para indicar si el taller es presencial o virtual.
 */
package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "talleres")
public final class Taller extends Evento {


    /** Indica si el taller es presencial (true) o virtual (false) */
    @Column(name = "es_presencial", nullable = false)
    private boolean esPresencial;
    

    /**
     * Constructor por defecto. Inicializa el taller como activo.
     */
    public Taller() {
        super();
    }


    /**
     * Constructor que recibe solo si el taller es presencial.
     * @param esPresencial true si es presencial, false si es virtual
     */
    public Taller(boolean esPresencial) {
        super();
        this.setEsPresencial(esPresencial);
    }


    /**
     * Constructor completo para inicializar todos los campos del taller.
     * @param esPresencial true si es presencial, false si es virtual
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
    public Taller(
            boolean esPresencial,
            int id,
            String nombre,
            LocalDate fechaInicio,
            int duracionEstimada,
            boolean tieneCupo,
            int capacidadMaxima,
            boolean tieneInscripcion,
            String ubicacion,
            EstadoEvento estado,
            List<Participacion> unaListaParticipacion
    ) {
        super(
                id,
                nombre,
                fechaInicio,
                duracionEstimada,
                tieneCupo,
                capacidadMaxima,
                tieneInscripcion,
                ubicacion,
                estado,
                unaListaParticipacion
        );
        this.setEsPresencial(esPresencial);
    }


    // Getters y Setters

    /**
     * Devuelve si el taller es presencial.
     */
    public boolean isEsPresencial() {
        return esPresencial;
    }

    /**
     * Asigna si el taller es presencial o virtual.
     * @param esPresencial true si es presencial, false si es virtual
     */
    public void setEsPresencial(boolean esPresencial) {
        this.esPresencial = esPresencial;
    }

}
