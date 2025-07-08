package com.mycompany.bdppeventos.model.entities;
import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;




@Entity
@Table(name = "tipo_roles")
public class TipoRol implements Activable{
    
    @Id
    @Column(name = "id_tiporol", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTipoRol;
    
    @Column(name = "nombrerol", length = 35, nullable = false)
    private String nombreRol;
    
    @Column(name = "activo")
    private boolean activo;
    
    //Contructores

    public TipoRol() {
    }

    public TipoRol(int idTipoRol, String nombreRol, boolean activo) {
        this.idTipoRol = idTipoRol;
        this.nombreRol = nombreRol;
        this.activo = activo;
    }
    
    // Getters y Setters
    
    public int getIdTipoRol() {
        return idTipoRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    
    public void setNombreRol(String nombreRol) {
    if (nombreRol == null || nombreRol.trim().isEmpty()) {
        throw new IllegalArgumentException("Nombre del rol no puede estar vacío");
    }
    
    String nombreLimpio = nombreRol.trim();
    
    if (nombreLimpio.length() > 35) {  // Ajusta según tu @Column
        throw new IllegalArgumentException("Nombre del rol no puede exceder 35 caracteres");
    }
    
    // Validar que solo contenga letras y espacios
    if (!nombreLimpio.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        throw new IllegalArgumentException("Nombre del rol solo puede contener letras y espacios");
    }
    
    this.nombreRol = nombreLimpio;
}

    // Metodos de la interfaz Activable
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
