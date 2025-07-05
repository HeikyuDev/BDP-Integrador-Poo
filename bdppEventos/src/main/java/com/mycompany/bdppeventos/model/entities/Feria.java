package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "ferias")
public class Feria extends Evento{
    
    // Atributos
    @Column(name = "cantidad_stand", nullable = false)
    private int cantidadStands;
    
    @Column(name = "tipo_cobertura", nullable = false)
    private TipoCobertura tipoCobertura;
    
    // Constructores

    public Feria() {
        super(); // Me comunico con el Contructor sin  parametros de la clase padre para poder crear la instancia
    }

    public Feria(int cantidadStands, TipoCobertura tipoCobertura) {
        super(); // Me comunico con el Contructor sin  parametros de la clase padre para poder crear la instancia
        this.cantidadStands = cantidadStands;
        this.tipoCobertura = tipoCobertura;
    }

    public Feria(int cantidadStands, TipoCobertura tipoCobertura, int idEvento, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, EstadoEvento unEstadoEvento, boolean esPago, double montoInscripcion, boolean activo, List<Participacion> unaListaParticipacion) {
        super(idEvento, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, unEstadoEvento, esPago, montoInscripcion, activo, unaListaParticipacion);
        this.cantidadStands = cantidadStands;
        this.tipoCobertura = tipoCobertura;
    }
    
    // Getters y Setters

    
    // TODO: Falta validaciones
    
    public int getCantidadStands() {
        return cantidadStands;
    }

    public void setCantidadStands(int cantidadStands) {
        this.cantidadStands = cantidadStands;
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }
    
    
    
    
}
