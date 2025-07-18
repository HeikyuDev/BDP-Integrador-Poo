package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import java.time.LocalDate;

import com.mycompany.bdppeventos.model.enums.TipoConcierto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.List;

/**
 * Entidad que representa un Concierto como especialización de Evento
 * Un concierto es un evento musical que puede tener entrada gratuita o paga
 */
@Entity
@Table(name = "conciertos")
public class Concierto extends Evento {

    @Enumerated(EnumType.STRING) // Se usa STRING para almacenar el nombre del enum en la base de datos
    @Column(name = "tipo_concierto", nullable = false, length = 20) // length debe ser suficiente para el nombre del enum
    private TipoConcierto tipoConcierto;

    // Constructores

    public Concierto() {
        super();
    }

    public Concierto(TipoConcierto tipoConcierto) {
        this.tipoConcierto = tipoConcierto;
    }

    public Concierto(TipoConcierto tipoConcierto, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, boolean esPago, double monto, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, esPago, monto, unaListaParticipacion);
        this.tipoConcierto = tipoConcierto;
    }

    // Getters y Setters

    public TipoConcierto getTipoConcierto() {
        return tipoConcierto;
    }

    public void setTipoConcierto(TipoConcierto tipoConcierto) {
        if(tipoConcierto == null)
        {
            throw new IllegalArgumentException("tipoConcierto no puede ser null");
        }
        this.tipoConcierto = tipoConcierto;
    }                   
}