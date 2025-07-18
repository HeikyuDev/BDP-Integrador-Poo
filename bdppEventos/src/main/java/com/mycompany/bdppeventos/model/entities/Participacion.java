package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase intermedia que representa la participación de una persona en un evento.
 * Asocia un TipoRol a una persona dentro de un evento específico.
 * Implementa la interfaz Activable para permitir activar/desactivar la participación.
 */
@Entity
@Table(name = "participaciones")
public class Participacion implements Activable {

    

    /** Identificador único de la participación (clave primaria, autoincremental) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParticipacion;


    /** Persona asociada a la participación (muchas participaciones pueden ser de una persona) */
    @ManyToOne
    @JoinColumn(name = "dni_persona")
    private Persona unaPersona;


    /** Evento asociado a la participación (muchas participaciones pueden ser de un evento) */
    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento unEvento;


    /** Rol que cumple la persona en el evento (CURADOR, ASISTENTE) */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rol", nullable = false, length = 20)
    private TipoRol unTipoRol;


    /** Indica si la participación está activa (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;


    // Constructores

    /**
     * Constructor por defecto. Marca la participación como activa.
     */
    public Participacion() {
        this.activo = true;
    }

    /**
     * Constructor completo para inicializar todos los campos de la participación.
     * @param idParticipacion Identificador único
     * @param activo Estado de la participación
     * @param unaPersona Persona asociada
     * @param unEvento Evento asociado
     * @param unTipoRol Rol de la persona en el evento
     */
    public Participacion(int idParticipacion, boolean activo, Persona unaPersona, Evento unEvento, TipoRol unTipoRol) {
        this.idParticipacion = idParticipacion;
        this.activo = true;
        this.unaPersona = unaPersona;
        this.unEvento = unEvento;
        this.unTipoRol = unTipoRol;
    }


    // Getters y Setters

    /**
     * Devuelve el identificador único de la participación.
     */
    public int getIdParticipacion() {
        return idParticipacion;
    }


    /**
     * Devuelve la persona asociada a la participación.
     */
    public Persona getUnaPersona() {
        return unaPersona;
    }


    /**
     * Asocia una persona a la participación, validando que no sea nula.
     * @param unaPersona Persona a asociar
     */
    public void setUnaPersona(Persona unaPersona) {
        if (unaPersona == null) {
            throw new IllegalArgumentException("La persona no puede ser nulo");
        }
        this.unaPersona = unaPersona;
    }


    /**
     * Devuelve el rol de la persona en el evento.
     */
    public TipoRol getUnTipoRol() {
        return unTipoRol;
    }


    /**
     * Asigna el rol de la persona en el evento, validando que no sea nulo.
     * @param unTipoRol Rol a asignar
     */
    public void setUnTipoRol(TipoRol unTipoRol) {
        if (unTipoRol == null) {
            throw new IllegalArgumentException("Tipo de rol no puede ser nulo");
        }
        this.unTipoRol = unTipoRol;
    }

    

    /**
     * Devuelve el evento asociado a la participación.
     */
    public Evento getUnEvento() {
        return unEvento;
    }


    /**
     * Asocia un evento a la participación, validando que no sea nulo.
     * Si el rol es PARTICIPANTE, solo permite eventos confirmados o en ejecución.
     * @param unEvento Evento a asociar
     */
    public void setUnEvento(Evento unEvento) {
        if (unEvento == null) {
            throw new IllegalArgumentException("Un evento no puede ser nulo");
        }
        // Solo validamos si el rol ya está definido y es PARTICIPANTE
        if (this.unTipoRol == TipoRol.PARTICIPANTE) {
            EstadoEvento estado = unEvento.getEstado();
            if (estado != EstadoEvento.CONFIRMADO && estado != EstadoEvento.EN_EJECUCION) {
                throw new IllegalStateException("No se puede inscribir participantes a eventos que no estén confirmados o en ejecución");
            }
        }
        this.unEvento = unEvento;
    }


    // Métodos de la interfaz Activable

    /**
     * Activa la participación (la marca como activa).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la participación (la marca como inactiva).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si la participación está activa.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo de la participación. No permite null.
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
