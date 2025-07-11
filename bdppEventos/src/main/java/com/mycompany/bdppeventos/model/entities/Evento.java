package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos")
public abstract class Evento implements Activable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;

    // Que anotation es reocmendada. //Hace falta usar Temporal ??
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "duracion_estimada", nullable = false)
    private int duracionEstimada;

    @Column(name = "tiene_cupo", nullable = false)
    private boolean tieneCupo;

    @Column(name = "capacidad_maxima", nullable = false)
    private int capacidadMaxima;

    @Column(name = "tiene_inscripcion", nullable = false)
    private boolean tieneInscripcion;

    @Column(name = "ubicacion", length = 35, nullable = false)
    private String ubicacion;

    @Column(name = "estado", nullable = false)
    private EstadoEvento estado;

    @Column(name = "es_pago", nullable = false)
    private boolean esPago;

    @Column(name = "monto", nullable = false)
    private double monto;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    // Relacion uno muchos evento con participacion
    @OneToMany(mappedBy = "estado")
    private List<Participacion> unaListaParticipacion;

    // Constructores

    public Evento() {
    }

    public Evento(String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo,
            int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado,
            boolean esPago, double monto) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimada = duracionEstimada;
        this.tieneCupo = tieneCupo;
        this.capacidadMaxima = capacidadMaxima;
        this.tieneInscripcion = tieneInscripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.esPago = esPago;
        this.monto = monto;
    }

    // Getters Y setters

    // TODO: Falta validaciones IMPORTANTE
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public boolean isTieneCupo() {
        return tieneCupo;
    }

    public void setTieneCupo(boolean tieneCupo) {
        this.tieneCupo = tieneCupo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean isTieneInscripcion() {
        return tieneInscripcion;
    }

    public void setTieneInscripcion(boolean tieneInscripcion) {
        this.tieneInscripcion = tieneInscripcion;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public boolean isEsPago() {
        return esPago;
    }

    public void setEsPago(boolean esPago) {
        this.esPago = esPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public List<Participacion> getUnaListaParticipacion() {
        return unaListaParticipacion;
    }

    public void setUnaListaParticipacion(List<Participacion> unaListaParticipacion) {
        this.unaListaParticipacion = unaListaParticipacion;
    }

    // Metodos interfaz Activable

    @Override
    public void activar() {
        this.activo = ACTIVO;
    }

    @Override
    public void desactivar() {
        this.activo = INACTIVO;
    }

    @Override
    public Boolean getActivo() {
        return activo;
    }

    @Override
    public void setActivo(Boolean activo) {
        // Activo no puede ser null
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

}
