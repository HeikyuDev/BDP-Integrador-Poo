package com.mycompany.bdppeventos.model.entities;

import java.util.List;

import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "proyecciones")
public class Proyeccion implements Activable {


    /** Identificador único de la proyección (clave primaria, autoincremental) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProyeccion;

    

    /** Nombre de la proyección (máximo 35 caracteres, no nulo) */
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;
        
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "proyeccion_pelicula", joinColumns = @JoinColumn(name = "id_proyeccion"), inverseJoinColumns = @JoinColumn(name = "id_pelicula"))
    private List<Pelicula> unaListaPelicula;

    
    /** Indica si la proyección está activa (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;                                               
    
    // === CONSTRUCTORES === 

    public Proyeccion() {
        this.activo = true; // Por defecto activo
    }

    public Proyeccion(int idProyeccion, String nombre, List<Pelicula> unaListaPelicula) {
        this.idProyeccion = idProyeccion;
        this.nombre = nombre;
        this.unaListaPelicula = unaListaPelicula;
        this.activo = true;
    }          
    // === GETTERS Y SETTERS === 
    
    
    public int getIdProyeccion() {
        return idProyeccion;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("Nombre no puede exceder 35 caracteres");
        }
        this.nombre = nombre.trim();
    }
    
    

    public List<Pelicula> getUnaListaPelicula() {
        return unaListaPelicula;
    }

    public void setUnaListaPelicula(List<Pelicula> unaListaPelicula) {
        this.unaListaPelicula = unaListaPelicula;
    }
                                
    // Métodos de la interfaz Activable

    /**
     * Activa la proyección (la marca como activa).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la proyección (la marca como inactiva).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si la proyección está activa.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo de la proyección. No permite null.
     * @param activo true para activo, false para inactivo
     */
    @Override
    public void setActivo(Boolean activo) {
        // Activo no puede ser null
        if (activo == null) {
            throw new IllegalArgumentException("Estado activo no puede ser nulo");
        }
        this.activo = activo;
    }

    @Override
    public String toString() {
        return   nombre;
    }
    
    
}
