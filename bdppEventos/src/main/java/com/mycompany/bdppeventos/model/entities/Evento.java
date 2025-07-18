package com.mycompany.bdppeventos.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "ubicacion", length = 50, nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEvento estado;
       
    @Column(name = "activo", nullable = false)
    private boolean activo;

    // Relacion uno muchos evento con participacion
    @OneToMany(mappedBy = "unEvento")
    private List<Participacion> unaListaParticipacion;

    // Constructores
    public Evento() {
        this.activo = true;
    }

    public Evento(int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        this.id = id;
        this.setNombre(nombre);
        this.setFechaInicio(fechaInicio);
        this.setDuracionEstimada(duracionEstimada);
        this.setTieneCupo(tieneCupo);
        this.setCapacidadMaxima(capacidadMaxima);
        this.setTieneInscripcion(tieneInscripcion);
        this.setUbicacion(ubicacion);
        this.setEstado(estado);
        this.setUnaListaParticipacion(unaListaParticipacion);
        this.activo = true;
    }

    // Getters Y setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("El nombre no puede exceder los 35 caracteres");
        }

        this.nombre = nombre.trim();
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser posterior a la fecha actual");
        }

        this.fechaInicio = fechaInicio;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        if (duracionEstimada <= 0) {
            throw new IllegalArgumentException("La duracion del evento no puede ser negativo o nulo");
        } else {
            this.duracionEstimada = duracionEstimada;
        }
    }

    public boolean isTieneCupo() {
        return tieneCupo;
    }

    public void setTieneCupo(boolean tieneCupo) {
        this.tieneCupo = tieneCupo;
        
        if(this.tieneCupo == false)
        {
            this.capacidadMaxima = 0;
        }
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        if (this.tieneCupo == false && capacidadMaxima > 0) {
            throw new IllegalArgumentException("La capacidad maxima requiere que tieneCupo sea verdadero");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad maxima debe ser positiva");
        } else {
            this.capacidadMaxima = capacidadMaxima;
        }
    }

    public boolean isTieneInscripcion() {
        return tieneInscripcion;
    }

    public void setTieneInscripcion(boolean tieneInscripcion) {
        this.tieneInscripcion = tieneInscripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        if (ubicacion.trim().length() > 50) {
            throw new IllegalArgumentException("La ubicación no puede exceder los 50 caracteres");
        }

        this.ubicacion = ubicacion.trim();
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        if (estado == null) {
            throw new IllegalArgumentException("el estado del evento no puede ser nulo");
        } else {
            this.estado = estado;
        }
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
        this.activo = true;
    }

    @Override
    public void desactivar() {
        this.activo = false;
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
