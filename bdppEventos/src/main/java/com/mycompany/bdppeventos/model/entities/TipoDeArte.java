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
    @Column(name = "id", nullable = false)
    private int idTipoArte;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "activo", nullable = false)
    private boolean activo;
    
    @OneToMany(mappedBy = "unTipoArte")    
    private List<Exposicion> unaListaExposicion;
    // Constructores

    public TipoDeArte() {
        this.activo = true;
    }

    public TipoDeArte(int idTipoArte, String nombre, boolean activo, List<Exposicion> unaListaExposicion) {
        this.idTipoArte = idTipoArte;
        this.nombre = nombre;
        this.activo = true;
        this.unaListaExposicion = unaListaExposicion;
    }
    
    // Getters y Setters    
       
    public int getIdTipoArte() {
        return idTipoArte;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("El nombre no puede exceder los 35 caracteres");
        }

        this.nombre = nombre.trim();
    }
  
    public List<Exposicion> getUnaListaExposicion() {
        return unaListaExposicion;
    }

    public void setUnaListaExposicion(List<Exposicion> unaListaExposicion) {
        this.unaListaExposicion = unaListaExposicion;
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
    
    // To String

    @Override
    public String toString() {
        return this.nombre;
    }
    
    
}
