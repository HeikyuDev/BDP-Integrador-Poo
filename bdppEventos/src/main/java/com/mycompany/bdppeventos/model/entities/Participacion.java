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

@Entity
@Table(name = "participaciones")
public class Participacion implements Activable {

    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParticipacion;

    @ManyToOne
    @JoinColumn(name = "dni_persona")
    private Persona unaPersona;

    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento unEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rol", nullable = false, length = 20)
    private TipoRol unTipoRol;

    @Column(name = "activo")
    private boolean activo;

    // Constructores
    public Participacion() {
        this.activo = true;
    }

    public Participacion(int idParticipacion, boolean activo, Persona unaPersona, Evento unEvento, TipoRol unTipoRol) {
        this.idParticipacion = idParticipacion;
        this.activo = true;
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

    // Metodos interfaz Activable
    @Override
    public void activar() {
        this.activo = true;
    }

    @Override
    public void desactivar() {
        this.activo = false;
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
