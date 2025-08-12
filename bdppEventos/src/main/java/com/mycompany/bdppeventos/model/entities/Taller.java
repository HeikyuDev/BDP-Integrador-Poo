package com.mycompany.bdppeventos.model.entities;

import java.util.List;

import com.mycompany.bdppeventos.model.enums.TipoRol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "talleres")
public final class Taller extends Evento {


    /** Indica si el taller es presencial (true) o virtual (false) */
    @Column(name = "es_presencial", nullable = true)
    private boolean esPresencial;
    

    /**
     * Constructor por defecto. Inicializa el taller como activo, Y en estado PLANIFICADO
     */
    public Taller() {
        super();
    }    

    // Getters y Setters

    /**
     * Devuelve si el taller es presencial.
     */
    public boolean isEsPresencial() {
        return esPresencial;
    }

    /**
     * Asigna si el taller es presencial o virtual.
     * @param esPresencial true si es presencial, false si es virtual
     */
    public void setEsPresencial(boolean esPresencial) {
        this.esPresencial = esPresencial;
    }
    
    /**
     * Obtiene el instructor del evento.
     */
    public Persona getInstructor() {
        List<Persona> instructores = getPersonasPorRol(TipoRol.INSTRUCTOR);
        return instructores.isEmpty() ? null : instructores.get(0);
    }
    
    public String getModalidadTexto()
    {
        if(this.esPresencial == true)
        {
            return "Presencial";
        }
        else
        {
            return "Virtual";
        }
        
    }

}
