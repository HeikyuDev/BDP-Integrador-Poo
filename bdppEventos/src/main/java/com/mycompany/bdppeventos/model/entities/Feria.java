
/**
 * Clase que representa una Feria, subclase de Evento.
 * Agrega los atributos cantidadStands y tipoCobertura para modelar ferias específicas.
 */
package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "ferias")
public final class Feria extends Evento {


    /** Cantidad de stands disponibles en la feria (no puede ser 0 ni negativo) */
    @Column(name = "cantidad_stand", nullable = true)
    private int cantidadStands;

    /** Tipo de cobertura de la feria (enum, no nulo) */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobertura", nullable = true, length = 20)
    private TipoCobertura tipoCobertura;


    // Constructores

    /**
     * Constructor por defecto. Inicializa la feria como activa.
     */
    public Feria() {
        super();
    }

    /**
     * Constructor que recibe solo la cantidad de stands y el tipo de cobertura.
     * @param cantidadStands Cantidad de stands
     * @param tipoCobertura Tipo de cobertura de la feria
     */
    public Feria(int cantidadStands, TipoCobertura tipoCobertura) {
        super();
        this.setCantidadStands(cantidadStands);
        this.setTipoCobertura(tipoCobertura);
    }

    /**
     * Constructor completo para inicializar todos los campos de la feria.
     * @param cantidadStands Cantidad de stands
     * @param tipoCobertura Tipo de cobertura
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
    public Feria(int cantidadStands, TipoCobertura tipoCobertura, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Persona> listaPersonas) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, listaPersonas);
        this.setCantidadStands(cantidadStands);
        this.setTipoCobertura(tipoCobertura);
    }


    // Getters y Setters

    /**
     * Devuelve la cantidad de stands de la feria.
     */
    public int getCantidadStands() {
        return cantidadStands;
    }

    /**
     * Asigna la cantidad de stands, validando que sea positiva y mayor a cero.
     * @param cantidadStands Cantidad de stands
     */
    public void setCantidadStands(int cantidadStands) {
        if (cantidadStands <= 0) {
            throw new IllegalArgumentException("La cantidad de Stands no puede ser negativa ni igual a 0");
        } else {
            this.cantidadStands = cantidadStands;
        }
    }

    /**
     * Devuelve el tipo de cobertura de la feria.
     */
    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    /**
     * Asigna el tipo de cobertura de la feria, validando que no sea null.
     * @param tipoCobertura Tipo de cobertura
     */
    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        if (tipoCobertura == null) {
            throw new IllegalArgumentException("El tipo de cobertura no puede ser null");
        } else {
            this.tipoCobertura = tipoCobertura;
        }
    }

}
