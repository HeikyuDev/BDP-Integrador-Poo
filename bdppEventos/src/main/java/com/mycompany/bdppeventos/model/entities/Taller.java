package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "talleres")
public class Taller extends Evento {

    @Column(name = "es_presencial", nullable = false)
    private boolean esPresencial;
    
    public Taller() {
        super();
    }

    public Taller(boolean esPresencial) {
        super();
        this.setEsPresencial(esPresencial);
    }

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
    public boolean isEsPresencial() {
        return esPresencial;
    }

    public void setEsPresencial(boolean esPresencial) {
        this.esPresencial = esPresencial;
    }

}
