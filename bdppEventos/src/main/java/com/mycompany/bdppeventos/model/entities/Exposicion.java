package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "exposiciones")
public class Exposicion extends Evento {

    // Atributos 
    @ManyToOne
    @JoinColumn(name = "id_tipo_arte")
    TipoDeArte unTipoArte;

    // Constructores
    public Exposicion() {
        super();
    }

    public Exposicion(TipoDeArte unTipoArte) {
        super();
        this.setUnTipoArte(unTipoArte);
    }

    public Exposicion(TipoDeArte unTipoArte, int id, String nombre, LocalDate fechaInicio, int duracionEstimada, boolean tieneCupo, int capacidadMaxima, boolean tieneInscripcion, String ubicacion, EstadoEvento estado, List<Participacion> unaListaParticipacion) {
        super(id, nombre, fechaInicio, duracionEstimada, tieneCupo, capacidadMaxima, tieneInscripcion, ubicacion, estado, unaListaParticipacion);
        this.setUnTipoArte(unTipoArte);
    }

    
    
    //getters y Setters    
    
    public TipoDeArte getUnTipoArte() {
        return unTipoArte;
    }

    public void setUnTipoArte(TipoDeArte unTipoArte) {
        if(unTipoArte == null)
        {
            throw new IllegalArgumentException("unTipoArte no puede ser null");
        }
        else
        {
            this.unTipoArte = unTipoArte;
        }
    }
        
}
