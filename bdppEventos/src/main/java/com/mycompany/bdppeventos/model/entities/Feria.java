package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ferias")
public class Feria extends Evento {

    // Atributos
    @Column(name = "cantidad_stand", nullable = false)
    private int cantidadStands;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobertura", nullable = false, length = 20)
    private TipoCobertura tipoCobertura;

    // Constructores
    public Feria() {
        super();
    }

    public Feria(int cantidadStands, TipoCobertura tipoCobertura) {
        super();
        this.setCantidadStands(cantidadStands);
        this.setTipoCobertura(tipoCobertura);
    }

    public Feria(int cantidadStands, TipoCobertura tipoCobertura, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setCantidadStands(cantidadStands);
        this.setTipoCobertura(tipoCobertura);
    }

    // Getters y Setters
    public int getCantidadStands() {
        return cantidadStands;
    }

    public void setCantidadStands(int cantidadStands) {
        if (cantidadStands <= 0) {
            throw new IllegalArgumentException("La cantidad de Stands no puede ser negativa ni igual a 0");
        } else {
            this.cantidadStands = cantidadStands;
        }
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        if (tipoCobertura == null) {
            throw new IllegalArgumentException("El tipo de cobertura no puede ser null");
        } else {
            this.tipoCobertura = tipoCobertura;
        }
    }

}
