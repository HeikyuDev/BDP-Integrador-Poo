package com.mycompany.bdppeventos.model.entities;

import com.mycompany.bdppeventos.model.interfaces.Activable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "personas")
public class Persona implements Activable {

    // Clave Primaria
    @Id
    @Column(name = "dni", length = 15, nullable = false)
    private String dni;

    // Atributos Simples    
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 35, nullable = false)
    private String apellido;

    @Column(name = "telefono", length = 15, nullable = true)
    private String telefono;

    @Column(name = "correo_electronico", length = 50, nullable = true)
    private String correoElectronico;

    @OneToMany(mappedBy = "unaPersona")    
    private List<Participacion> unaListaParticipacion;
    
    
    // Variable que verifica si la Persona esta en baja o esta activa
    @Column(name = "activo")
    private Boolean activo;

    // Constructores 
    public Persona() {

        // Al crear una nueva instancia siempre va a estar activo (Hasta que se de la baja)
        this.activo = true;
    }

    public Persona(String dni, String nombre, String apellido, String telefono, String correoElectronico, List<Participacion> unaListaParticipacion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.unaListaParticipacion = unaListaParticipacion;
        this.activo = true;
    }
    
    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("DNI no puede estar vacío");
        }        
        if(dni.trim().length() > 15)
        {
            throw new IllegalArgumentException("Dni no puede exceder los 15 caracteres");
        }
        // Validar que solo contenga números
        if (!dni.trim().matches("\\d+")) {
            throw new IllegalArgumentException("DNI debe contener solo números");
        }
        this.dni = dni.trim();
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede estar vacío");
        }
        if (apellido.trim().length() > 35) {
            throw new IllegalArgumentException("Apellido no puede exceder 35 caracteres");
        }
        this.apellido = apellido.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        // Teléfono es opcional, pero si viene debe ser válido
        if (telefono != null && !telefono.trim().isEmpty()) {            
            if (telefono.trim().length() > 15) {
                throw new IllegalArgumentException("Teléfono no puede exceder 15 caracteres");
            }
            //validar formato (solo números, espacios, guiones)
            if (!telefono.trim().matches("[0-9 \\-+()]+")) {
                throw new IllegalArgumentException("Teléfono solo puede tener números, espacios, guiones, + y paréntesis");
            }
            this.telefono = telefono.trim();
        } else {
            this.telefono = null;  // Si viene vacío, guardar como null
        }
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        // Email es opcional, pero si viene debe ser válido
        if (correoElectronico != null && !correoElectronico.trim().isEmpty()) {
            
            if (correoElectronico.trim().length() > 50) {
                throw new IllegalArgumentException("Email no puede exceder 50 caracteres");
            }
            if (!esEmailValido(correoElectronico.trim())) {
                throw new IllegalArgumentException("Formato de email inválido");
            }
            this.correoElectronico = correoElectronico.trim();
        } else {
            this.correoElectronico = null;  // Si viene vacío, guardar como null
        }
    }
       
    public List<Participacion> getUnaListaParticipacion() {
        return unaListaParticipacion;
    }

    public void setUnaListaParticipacion(List<Participacion> unaListaParticipacion) {
        this.unaListaParticipacion = unaListaParticipacion;
    }

    
    
    // Metodos Específicos
    private boolean esEmailValido(String email) {

        if (email == null) {
            return false;
        }

        // Validaciones básicas: debe tener @ y al menos un punto después del @
        if (!email.contains("@")) {
            return false;
        }

        String[] partes = email.split("@");
        if (partes.length != 2) {
            return false;
        }

        String dominio = partes[1];
        if (!dominio.contains(".")) {
            return false;
        }
        return true;
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

    @Override
    public String toString() {
        return "Persona{" + "dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + ", correoElectronico=" + correoElectronico + ", activo=" + activo + '}';
    }

    // Informacion que hay que cargar en los listBox
    public String getInformacionPersonal() {
        return this.apellido + " " + this.nombre + "|DNI: " + this.dni;
    }

}
