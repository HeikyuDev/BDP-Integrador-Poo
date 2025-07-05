package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.interfaces.Activable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "eventos")
public abstract class Evento implements Activable {
    
    @Id
    @Column(name = "id_evento", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvento;
    
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
    
    @Column (name = "estado_evento", nullable = false)
    private EstadoEvento unEstadoEvento;
    
    @Column(name = "es_pago", nullable = false)
    private boolean esPago;
    
    @Column(name = "monto_inscripcion", nullable = false)
    private double montoInscripcion;    
    
    @Column (name = "activo", nullable = false)
    private boolean activo;
    
    // Relacion uno muchos evento con participacion
    @OneToMany(mappedBy = "unEvento")
    private List<Participacion> unaListaParticipacion;
        
    // Constructores 
    
    public Evento() {
    }

    public Evento(int idEvento, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, EstadoEvento unEstadoEvento, boolean esPago, double montoInscripcion, boolean activo, List<Participacion> unaListaParticipacion) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimada = duracionEstimada;
        this.tieneCupo = tieneCupo;
        this.capacidadMaxima = capacidadMaxima;
        this.tieneInscripcion = tieneInscripcion;
        this.unEstadoEvento = unEstadoEvento;
        this.esPago = esPago;
        this.montoInscripcion = montoInscripcion;
        this.activo = activo;
        this.unaListaParticipacion = unaListaParticipacion;
    }
    
    // Getters  Y setters

    public int getIdEvento() {
        return idEvento;
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

    public EstadoEvento getUnEstadoEvento() {
        return unEstadoEvento;
    }

    public void setUnEstadoEvento(EstadoEvento unEstadoEvento) {
        this.unEstadoEvento = unEstadoEvento;
    }

    public boolean isEsPago() {
        return esPago;
    }

    public void setEsPago(boolean esPago) {
        this.esPago = esPago;
    }

    public double getMontoInscripcion() {
        return montoInscripcion;
    }

    public void setMontoInscripcion(double montoInscripcion) {
        this.montoInscripcion = montoInscripcion;
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
