package com.mycompany.bdppeventos.model.entities;

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
 * Entidad intermedia que representa la participación de una persona en un evento específico.
 * Define el rol que cumple esa persona en ese evento particular. 
 */

@Entity
@Table(name = "participaciones")
public class Participacion implements Activable {

    /** Identificador único de la participación (clave primaria, autoincremental) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Evento en el que participa la persona */
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento unEvento;

    /** Persona que participa en el evento */
    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona unaPersona;

    /** Rol específico que cumple la persona en este evento */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_en_evento", nullable = false, length = 20)
    private TipoRol rolEnEvento;
    

    /** Indica si la participación está activa */
    @Column(name = "activo", nullable = false)
    private boolean activo;

    // === CONSTRUCTORES === 

    /**
     * Constructor por defecto. Marca la participación como activa.
     */
    public Participacion() {
        this.activo = true;
    }

    /**
     * Constructor para crear una participación con los datos básicos.
     * 
     * @param evento Evento en el que participa
     * @param persona Persona que participa
     * @param rolEnEvento Rol específico en este evento
     */
    public Participacion(Evento evento, Persona persona, TipoRol rolEnEvento) {
        this();
        this.unEvento = evento;
        this.unaPersona = persona;
        this.rolEnEvento = rolEnEvento;
    }
   

    // === GETTERS Y SETTERS === 

    /**
     * Devuelve el identificador único de la participación.
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el evento asociado.
     */
    public Evento getEvento() {
        return this.unEvento;
    }

    /**
     * Asigna el evento, validando que no sea nulo.
     * 
     * @param evento Evento asociado
     */
    
    public void setEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo");
        }
        this.unEvento = evento;
    }

    /**
     * Devuelve la persona asociada.
     */
    public Persona getPersona() {
        return this.unaPersona;
    }

    /**
     * Asigna la persona, validando que no sea nula.
     * 
     * @param persona Persona asociada
     */
    public void setPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        this.unaPersona = persona;
    }

    /**
     * Devuelve el rol específico en el evento.
     */
    public TipoRol getRolEnEvento() {
        return rolEnEvento;
    }

    /**
     * Asigna el rol específico en el evento, validando que no sea nulo.
     * 
     * @param rolEnEvento Rol específico en el evento
     */
    public void setRolEnEvento(TipoRol rolEnEvento) {
        if (rolEnEvento == null) {
            throw new IllegalArgumentException("El rol en el evento no puede ser nulo");
        }
        this.rolEnEvento = rolEnEvento;
    }

    // Métodos de la interfaz Activable

    /**
     * Activa la participación.
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la participación.
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
     * Asigna el estado activo/inactivo de la participación.
     * 
     * @param activo true para activo, false para inactivo
     */
    @Override
    public void setActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

    /**
     * Devuelve una representación en texto de la participación.
     */
    @Override
    public String toString() {
        return unaPersona.toString() + " - " + rolEnEvento + " en " + unEvento.getNombre();
    }

    /**
     * Verifica si esta participación corresponde a un rol específico.
     * 
     * @param rol Rol a verificar
     * @return true si el rol coincide
     */
    public boolean esRol(TipoRol rol) {
        return this.rolEnEvento.equals(rol);
    }
}