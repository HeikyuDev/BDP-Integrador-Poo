package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.TipoEntrada;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Entidad que representa un Concierto como especialización de Evento
 * Un concierto es un evento musical que puede tener entrada gratuita o paga
 */
@Entity
@Table(name = "conciertos")
public class Concierto extends Evento {

    @Enumerated(EnumType.STRING) // Se usa STRING para almacenar el nombre del enum en la base de datos
    @Column(name = "tipo_entrada", nullable = false, length = 20) // length debe ser suficiente para el nombre del enum
    private TipoEntrada tipoEntrada;

    // Constructores

    public Concierto() {
        super();
    }

    // Constructor simplificado para crear conciertos gratis en general
    public Concierto(String nombre, LocalDate fechaInicio, int duracionEstimada,
            int capacidadMaxima, TipoEntrada tipoEntrada) {
        super();
        this.setNombre(nombre);
        this.setFechaInicio(fechaInicio);
        this.setDuracionEstimada(duracionEstimada);
        this.setTieneCupo(true); // Los conciertos generalmente tienen cupo limitado
        this.setCapacidadMaxima(capacidadMaxima);
        this.setTieneInscripcion(false); // Por defecto no requieren inscripción previa
        this.setTipoEntrada(tipoEntrada);

        if (tipoEntrada == TipoEntrada.PAGA) {
            if (this.getMontoInscripcion() <= 0) {
                throw new IllegalArgumentException("Debe especificar un monto positivo para conciertos pagos.");
            }
            this.setMontoInscripcion(this.getMontoInscripcion());
            this.setEsPago(true);
        } else {
            this.setMontoInscripcion(0.0);
            this.setEsPago(false);
        }

        this.activar(); // Se activa por defecto
    }

    // Constructor para conciertos con entrada paga
    public Concierto(String nombre, LocalDate fechaInicio, int duracionEstimada,
            int capacidadMaxima, double montoEntrada) {
        this(nombre, fechaInicio, duracionEstimada, capacidadMaxima, TipoEntrada.PAGA);
        this.setMontoInscripcion(montoEntrada);
    }

    // Getters y Setters

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {

        this.tipoEntrada = tipoEntrada;

        // Sincronizar con los atributos del evento padre
        if (tipoEntrada == TipoEntrada.GRATUITA) {
            this.setEsPago(false);
            this.setMontoInscripcion(0.0);
        } else {
            this.setEsPago(true);
        }
    }

}