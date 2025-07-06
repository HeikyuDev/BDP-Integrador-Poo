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

    public Taller(int idEvento, String nombre, LocalDate fechaInicio, int duracionEstimada,
            boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion,
            EstadoEvento unEstadoEvento, boolean esPago, double montoInscripcion,
            boolean activo, List<Participacion> unaListaParticipacion,
            int cupoMaximo, TipoModalidad modalidad) {
        super(idEvento, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima,
                tieneInscripcion, unEstadoEvento, esPago, montoInscripcion, activo, unaListaParticipacion);
        setCupoMaximo(cupoMaximo);
        setModalidad(modalidad);
    }

    // Constructor simplificado para crear talleres nuevos desde la interfaz
    // No requiere idEvento ya que es autogenerado por la base de datos
    public Taller(String nombre, LocalDate fechaInicio, int duracionEstimada,
            int cupoMaximo, TipoModalidad modalidad) {
        super();
        this.setNombre(nombre);
        this.setFechaInicio(fechaInicio);
        this.setDuracionEstimada(duracionEstimada);
        this.setTieneCupo(true); // Los talleres siempre tienen cupo
        this.setCapacidadMaxima(cupoMaximo);
        this.setTieneInscripcion(true); // Los talleres requieren inscripción
        this.setEsPago(false); // Por defecto gratuito
        this.setMontoInscripcion(0.0);
        this.setCupoMaximo(cupoMaximo);
        this.setModalidad(modalidad);
        this.activar(); // Se activa por defecto
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
