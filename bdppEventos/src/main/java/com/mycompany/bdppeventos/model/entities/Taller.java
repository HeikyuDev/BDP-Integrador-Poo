package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoModalidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "talleres")
public class Taller extends Evento {

    @Column(name = "cupo_maximo", nullable = false)
    private int cupoMaximo;

    @Enumerated(EnumType.STRING) // EnumType.STRING para guardar el nombre del enum en la base de datos
    private TipoModalidad modalidad;

    // Constructores

    public Taller() {
        super();
    }

    public Taller(int cupoMaximo, TipoModalidad modalidad) {
        this.cupoMaximo = cupoMaximo;
        this.modalidad = modalidad;
    }

    public Taller(int cupoMaximo, TipoModalidad modalidad, String nombre, LocalDate fechaInicio, int duracionEstimada,
            boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion,
            EstadoEvento unEstadoEvento, boolean esPago, double montoInscripcion, boolean activo,
            List<Participacion> unaListaParticipacion) {
        super(nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion,
                unEstadoEvento, esPago, montoInscripcion, activo, unaListaParticipacion);
        this.cupoMaximo = cupoMaximo;
        this.modalidad = modalidad;
    }

    // Getters y Setters

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    /**
     * Establece el cupo máximo del taller
     * 
     * @param cupoMaximo número máximo de participantes
     * @throws IllegalArgumentException si el cupo es menor o igual a 0
     */
    public void setCupoMaximo(int cupoMaximo) {
        if (cupoMaximo <= 0) {
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0");
        }
        this.cupoMaximo = cupoMaximo;
        this.setCapacidadMaxima(cupoMaximo); // Sincronizar con la capacidad máxima del evento padre
    }

    public TipoModalidad getModalidad() {
        return modalidad;
    }

    /**
     * Establece la modalidad del taller
     * 
     * @param modalidad tipo de modalidad (PRESENCIAL o VIRTUAL)
     * @throws IllegalArgumentException si la modalidad es null
     */
    public void setModalidad(TipoModalidad modalidad) {
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad no puede ser nula");
        }
        this.modalidad = modalidad;
    }

}
