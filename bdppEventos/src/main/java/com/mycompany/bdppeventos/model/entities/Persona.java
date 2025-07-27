package com.mycompany.bdppeventos.model.entities;

import java.util.List;

import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa una persona en el sistema.
 * Incluye datos personales, información de contacto y estado de actividad.
 * Implementa la interfaz Activable para permitir activar/desactivar la persona.
 */
@Entity
@Table(name = "personas")
public class Persona implements Activable {


    /** DNI de la persona (clave primaria, máximo 15 caracteres) */
    @Id
    @Column(name = "dni", length = 15, nullable = false)
    private String dni;


    /** Nombre de la persona (máximo 35 caracteres, no nulo) */
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;


    /** Apellido de la persona (máximo 35 caracteres, no nulo) */
    @Column(name = "apellido", length = 35, nullable = false)
    private String apellido;


    /** Teléfono de contacto (opcional, máximo 15 caracteres) */
    @Column(name = "telefono", length = 15, nullable = true)
    private String telefono;


    /** Correo electrónico de contacto (opcional, máximo 50 caracteres) */
    @Column(name = "correo_electronico", length = 50, nullable = true)
    private String correoElectronico;


    /**
     * Relación uno a muchos: una persona puede tener muchas participaciones
     * (eventos en los que participa)
     */
    @OneToMany(mappedBy = "unaPersona")    
    private List<Participacion> unaListaParticipacion;
    
    

    /** Indica si la persona está activa (true) o dada de baja (false) */
    @Column(name = "activo")
    private Boolean activo;


    // Constructores

    /**
     * Constructor por defecto. Marca la persona como activa.
     */
    public Persona() {
        // Al crear una nueva instancia siempre va a estar activo (Hasta que se de la baja)
        this.activo = true;
    }

 /**
     * Constructor para crear una persona con los datos básicos (DNI, nombre, apellido).
     * Los campos opcionales (teléfono y correo) se pueden dejar como null.
     * @param dni DNI de la persona
     * @param nombre Nombre
     * @param apellido Apellido
     * @param telefono Teléfono de contacto (opcional)
     * @param correoElectronico Correo electrónico (opcional)
     */
    public Persona(String dni, String nombre, String apellido, String telefono, String correoElectronico) {
        this(); // Llama al constructor por defecto para inicializar activo
        setDni(dni); // Usa el setter para validar el DNI
        setNombre(nombre); // Usa el setter para validar el nombre
        setApellido(apellido); // Usa el setter para validar el apellido
        setTelefono(telefono); // Usa el setter para validar el teléfono
        setCorreoElectronico(correoElectronico); // Usa el setter para validar el correo electrónico
    }

    /**
     * Constructor completo para asignar una persona a una lista de participaciones.
     * @param dni DNI de la persona
     * @param nombre Nombre
     * @param apellido Apellido
     * @param telefono Teléfono de contacto
     * @param correoElectronico Correo electrónico
     * @param unaListaParticipacion Lista de participaciones
     */
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

    /**
     * Devuelve el DNI de la persona.
     */
    public String getDni() {
        return dni;
    }


    /**
     * Asigna el DNI, validando que no sea nulo, vacío, que no exceda 15 caracteres y que contenga solo números.
     * @param dni DNI de la persona
     */
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


    /**
     * Devuelve el nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }


    /**
     * Asigna el nombre, validando que no sea nulo, vacío ni exceda 35 caracteres.
     * @param nombre Nombre de la persona
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
     * Devuelve el apellido de la persona.
     */
    public String getApellido() {
        return apellido;
    }


    /**
     * Asigna el apellido, validando que no sea nulo, vacío ni exceda 35 caracteres.
     * @param apellido Apellido de la persona
     */
    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede estar vacío");
        }
        if (apellido.trim().length() > 35) {
            throw new IllegalArgumentException("Apellido no puede exceder 35 caracteres");
        }
        this.apellido = apellido.trim();
    }


    /**
     * Devuelve el teléfono de contacto de la persona.
     */
    public String getTelefono() {
        return telefono;
    }


    /**
     * Asigna el teléfono de contacto, validando formato y longitud si no es vacío.
     * @param telefono Teléfono de contacto
     */
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


    /**
     * Devuelve el correo electrónico de la persona.
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }


    /**
     * Asigna el correo electrónico, validando formato y longitud si no es vacío.
     * @param correoElectronico Correo electrónico
     */
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
       

    /**
     * Devuelve la lista de participaciones asociadas a la persona.
     */
    public List<Participacion> getUnaListaParticipacion() {
        return unaListaParticipacion;
    }


    /**
     * Asigna la lista de participaciones asociadas a la persona.
     * @param unaListaParticipacion Lista de participaciones
     */
    public void setUnaListaParticipacion(List<Participacion> unaListaParticipacion) {
        this.unaListaParticipacion = unaListaParticipacion;
    }

    
    

    // Métodos específicos

    /**
     * Valida el formato básico de un correo electrónico.
     * Debe contener un @ y al menos un punto después del @.
     * @param email Correo electrónico a validar
     * @return true si es válido, false si no
     */
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
        if(!dominio.contains(".")) {
            return false;
        }
        return true;
    }


    // Métodos de la interfaz Activable

    /**
     * Activa la persona (la marca como activa).
     */
    @Override
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la persona (la marca como inactiva).
     */
    @Override
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Devuelve si la persona está activa.
     */
    @Override
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Asigna el estado activo/inactivo de la persona. No permite null.
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


    /**
     * Devuelve una representación en texto de la persona.
     */
    @Override
    public String toString() {
        return "Persona{" + "dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + ", correoElectronico=" + correoElectronico + ", activo=" + activo + '}';
    }


    /**
     * Devuelve información personal resumida para mostrar en listados.
     * @return Cadena con apellido, nombre y DNI
     */
    public String getInformacionPersonal() {
        return this.apellido + " " + this.nombre + "|DNI: " + this.dni;
    }

}
