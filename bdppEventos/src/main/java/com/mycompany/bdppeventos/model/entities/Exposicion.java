package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.TipoRol;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Clase que representa una Exposición, subclase de Evento.
 * Agrega el atributo unTipoArte para especificar el tipo de arte de la exposición.
 */
@Entity
@Table(name = "exposiciones")
public final class Exposicion extends Evento {


    /** Tipo de arte asociado a la exposición (relación muchos a uno) */
    @ManyToOne
    @JoinColumn(name = "id_tipo_arte")
    TipoDeArte unTipoArte;
      
    // Constructores

    /**
     * Constructor por defecto. Inicializa la exposición como activa.
     */
    public Exposicion() {
        super();
    }

    // TODO: Hacer Contructor Con parametros           
    
    // Getters y Setters
    public TipoDeArte getUnTipoArte() {
        return unTipoArte;
    }

    /**
     * Asigna el tipo de arte a la exposición, validando que no sea null.
     * @param unTipoArte Tipo de arte a asignar
     */
    public void setUnTipoArte(TipoDeArte unTipoArte) {
        if(unTipoArte == null) {
            throw new IllegalArgumentException("unTipoArte no puede ser null");
        } else {
            this.unTipoArte = unTipoArte;
        }
    }

    /**
     * Obtiene el curador del evento (si es una exposición).
     */
    public Persona getCurador() {
        List<Persona> curadores = getPersonasPorRol(TipoRol.CURADOR);
        return curadores.isEmpty() ? null : curadores.get(0);
    }
    
    
}
