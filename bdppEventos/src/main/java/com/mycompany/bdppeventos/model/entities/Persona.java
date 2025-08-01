package com.mycompany.bdppeventos.model.entities;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.model.interfaces.Activable;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.stream.Collectors;

/**
 * Clase que representa una persona en el sistema. Incluye datos personales,
 * información de contacto y estado de actividad. Implementa la interfaz
 * Activable para permitir activar/desactivar la persona.
 */
@Entity
@Table(name = "personas")
public class Persona implements Activable {

    /**
     * DNI de la persona (clave primaria, máximo 15 caracteres)
     */
    @Id
    @Column(name = "dni", length = 15, nullable = false)
    private String dni;

    /**
     * Nombre de la persona (máximo 35 caracteres, no nulo)
     */
    @Column(name = "nombre", length = 35, nullable = false)
    private String nombre;

    /**
     * Apellido de la persona (máximo 35 caracteres, no nulo)
     */
    @Column(name = "apellido", length = 35, nullable = false)
    private String apellido;

    /**
     * Teléfono de contacto (opcional, máximo 15 caracteres)
     */
    @Column(name = "telefono", length = 15, nullable = true)
    private String telefono;

    /**
     * Correo electrónico de contacto (opcional, máximo 50 caracteres)
     */
    @Column(name = "correo_electronico", length = 50, nullable = true)
    private String correoElectronico;

    /**
     * Indica si la persona está activa (true) o dada de baja (false)
     */
    @Column(name = "activo")
    private Boolean activo;

    /**
     * Una persona puede tener uno o mas roles asignados
     */
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private List<TipoRol> unaListaRoles;

    @OneToMany(mappedBy = "unaPersona")
    private List<Participacion> participaciones;

    // Constructores
    /**
     * Constructor por defecto. Marca la persona como activa e inicializa roles.
     */
    public Persona() {
        // Al crear una nueva instancia siempre va a estar activo (Hasta que se de la
        // baja)
        this.activo = true;
        // Inicializar lista de roles con rol por defecto
        this.unaListaRoles = new ArrayList<>();
        this.unaListaRoles.add(TipoRol.getRolPorDefecto());
    }

    /**
     * Constructor para crear una persona con los datos básicos (DNI, nombre,
     * apellido). Los campos opcionales (teléfono, correo) se pueden dejar como
     * null.
     *
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

    // Getters y Setters
    /**
     * Devuelve el DNI de la persona.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Asigna el DNI, validando que no sea nulo, vacío, que no exceda 15
     * caracteres y que contenga solo números.
     *
     * @param dni DNI de la persona
     */
    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("DNI no puede estar vacío");
        }
        if (dni.trim().length() > 15) {
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
     * Asigna el nombre, validando que no sea nulo, vacío ni exceda 35
     * caracteres.
     *
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
     * Asigna el apellido, validando que no sea nulo, vacío ni exceda 35
     * caracteres.
     *
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
     * Asigna el teléfono de contacto, validando formato y longitud si no es
     * vacío.
     *
     * @param telefono Teléfono de contacto
     */
    public void setTelefono(String telefono) {
        // Teléfono es opcional, pero si viene debe ser válido
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (telefono.trim().length() > 15) {
                throw new IllegalArgumentException("Teléfono no puede exceder 15 caracteres");
            }
            // validar formato (solo números, espacios, guiones)
            if (!telefono.trim().matches("[0-9 \\-+()]+")) {
                throw new IllegalArgumentException(
                        "Teléfono solo puede tener números, espacios, guiones, + y paréntesis");
            }
            this.telefono = telefono.trim();
        } else {
            this.telefono = null; // Si viene vacío, guardar como null
        }
    }

    /**
     * Devuelve el correo electrónico de la persona.
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Asigna el correo electrónico, validando formato y longitud si no es
     * vacío.
     *
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
            this.correoElectronico = null; // Si viene vacío, guardar como null
        }
    }

    /**
     * Devuelve todas las participaciones de la persona.
     */
    public List<Participacion> getParticipaciones() {
        return participaciones;
    }

    /**
     * Asigna la lista de participaciones.
     */
    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }

    /**
     * Devuelve la lista de roles asociados a la persona.
     */
    public List<TipoRol> getUnaListaRoles() {
        return unaListaRoles;
    }

    /**
     * Asigna la lista de roles asociados a la persona. Si la lista está vacía o
     * es null, asigna el rol por defecto.
     *
     * @param unaListaRoles Lista de roles
     */
    public void setUnaListaRoles(List<TipoRol> unaListaRoles) {
        if (unaListaRoles == null || unaListaRoles.isEmpty()) {
            // Si no se proporcionan roles, asignar rol por defecto
            this.unaListaRoles = new ArrayList<>();
            this.unaListaRoles.add(TipoRol.getRolPorDefecto());
        } else {
            this.unaListaRoles = unaListaRoles;
        }
    }

    /**
     * Agrega un rol a la persona sin quitar los existentes.
     *
     * @param rol Rol a agregar
     */
    public void agregarRol(TipoRol rol) {
        if (this.unaListaRoles == null) {
            this.unaListaRoles = new ArrayList<>();
        }
        if (!this.unaListaRoles.contains(rol)) {
            this.unaListaRoles.add(rol);
        }
    }

    /**
     * Quita un rol de la persona. Si queda sin roles, asigna el rol por
     * defecto.
     *
     * @param rol Rol a quitar
     */
    public void quitarRol(TipoRol rol) {
        if (this.unaListaRoles != null) {
            this.unaListaRoles.remove(rol);
            // Si queda sin roles, asignar rol por defecto
            if (this.unaListaRoles.isEmpty()) {
                this.unaListaRoles.add(TipoRol.getRolPorDefecto());
            }
        }
    }

    /**
     * Verifica si la persona tiene un rol específico.
     *
     * @param rol Rol a verificar
     * @return true si tiene el rol, false si no
     */
    public boolean tieneRol(TipoRol rol) {
        return this.unaListaRoles != null && this.unaListaRoles.contains(rol);
    }

    // Métodos específicos
    /**
     * Valida el formato básico de un correo electrónico. Debe contener un @ y
     * al menos un punto después del @.
     *
     * @param email Correo electrónico a validar
     * @return true si es válido, false si no
     */
    private boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailTrimmed = email.trim();

        // Validaciones básicas: debe tener @ y al menos un punto después del @
        if (!emailTrimmed.contains("@")) {
            return false;
        }

        String[] partes = emailTrimmed.split("@");
        if (partes.length != 2) {
            return false;
        }

        String usuario = partes[0];
        String dominio = partes[1];

        // El usuario y dominio no pueden estar vacíos
        if (usuario.isEmpty() || dominio.isEmpty()) {
            return false;
        }

        // El dominio debe tener al menos un punto
        if (!dominio.contains(".")) {
            return false;
        }

        // Verificar que no termine o empiece con punto
        if (dominio.startsWith(".") || dominio.endsWith(".")) {
            return false;
        }

        return true;
    }

    /**
     * Agrega una participación a la persona.
     */
    public void agregarParticipacion(Participacion participacion) {
        if (this.participaciones == null) {
            this.participaciones = new ArrayList<>();
        }
        this.participaciones.add(participacion);
        participacion.setPersona(this);
    }

    /**
     * Quita una participación de la persona.
     */
    public void quitarParticipacion(Participacion participacion) {
        if (this.participaciones != null) {
            this.participaciones.remove(participacion);
            participacion.setPersona(null);
        }
    }

    /**
     * Obtiene todos los eventos donde la persona tiene un rol específico.
     */
    public List<Evento> getEventosPorRol(TipoRol rol) {
        if (this.participaciones == null) {
            return new ArrayList<>();
        }

        return this.participaciones.stream()
                .filter(p -> p.getRolEnEvento().equals(rol) && p.getActivo())
                .map(Participacion::getEvento)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los eventos donde la persona es organizadora.
     */
    public List<Evento> getEventosComoOrganizadora() {
        return getEventosPorRol(TipoRol.ORGANIZADOR);
    }

    /**
     * Obtiene todos los eventos donde la persona es artista.
     */
    public List<Evento> getEventosComoArtista() {
        return getEventosPorRol(TipoRol.ARTISTA);
    }

    /**
     * Obtiene todos los eventos donde la persona es curadora.
     */
    public List<Evento> getEventosComoCuradora() {
        return getEventosPorRol(TipoRol.CURADOR);
    }

    /**
     * Obtiene todos los eventos donde la persona es instructora.
     */
    public List<Evento> getEventosComoInstructora() {
        return getEventosPorRol(TipoRol.INSTRUCTOR);
    }

    /**
     * Verifica si la persona participa en un evento específico con un rol
     * determinado.
     */
    public boolean participaEnEventoConRol(Evento evento, TipoRol rol) {
        if (this.participaciones == null) {
            return false;
        }

        return this.participaciones.stream()
                .anyMatch(p -> p.getEvento().equals(evento)
                && p.getRolEnEvento().equals(rol)
                && p.getActivo());
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
     *
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
        return this.nombre + " " + this.apellido + " DNI: " + this.dni;
    }

    /**
     * Devuelve información personal resumida para mostrar en listados.
     *
     * @return Cadena con apellido, nombre y DNI
     */
    public String getInformacionPersonal() {
        return this.apellido + " " + this.nombre + " DNI: " + this.dni;
    }

}
