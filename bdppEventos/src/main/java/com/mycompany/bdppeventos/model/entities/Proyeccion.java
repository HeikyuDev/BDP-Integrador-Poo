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

/**
 * Entidad que representa una Proyección
 * Una proyección pertenece a un CicloDeCine y está asociada a una Película
 */
@Entity
@Table(name = "proyecciones")
public class Proyeccion implements Activable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProyeccion;

    
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;
    
    @Column(name = "activo", nullable = false)
    private boolean activo;

    // Relación muchos a uno con CicloDeCine
    @ManyToOne
    @JoinColumn(name = "id_ciclo_cine", nullable = false)
    private CicloDeCine unCicloDeCine;

    // Relación muchos a uno con Pelicula
    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    // Constructor por defecto
    public Proyeccion() {
        this.activo = true; // Por defecto activo
    }

    public Proyeccion(int idProyeccion, String nombre, CicloDeCine unCicloDeCine, Pelicula pelicula) {
        this.idProyeccion = idProyeccion;
        this.nombre = nombre;
        this.activo = true;
        this.unCicloDeCine = unCicloDeCine;
        this.pelicula = pelicula;
    }

    

    // Getters y Setters

    public int getIdProyeccion() {
        return idProyeccion;
    }   

    public CicloDeCine getUnCicloDeCine() {
        return unCicloDeCine;
    }

    public void setUnCicloDeCine(CicloDeCine unCicloDeCine) {
        this.unCicloDeCine = unCicloDeCine;
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
    
    
    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
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
