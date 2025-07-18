package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import java.time.LocalDate;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

/**
 * Entidad que representa un Concierto como especialización de Evento
 * Un concierto es un evento musical que puede tener entrada gratuita o paga
 */
@Entity
@Table(name = "conciertos")
public class Concierto extends Evento {

   @Column(name = "es_pago", nullable = false)
    private boolean esPago;

   @Column(name = "monto", nullable = false)
    private double monto;

    
    
    // Constructores

    public Concierto() {
        super();
    }

    public Concierto(boolean esPago, double monto) {
        super();
        this.setEsPago(esPago);
        this.setMonto(monto);
    }

    public Concierto(boolean esPago, double monto, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setEsPago(esPago);
        this.setMonto(monto);
    }
      
    // Getters y Setters
    public boolean isEsPago() {
        return esPago;
    }

    public void setEsPago(boolean esPago) {
        this.esPago = esPago;
        
        if(this.esPago == false)
        {
            this.monto = 0.0;
        }
    }

    public double getMonto() {
        return this.monto;
    }

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
