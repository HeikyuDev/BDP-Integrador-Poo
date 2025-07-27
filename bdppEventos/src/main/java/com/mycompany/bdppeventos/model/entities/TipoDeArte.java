package com.mycompany.bdppeventos.model.entities;

import java.util.List;

import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa un Tipo de Arte en el sistema.
 * Un tipo de arte puede estar asociado a muchas exposiciones.
 * Implementa la interfaz Activable para permitir activar/desactivar el tipo de arte.
 */
@Entity
@Table(name = "tipos_de_arte")
public class TipoDeArte implements Activable{

    // Atributos

    /** Identificador único del tipo de arte (clave primaria) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTipoArte;

    /** Nombre del tipo de arte (no nulo, máx 35 caracteres) */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /** Indica si el tipo de arte está activo (lógico, para borrado suave) */
    @Column(name = "activo", nullable = false)
    private boolean activo;

    /**
     * Relación uno a muchos: un tipo de arte puede estar asociado a muchas exposiciones
     */
    @OneToMany(mappedBy = "unTipoArte")    
    private List<Exposicion> unaListaExposicion;
    // Constructores

    /**
     * Constructor por defecto. Marca el tipo de arte como activo.
     */
    public TipoDeArte() {
        this.activo = true;
    }

    /**
     * Constructor completo para inicializar todos los campos del tipo de arte.
     * @param idTipoArte Identificador único
     * @param nombre Nombre del tipo de arte
     * @param activo Estado del tipo de arte
     * @param unaListaExposicion Lista de exposiciones asociadas
     */
    public TipoDeArte(int idTipoArte, String nombre, boolean activo, List<Exposicion> unaListaExposicion) {
        this.idTipoArte = idTipoArte;
        this.nombre = nombre;
        this.activo = true;
        this.unaListaExposicion = unaListaExposicion;
    }
    
    // Getters y Setters

    /**
     * Devuelve el identificador único del tipo de arte.
     */
    public int getIdTipoArte() {
        return idTipoArte;
    }

    /**
     * Devuelve el nombre del tipo de arte.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del tipo de arte, validando que no sea nulo ni exceda 35 caracteres.
     * @param nombre Nombre del tipo de arte
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 35) {
            throw new IllegalArgumentException("El nombre no puede exceder los 35 caracteres");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Devuelve la lista de exposiciones asociadas a este tipo de arte.
     */
    public List<Exposicion> getUnaListaExposicion() {
        return unaListaExposicion;
    }

    /**
     * Asigna la lista de exposiciones asociadas a este tipo de arte.
     * @param unaListaExposicion Lista de exposiciones
     */
    public void setUnaListaExposicion(List<Exposicion> unaListaExposicion) {
        this.unaListaExposicion = unaListaExposicion;
    }
    
    // Métodos de la interfaz Activable

    /**
     * Activa el tipo de arte (lo marca como activo).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva el tipo de arte (lo marca como inactivo).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si el tipo de arte está activo.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo del tipo de arte. No permite null.
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
    
    // To String

    /**
     * Devuelve el nombre del tipo de arte como representación en texto.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}
