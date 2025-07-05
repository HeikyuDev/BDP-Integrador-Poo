package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.interfaces.Activable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tipos_de_arte")
public class TipoDeArte implements Activable{

    // Atributos
    
    @Id
    @Column(name = "id_tipo_arte", nullable = false)
    private int idTipoArte;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "activo", nullable = false)
    private boolean activo;
    
    @OneToMany(mappedBy = "unTipoArte")    
    private List<Exposicion> unaListaExposicion;
    // Constructores

    public TipoDeArte() {
    }

    public TipoDeArte(int idTipoArte, String nombre, boolean activo, List<Exposicion> unaListaExposicion) {
        this.idTipoArte = idTipoArte;
        this.nombre = nombre;
        this.activo = activo;
        this.unaListaExposicion = unaListaExposicion;
    }
    
    // Getters y Setters    
    // TODO: Falta las validaciones
    
    public int getIdTipoArte() {
        return idTipoArte;
    }

    public void setIdTipoArte(int idTipoArte) {
        this.idTipoArte = idTipoArte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Exposicion> getUnaListaExposicion() {
        return unaListaExposicion;
    }

    public void setUnaListaExposicion(List<Exposicion> unaListaExposicion) {
        this.unaListaExposicion = unaListaExposicion;
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
