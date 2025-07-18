package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Clase que representa un Concierto, subclase de Evento.
 * Un concierto es un evento musical que puede tener entrada gratuita o paga.
 * Incluye atributos para indicar si es pago y el monto correspondiente.
 */
@Entity
@Table(name = "conciertos")
public final class Concierto extends Evento {


    /** Indica si el concierto es pago (true) o gratuito (false) */
    @Column(name = "es_pago", nullable = false)
    private boolean esPago;

    /** Monto de la entrada si el concierto es pago (0 si es gratuito) */
    @Column(name = "monto", nullable = false)
    private double monto;

    
    
    // Constructores

    /**
     * Constructor por defecto. Inicializa el concierto como activo.
     */
    public Concierto() {
        super();
    }

    /**
     * Constructor que recibe si es pago y el monto.
     * @param esPago true si es pago, false si es gratuito
     * @param monto Monto de la entrada (0 si es gratuito)
     */
    public Concierto(boolean esPago, double monto) {
        super();
        this.setEsPago(esPago);
        this.setMonto(monto);
    }

    /**
     * Constructor completo para inicializar todos los campos del concierto.
     * @param esPago true si es pago, false si es gratuito
     * @param monto Monto de la entrada
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
    public Concierto(boolean esPago, double monto, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setEsPago(esPago);
        this.setMonto(monto);
    }
      

    // Getters y Setters

    /**
     * Devuelve si el concierto es pago.
     */
    public boolean isEsPago() {
        return esPago;
    }

    /**
     * Asigna si el concierto es pago. Si es gratuito, el monto se pone en 0.
     * @param esPago true si es pago, false si es gratuito
     */
    public void setEsPago(boolean esPago) {
        this.esPago = esPago;
        if(this.esPago == false) {
            this.monto = 0.0;
        }
    }

    /**
     * Devuelve el monto de la entrada del concierto.
     */
    public double getMonto() {
        return this.monto;
    }

    /**
     * Asigna el monto de la entrada, validando que esPago sea true y el monto positivo.
     * @param monto Monto de la entrada
     */
    public void setMonto(double monto) {
        if (this.esPago == false) {
            throw new IllegalArgumentException("Es necesario que esPago sea verdadero para poder definir el monto del evento");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo o nulo");
        } else {
            this.monto = monto;
        }
    }

}
