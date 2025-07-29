package com.mycompany.bdppeventos.services.Persona;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

/**
 * PersonaServicio es una clase que extiende CrudServicio para manejar
 * operaciones CRUD
 * específicas relacionadas con la entidad Persona.
 * Su funcion principal es validar los datos de una persona y realizar
 * operaciones.
 */

public class PersonaServicio extends CrudServicio<Persona> {

    public PersonaServicio(Repositorio repositorio) {
        super(repositorio, Persona.class);
    }

    // Método para validar y crear una nueva persona
    public Persona validarEInsertar(Object... datos) {
        if (datos.length < 3 || datos.length > 5) {
            throw new IllegalArgumentException(
                    "Número incorrecto de parámetros. Se requieren: DNI, nombre, apellido y opcionalmente teléfono y correo.");
        }

        String dni = (String) datos[0];
        String nombre = (String) datos[1];
        String apellido = (String) datos[2];
        // campos opcionales
        String telefono = datos.length > 3 ? (String) datos[3] : null; // operador ternario, si es verdadero, asigna el
                                                                       // valor, si es falso, asigna null
        String correoElectronico = datos.length > 4 ? (String) datos[4] : null; // Si el correo electrónico no se
                                                                                // proporciona, se asigna null

        List<String> errores = new ArrayList<>();

        // Validar que el DNI no sea nulo o vacío
        if (buscarPorId(dni) != null) {
            errores.add("El DNI ingresado ya está en uso. Por favor, ingrese otro DNI.");
        }

        Persona nuevaPersona = null; // declaro como null para evitar errores
        try {
            nuevaPersona = new Persona(dni, nombre, apellido, telefono, correoElectronico);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errores));
        }

        insertar(nuevaPersona);
        return nuevaPersona;
    }

    // Método para validar y modificar una persona existente
    public void validarYModificar(Persona persona, Object... datos) {
        if (datos.length < 2 || datos.length > 5) {
            throw new IllegalArgumentException(
                    "Número incorrecto de parámetros. Se requieren: nombre, apellido y opcionalmente teléfono y correo.");
        }

        String nombre = (String) datos[0];
        String apellido = (String) datos[1];
        String telefono = datos.length > 2 ? (String) datos[2] : null;
        String correoElectronico = datos.length > 3 ? (String) datos[3] : null;

        Persona aux = new Persona(); // para llenar los campos con los setters
        List<String> errores = new ArrayList<>();

        // validaciones para saber si los datos son correctos
        try {
            aux.setNombre(nombre);
        } catch (IllegalArgumentException e) {
            errores.add("Nombre inválido: " + e.getMessage());
        }

        try {
            aux.setApellido(apellido);
        } catch (IllegalArgumentException e) {
            errores.add("Apellido inválido: " + e.getMessage());
        }

        try {
            aux.setTelefono(telefono);
        } catch (Exception e) {
            errores.add("Teléfono inválido: " + e.getMessage());
        }

        try {
            aux.setCorreoElectronico(correoElectronico);
        } catch (Exception e) {
            errores.add("Correo electrónico inválido: " + e.getMessage());
        }

        // Si hay errores, lanzamos una excepción con todos los mensajes
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errores));
        }

        // Asignar los valores validados a la persona
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTelefono(telefono);
        persona.setCorreoElectronico(correoElectronico);

        if (persona.getUnaListaRoles() == null || persona.getUnaListaRoles().isEmpty()) {
            List<TipoRol> roles = new ArrayList<>();
            roles.add(TipoRol.getRolPorDefecto());
            persona.setUnaListaRoles(roles);
        }

        // Guardar cambios en la base
        modificar(persona);

    }

    // Método para dar de baja una persona
    public void validarYBorrar(Persona persona) {
        if (buscarPorId(persona.getDni()) == null) {
            throw new IllegalArgumentException("La persona no existe.");
        }
        borrar(persona);
    }

    // Métodos abstractos requeridos por CrudServicio
    @Override
    protected boolean estaActivo(Persona persona) {
        return persona != null && persona.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Persona persona) {
        if (persona != null) {
            persona.setActivo(false);
        }
    }

    // Metodos requeridos para Eventos

    public List<Persona> buscarOrganizadores() {
        return buscarTodos().stream()
                .filter(p -> p.getUnaListaRoles().contains(TipoRol.ORGANIZADOR))
                .toList();
    }

    public List<Persona> buscarCuradores() {
        return buscarTodos().stream()
                .filter(p -> p.getUnaListaRoles().contains(TipoRol.CURADOR))
                .toList();
    }

    public List<Persona> buscarInstructores() {
        return buscarTodos().stream()
                .filter(p -> p.getUnaListaRoles().contains(TipoRol.INSTRUCTOR))
                .toList();
    }

    public List<Persona> buscarArtistas() {
        return buscarTodos().stream()
                .filter(p -> p.getUnaListaRoles().contains(TipoRol.ARTISTA))
                .toList();
    }

}
