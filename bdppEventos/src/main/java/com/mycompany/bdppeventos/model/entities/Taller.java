package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoModalidad;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "talleres")
public class Taller extends Evento {

    
    @Enumerated(EnumType.STRING) // EnumType.STRING para guardar el nombre del enum en la base de datos
    private TipoModalidad modalidad;

    // Constructores

    public Taller() {
        super();
    }

    public Taller(TipoModalidad modalidad) {
        super();
        this.modalidad = modalidad;
    }

    public Taller(TipoModalidad modalidad, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, boolean esPago, double monto, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, esPago, monto, unaListaParticipacion);
        this.modalidad = modalidad;
    } 
    
       
    // Getters y Setters   

    public TipoModalidad getModalidad() {
        return modalidad;
    }
    
    public void setModalidad(TipoModalidad modalidad) {
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad no puede ser nula");
        }
        this.modalidad = modalidad;
    }

}
