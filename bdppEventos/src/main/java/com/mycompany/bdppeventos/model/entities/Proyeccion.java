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
 * Clase que representa una Proyección en el sistema.
 * Una proyección pertenece a un CicloDeCine y está asociada a una Película.
 * Implementa la interfaz Activable para permitir activar/desactivar la proyección.
 */
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
    

    /** Indica si la proyección está activa (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;


    /** Ciclo de cine al que pertenece la proyección (muchas proyecciones pueden pertenecer a un ciclo) */
    @ManyToOne
    @JoinColumn(name = "id_ciclo_cine", nullable = false)
    private CicloDeCine unCicloDeCine;


    /** Película asociada a la proyección (muchas proyecciones pueden ser de una película) */
    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;


    // Constructores

    /**
     * Constructor por defecto. Marca la proyección como activa.
     */
    public Proyeccion() {
        this.activo = true; // Por defecto activo
    }

    /**
     * Constructor completo para inicializar todos los campos de la proyección.
     * @param idProyeccion Identificador único
     * @param nombre Nombre de la proyección
     * @param unCicloDeCine Ciclo de cine asociado
     * @param pelicula Película asociada
     */
    public Proyeccion(int idProyeccion, String nombre, CicloDeCine unCicloDeCine, Pelicula pelicula) {
        this.idProyeccion = idProyeccion;
        this.nombre = nombre;
        this.activo = true;
        this.unCicloDeCine = unCicloDeCine;
        this.pelicula = pelicula;
    }

    


    // Getters y Setters

    /**
     * Devuelve el identificador único de la proyección.
     */
    public int getIdProyeccion() {
        return idProyeccion;
    }

    /**
     * Devuelve el ciclo de cine asociado a la proyección.
     */
    public CicloDeCine getUnCicloDeCine() {
        return unCicloDeCine;
    }

    /**
     * Asocia un ciclo de cine a la proyección.
     * @param unCicloDeCine Ciclo de cine a asociar
     */
    public void setUnCicloDeCine(CicloDeCine unCicloDeCine) {
        this.unCicloDeCine = unCicloDeCine;
    }

    /**
     * Devuelve el nombre de la proyección.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de la proyección, validando que no sea nulo ni exceda 35 caracteres.
     * @param nombre Nombre de la proyección
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("Nombre no puede exceder 35 caracteres");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Devuelve la película asociada a la proyección.
     */
    public Pelicula getPelicula() {
        return pelicula;
    }

    /**
     * Asocia una película a la proyección.
     * @param pelicula Película a asociar
     */
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
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
}
