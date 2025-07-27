package com.mycompany.bdppeventos.services.Persona;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

/**
 * PersonaServicio es una clase que extiende CrudServicio para manejar operaciones CRUD
 * específicas relacionadas con la entidad Persona.
 * Su funcion principal es validar los datos de una persona y realizar operaciones.
 */

public class PersonaServicio extends CrudServicio<Persona> {
    
    public PersonaServicio(Repositorio repositorio) {
        super(repositorio, Persona.class);
    }

    public void altaPersona(String nombre, String apellido, String email) {
        // Implementación del método para dar de alta una nueva persona
        // Aquí se llamaría al repositorio para insertar una nueva persona
    }

    public void modificarPersona(Integer id, String nombre, String apellido, String email) {
        // Implementación del método para modificar una persona existente
        // Aquí se llamaría al repositorio para actualizar los datos de la persona
    }
    public void bajaPersona(Integer id) {
        // Implementación del método para dar de baja una persona
        // Aquí se llamaría al repositorio para marcar la persona como inactiva
    }

    // Método para validar y crear una nueva persona
    public Persona validarEInsertar(Object... datos) {
        if (datos.length < 3 || datos.length > 5) {
            throw new IllegalArgumentException("Número incorrecto de parámetros. Se requieren: DNI, nombre, apellido y opcionalmente teléfono y correo.");
        }

        String dni = (String) datos[0];
        String nombre = (String) datos[1];
        String apellido = (String) datos[2];
        // campos opcionales
        String telefono = datos.length > 3 ? (String) datos[3] : null; // operador ternario, si es verdadero, asigna el valor, si es falso, asigna null
        String correoElectronico = datos.length > 4 ? (String) datos[4] : null; // Si el correo electrónico no se proporciona, se asigna null

        List<String> errores = new ArrayList<>();

        // Validar que el DNI no sea nulo o vacío
        if (buscarPorId(dni) != null) {
            errores.add("El DNI ingresado ya está en uso. Por favor, ingrese otro DNI.");
        }

        Persona nuevaPersona = null; // declaro como null para evitar errores 
        try{
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

}
