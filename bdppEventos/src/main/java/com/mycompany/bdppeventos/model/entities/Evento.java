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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Clase abstracta base para representar un Evento en el sistema.
 * Incluye atributos comunes y lógica de validación para todos los tipos de
 * eventos.
 * Implementa la interfaz Activable para permitir activar/desactivar el evento.
 */
@Entity
@Table(name = "eventos")
public abstract class Evento implements Activable {

    /** Identificador único del evento (clave primaria, autoincremental) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Nombre del evento (máx 35 caracteres, no nulo) */
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;

    /** Fecha de inicio del evento (no nulo) */
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /** Duración estimada del evento en horas (no nulo, positivo) */
    @Column(name = "duracion_estimada", nullable = false)
    private int duracionEstimada;

    /** Indica si el evento tiene cupo máximo de participantes */
    @Column(name = "tiene_cupo", nullable = false)
    private boolean tieneCupo;

    /** Capacidad máxima de participantes (si tieneCupo es true) */
    @Column(name = "capacidad_maxima", nullable = false)
    private int capacidadMaxima;

    /** Indica si el evento requiere inscripción previa */
    @Column(name = "tiene_inscripcion", nullable = false)
    private boolean tieneInscripcion;

    /** Ubicación física del evento (máx 50 caracteres, no nulo) */
    @Column(name = "ubicacion", length = 50, nullable = false)
    private String ubicacion;

    /** Estado actual del evento (enum: ACTIVO, CANCELADO, FINALIZADO, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEvento estado;

    /** Indica si el evento está activo (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;

    /**
     * Relación uno a muchos: un evento puede tener muchas participaciones
     * (personas inscriptas o asistentes)
     */
    @ManyToMany
    @JoinTable(name = "evento_persona", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "persona_id"))
    private List<Persona> listaPersonas;

    // Constructores

    /**
     * Constructor por defecto. Marca el evento como activo.
     */
    public Evento() {
        this.activo = true;
        this.estado = EstadoEvento.PLANIFICADO;
    }
    
    public Evento(int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo,
            int capacidadMaxima, boolean tieneInscripcion, String ubicacion,
            List<Persona> listaPersonas) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimada = duracionEstimada;
        this.tieneCupo = tieneCupo;
        this.capacidadMaxima = capacidadMaxima;
        this.tieneInscripcion = tieneInscripcion;
        this.ubicacion = ubicacion;
        this.estado = EstadoEvento.PLANIFICADO;
        this.listaPersonas = listaPersonas; 
        this.activo = true;
    }

    // Getters y setters

    /**
     * Devuelve el identificador único del evento.
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del evento, validando que no sea nulo ni exceda 35
     * caracteres.
     * 
     * @param nombre Nombre del evento
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("El nombre no puede exceder los 35 caracteres");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Devuelve la fecha de inicio del evento.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Asigna la fecha de inicio, validando que no sea nula ni anterior a la fecha
     * actual.
     * 
     * @param fechaInicio Fecha de inicio
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser posterior a la fecha actual");
        }
        this.fechaInicio = fechaInicio;
    }

    /**
     * Devuelve la duración estimada del evento.
     */
    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    /**
     * Asigna la duración estimada, validando que sea positiva.
     * 
     * @param duracionEstimada Duración estimada
     */
    public void setDuracionEstimada(int duracionEstimada) {
        if (duracionEstimada <= 0) {
            throw new IllegalArgumentException("La duracion del evento no puede ser negativo o nulo");
        } else {
            this.duracionEstimada = duracionEstimada;
        }
    }

    /**
     * Indica si el evento tiene cupo máximo.
     */
    public boolean isTieneCupo() {
        return tieneCupo;
    }

    /**
     * Asigna si el evento tiene cupo máximo. Si es false, la capacidad máxima se
     * pone en 0.
     * 
     * @param tieneCupo true si tiene cupo, false si no
     */
    public void setTieneCupo(boolean tieneCupo) {
        this.tieneCupo = tieneCupo;
        if (this.tieneCupo == false) {
            this.capacidadMaxima = 0;
        }
    }

    /**
     * Devuelve la capacidad máxima de participantes.
     */
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * Asigna la capacidad máxima, validando que sea positiva y que tieneCupo sea
     * true.
     * 
     * @param capacidadMaxima Capacidad máxima
     */
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

    /**
     * Indica si el evento requiere inscripción previa.
     */
    public boolean isTieneInscripcion() {
        return tieneInscripcion;
    }

    /**
     * Asigna si el evento requiere inscripción previa.
     * 
     * @param tieneInscripcion true si requiere inscripción
     */
    public void setTieneInscripcion(boolean tieneInscripcion) {
        this.tieneInscripcion = tieneInscripcion;
    }

    /**
     * Devuelve la ubicación física del evento.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Asigna la ubicación física, validando que no sea nula ni exceda 50
     * caracteres.
     * 
     * @param ubicacion Ubicación física
     */
    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        if (ubicacion.trim().length() > 50) {
            throw new IllegalArgumentException("La ubicación no puede exceder los 50 caracteres");
        }
        this.ubicacion = ubicacion.trim();
    }

    /**
     * Devuelve el estado actual del evento.
     */
    public EstadoEvento getEstado() {
        return estado;
    }

    /**
     * Asigna el estado del evento, validando que no sea nulo.
     * 
     * @param estado Estado del evento
     */
    public void setEstado(EstadoEvento estado) {
        if (estado == null) {
            throw new IllegalArgumentException("el estado del evento no puede ser nulo");
        } else {
            this.estado = estado;
        }
    }

    /**
     * Devuelve la lista de participaciones asociadas al evento.
     */
    public List<Persona> getListaPersona() {
        return this.listaPersonas;
    }

    /**
     * Asigna la lista de participaciones asociadas al evento.
     * 
     * @param unaListaParticipacion Lista de participaciones
     */
    public void setUnaListaPersona(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    // Métodos de la interfaz Activable

    /**
     * Activa el evento (lo marca como activo).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva el evento (lo marca como inactivo).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si el evento está activo.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo del evento. No permite null.
     * 
     * @param activo true para activo, false para inactivo
     */
    @Override
    public void setActivo(Boolean activo) {
        // Activo no puede ser null
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

}
