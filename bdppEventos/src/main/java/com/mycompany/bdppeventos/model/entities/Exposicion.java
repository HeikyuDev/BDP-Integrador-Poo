package com.mycompany.bdppeventos.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Exposiciones")
public class Exposicion extends Evento {

    // Atributos 
    @ManyToOne
    @JoinColumn(name = "id_tipo_arte")
    TipoDeArte unTipoArte;

    // Constructores
    public Exposicion() {
        super();
    }

    public Exposicion(TipoDeArte unTipoArte)
    {
        super(); // Utilizo el contructor sin paámetros de la clase padre
        this.unTipoArte = unTipoArte;
    }
    
    //getters y Setters
    // TODO: Falta validaciones
    
    public TipoDeArte getUnTipoArte() {
        return unTipoArte;
    }

    public void setUnTipoArte(TipoDeArte unTipoArte) {
        this.unTipoArte = unTipoArte;
    }
    
    

}
