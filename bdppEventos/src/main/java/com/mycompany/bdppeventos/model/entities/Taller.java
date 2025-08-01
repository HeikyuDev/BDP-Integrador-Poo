
/**
 * Clase que representa un Taller, subclase de Evento.
 * Agrega el atributo esPresencial para indicar si el taller es presencial o virtual.
 */
package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.TipoRol;
import java.time.LocalDate;
import java.util.List;

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
     * Constructor por defecto. Inicializa el taller como activo.
     */
    public Taller() {
        super();
    }


    /**
     * Constructor que recibe solo si el taller es presencial.
     * @param esPresencial true si es presencial, false si es virtual
     */
    public Taller(boolean esPresencial) {
        super();
        this.setEsPresencial(esPresencial);
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
     * Obtiene el instructor del evento (si es un taller).
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
