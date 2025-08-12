package com.mycompany.bdppeventos.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciclos_cine")

public final class CicloDeCine extends Evento {


    /** Indica si el ciclo de cine incluye charlas posteriores a las proyecciones */
    @Column(name = "charlas_posteriores", nullable = true)
    private boolean charlasPosteriores;


    /** Lista de proyecciones asociadas al ciclo de cine (relación uno a muchos) */
    @ManyToOne    
    @JoinColumn(name = "id_proyeccion", nullable = true)
    private Proyeccion unaProyeccion;


    // === CONSTRUCTORES === 
    
    // Constructor por defecto que inicializa como activo al ciclo, y en estado PLANIFICADO
    public CicloDeCine() {
        super();
    }            

    // === GETTERS Y SETTERS

    /**
     * Devuelve si el ciclo de cine incluye charlas posteriores.
     */
    public boolean isCharlasPosteriores() {
        return charlasPosteriores;
    }

    /**
     * Asigna si el ciclo de cine incluye charlas posteriores.
     * @param charlasPosteriores true si hay charlas posteriores
     */
    public void setCharlasPosteriores(boolean charlasPosteriores) {
        this.charlasPosteriores = charlasPosteriores;
    }

    public Proyeccion getUnaProyeccion() {
        return unaProyeccion;
    }

    public void setUnaProyeccion(Proyeccion unaProyeccion) {
        this.unaProyeccion = unaProyeccion;
    }

    public String getCharlasPosterioresTexto()
    {
        if(this.charlasPosteriores == true)
        {
            return "SI";
        }
        else 
        {
            return "NO";
        }
    }
    
}
