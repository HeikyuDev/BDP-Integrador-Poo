package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.interfaces.Activable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "participaciones")
public class Participacion implements Activable {

    @Id
    @Column(name = "id_participacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParticipacion;

    @Column(name = "activo")
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "dni_persona")
    private Persona unaPersona;

    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento unEvento;

    @ManyToOne
    @JoinColumn(name = "tipo_rol_id")
    private TipoRol unTipoRol;

    // Constructores
    public Participacion() {
    }

    public Participacion(int idParticipacion, boolean activo, Persona unaPersona, Evento unEvento, TipoRol unTipoRol) {
        this.idParticipacion = idParticipacion;
        this.activo = activo;
        this.unaPersona = unaPersona;
        this.unEvento = unEvento;
        this.unTipoRol = unTipoRol;
    }

    // Getters and Setters
    public int getIdParticipacion() {
        return idParticipacion;
    }

    public Persona getUnaPersona() {
        return unaPersona;
    }

    public void setUnaPersona(Persona unaPersona) {
        if (unaPersona == null) {
            throw new IllegalArgumentException("La persona no puede ser nulo");
        }
        this.unaPersona = unaPersona;
    }

    public TipoRol getUnTipoRol() {
        return unTipoRol;
    }

    public void setUnTipoRol(TipoRol unTipoRol) {
        if (unTipoRol == null) {
            throw new IllegalArgumentException("Tipo de rol no puede ser nulo");
        }
        this.unTipoRol = unTipoRol;
    }

    
    //TODO: Falta validaciones 
    public Evento getUnEvento() {
        return unEvento;
    }

    public void setUnEvento(Evento unEvento) {
        this.unEvento = unEvento;
    }

    
    
    // Metodos Interfaz Activable
    @Override
    public void activar() {
        this.activo = ACTIVO;
    }

    @Override
    public void desactivar() {
        this.activo = INACTIVO;
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
