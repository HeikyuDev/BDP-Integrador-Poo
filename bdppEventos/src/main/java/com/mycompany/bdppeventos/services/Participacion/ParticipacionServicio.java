package com.mycompany.bdppeventos.services.Participacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Participacion;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

/**
 * Servicio para gestionar las participaciones (relaciones persona-evento-rol).
 * Proporciona operaciones CRUD específicas para la entidad Participacion.
 */
public class ParticipacionServicio extends CrudServicio<Participacion> {

    public ParticipacionServicio(Repositorio repositorio) {
        super(repositorio, Participacion.class);
    }

    @Override
    protected boolean estaActivo(Participacion participacion) {
        return participacion.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Participacion participacion) {
        participacion.desactivar();
    }

    // ===== MÉTODOS DE CREACIÓN =====

    /**
     * Crea una nueva participación validando que no exista duplicado.
     * 
     * @param evento Evento en el que participa
     * @param persona Persona que participa
     * @param rol Rol que cumple en el evento
     * @return La participación creada
     * @throws IllegalArgumentException si ya existe la participación o si los datos son inválidos
     */
    public Participacion crearParticipacion(Evento evento, Persona persona, TipoRol rol) {
        // Validaciones
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo");
        }
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }

        // Verificar que no exista ya esta participación
        if (existeParticipacion(evento, persona, rol)) {
            throw new IllegalArgumentException(
                String.format("La persona %s ya tiene el rol %s en el evento %s", 
                    persona.getInformacionPersonal(), rol, evento.getNombre()));
        }

        // Validaciones específicas según el rol
        validarRolEspecifico(evento, rol);

        // Crear la participación
        Participacion participacion = new Participacion(evento, persona, rol);
        this.insertar(participacion);
        
        return participacion;
    }

    /**
     * Crea múltiples participaciones para un evento (útil para organizadores y artistas).
     */
    public List<Participacion> crearParticipaciones(Evento evento, List<Persona> personas, TipoRol rol) {
        List<Participacion> participacionesCreadas = new ArrayList<>();
        
        for (Persona persona : personas) {
            try {
                Participacion participacion = crearParticipacion(evento, persona, rol);
                participacionesCreadas.add(participacion);
            } catch (IllegalArgumentException e) {
                // Log del error pero continúa con las demás personas
                System.err.println("Error al crear participación: " + e.getMessage());
            }
        }
        
        return participacionesCreadas;
    }

    // ===== MÉTODOS DE CONSULTA =====

    /**
     * Busca todas las participaciones de un evento específico.
     */
    public List<Participacion> buscarPorEvento(Evento evento) {
        List<Participacion> todasLasParticipaciones = buscarTodos();
        return todasLasParticipaciones.stream()
                .filter(p -> p.getEvento().getId() == evento.getId() && p.getActivo())
                .collect(Collectors.toList());
    }

    /**
     * Busca todas las participaciones de una persona específica.
     */
    public List<Participacion> buscarPorPersona(Persona persona) {
        List<Participacion> todasLasParticipaciones = buscarTodos();
        return todasLasParticipaciones.stream()
                .filter(p -> p.getPersona().getDni().equals(persona.getDni()) && p.getActivo())
                .collect(Collectors.toList());
    }

    /**
     * Busca participaciones por evento y rol específico.
     */
    public List<Participacion> buscarPorEventoYRol(Evento evento, TipoRol rol) {
        return buscarPorEvento(evento).stream()
                .filter(p -> p.getRolEnEvento().equals(rol))
                .collect(Collectors.toList());
    }

    /**
     * Busca participaciones por persona y rol específico.
     */
    public List<Participacion> buscarPorPersonaYRol(Persona persona, TipoRol rol) {
        return buscarPorPersona(persona).stream()
                .filter(p -> p.getRolEnEvento().equals(rol))
                .collect(Collectors.toList());
    }

    /**
     * Busca una participación específica por evento, persona y rol.
     */
    public Participacion buscarParticipacionEspecifica(Evento evento, Persona persona, TipoRol rol) {
        List<Participacion> todasLasParticipaciones = buscarTodos();
        return todasLasParticipaciones.stream()
                .filter(p -> p.getEvento().getId() == evento.getId() 
                        && p.getPersona().getDni().equals(persona.getDni())
                        && p.getRolEnEvento().equals(rol)
                        && p.getActivo())
                .findFirst()
                .orElse(null);
    }

    // ===== MÉTODOS DE VERIFICACIÓN =====

    /**
     * Verifica si existe una participación específica.
     */
    public boolean existeParticipacion(Evento evento, Persona persona, TipoRol rol) {
        return buscarParticipacionEspecifica(evento, persona, rol) != null;
    }

    /**
     * Verifica si una persona participa en un evento (cualquier rol).
     */
    public boolean personaParticipaEnEvento(Persona persona, Evento evento) {
        return buscarPorEvento(evento).stream()
                .anyMatch(p -> p.getPersona().getDni().equals(persona.getDni()));
    }

    /**
     * Cuenta cuántas personas tienen un rol específico en un evento.
     */
    public int contarPersonasPorRol(Evento evento, TipoRol rol) {
        return buscarPorEventoYRol(evento, rol).size();
    }

    // ===== MÉTODOS DE MODIFICACIÓN =====

    /**
     * Cambia el rol de una persona en un evento específico.
     */
    public boolean cambiarRol(Evento evento, Persona persona, TipoRol rolAnterior, TipoRol rolNuevo) {
        Participacion participacion = buscarParticipacionEspecifica(evento, persona, rolAnterior);
        
        if (participacion == null) {
            throw new IllegalArgumentException("No se encontró la participación a modificar");
        }

        // Verificar que el nuevo rol no exista ya
        if (existeParticipacion(evento, persona, rolNuevo)) {
            throw new IllegalArgumentException("La persona ya tiene el rol " + rolNuevo + " en este evento");
        }

        // Validar el nuevo rol
        validarRolEspecifico(evento, rolNuevo);

        // Cambiar el rol
        participacion.setRolEnEvento(rolNuevo);
        this.modificar(participacion);
        
        return true;
    }

    /**
     * Elimina una participación específica (baja lógica).
     */
    public boolean eliminarParticipacion(Evento evento, Persona persona, TipoRol rol) {
        Participacion participacion = buscarParticipacionEspecifica(evento, persona, rol);
        
        if (participacion == null) {
            return false; // No existe la participación
        }

        // Validar que se pueda eliminar (por ejemplo, no eliminar el último organizador)
        if (rol == TipoRol.ORGANIZADOR && contarPersonasPorRol(evento, TipoRol.ORGANIZADOR) <= 1) {
            throw new IllegalArgumentException("No se puede eliminar el último organizador del evento");
        }

        // Marcar como inactivo (baja lógica)
        marcarComoInactivo(participacion);

        // Guardar cambios en la base (update)
        modificar(participacion);        
        return true;
    }

    /**
     * Elimina todas las participaciones de una persona en un evento.
     */
    public int eliminarTodasLasParticipaciones(Evento evento, Persona persona) {
        List<Participacion> participaciones = buscarPorEvento(evento).stream()
                .filter(p -> p.getPersona().getDni().equals(persona.getDni()))
                .collect(Collectors.toList());

        int eliminadas = 0;
        for (Participacion participacion : participaciones) {
            // Validar si se puede eliminar cada rol
            try {
                if (participacion.getRolEnEvento() != TipoRol.ORGANIZADOR || 
                    contarPersonasPorRol(evento, TipoRol.ORGANIZADOR) > 1) {
                    // Marcar como inactivo (baja lógica)
                    marcarComoInactivo(participacion);

                    // Guardar cambios en la base (update)
                    modificar(participacion);
                    eliminadas++;
                }
            } catch (Exception e) {
                System.err.println("Error al eliminar participación: " + e.getMessage());
            }
        }

        return eliminadas;
    }

    // ===== MÉTODOS DE UTILIDAD =====

    /**
     * Obtiene todas las personas con un rol específico en un evento.
     */
    public List<Persona> obtenerPersonasPorRol(Evento evento, TipoRol rol) {
        return buscarPorEventoYRol(evento, rol).stream()
                .map(Participacion::getPersona)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los eventos donde una persona tiene un rol específico.
     */
    public List<Evento> obtenerEventosPorPersonaYRol(Persona persona, TipoRol rol) {
        return buscarPorPersonaYRol(persona, rol).stream()
                .map(Participacion::getEvento)
                .collect(Collectors.toList());
    }

    // ===== MÉTODOS DE VALIDACIÓN ESPECÍFICA =====

    /**
     * Valida reglas específicas según el rol que se quiere asignar.
     */
    private void validarRolEspecifico(Evento evento, TipoRol rol) {
        switch (rol) {
            case CURADOR -> {
                // Un evento solo puede tener un curador
                long curadoresActuales = contarPersonasPorRol(evento, TipoRol.CURADOR);
                if (curadoresActuales >= 1) {
                    throw new IllegalArgumentException("El evento ya tiene un curador asignado");
                }
            }
            case INSTRUCTOR -> {
                // Un taller solo puede tener un instructor
                long instructoresActuales = contarPersonasPorRol(evento, TipoRol.INSTRUCTOR);
                if (instructoresActuales >= 1) {
                    throw new IllegalArgumentException("El evento ya tiene un instructor asignado");
                }
            }
            case ORGANIZADOR -> {
                // Los organizadores no tienen límite específico, pero debe haber al menos uno
                // Esta validación se hace al eliminar, no al crear
            }
            case ARTISTA -> {
                // Los artistas pueden ser múltiples, sin restricciones especiales
            }
            case PARTICIPANTE -> {
                // Validar cupo si el evento tiene límite
                if (evento.isTieneCupo()) {
                    long participantesActuales = contarPersonasPorRol(evento, TipoRol.PARTICIPANTE);
                    if (participantesActuales >= evento.getCapacidadMaxima()) {
                        throw new IllegalArgumentException("El evento ha alcanzado su capacidad máxima");
                    }
                }
            }
        }
    }

    // ===== MÉTODOS ESPECÍFICOS PARA LA INTERFAZ =====

    /**
     * Método especializado para registrar organizadores de un evento.
     * Se usa típicamente desde el formulario de alta de eventos.
     */
    public List<Participacion> registrarOrganizadores(Evento evento, List<Persona> organizadores) {
        if (organizadores == null || organizadores.isEmpty()) {
            throw new IllegalArgumentException("Un evento debe tener al menos un organizador");
        }
        
        return crearParticipaciones(evento, organizadores, TipoRol.ORGANIZADOR);
    }

    /**
     * Método especializado para registrar el curador de una exposición.
     */
    public Participacion registrarCurador(Evento evento, Persona curador) {
        return crearParticipacion(evento, curador, TipoRol.CURADOR);
    }

    /**
     * Método especializado para registrar el instructor de un taller.
     */
    public Participacion registrarInstructor(Evento evento, Persona instructor) {
        return crearParticipacion(evento, instructor, TipoRol.INSTRUCTOR);
    }

    /**
     * Método especializado para registrar artistas de un concierto.
     */
    public List<Participacion> registrarArtistas(Evento evento, List<Persona> artistas) {
        if (artistas == null || artistas.isEmpty()) {
            throw new IllegalArgumentException("Un concierto debe tener al menos un artista");
        }
        
        return crearParticipaciones(evento, artistas, TipoRol.ARTISTA);
    }

    /**
     * Método para inscribir un participante a un evento.
     * Valida capacidad y estado del evento.
     */
    public Participacion inscribirParticipante(Evento evento, Persona persona) {
        // Validar que el evento permita inscripciones
        if (!evento.isTieneInscripcion()) {
            throw new IllegalArgumentException("Este evento no requiere inscripción previa");
        }

        // Validar estado del evento
        switch (evento.getEstado()) {
            case  CONFIRMADO, EN_EJECUCION -> {
                // Permitir inscripción
            }
            case PLANIFICADO, FINALIZADO, CANCELADO -> {
                throw new IllegalArgumentException("No se pueden inscribir participantes a un evento " + 
                                                 evento.getEstado().toString().toLowerCase());
            }
        }

        return crearParticipacion(evento, persona, TipoRol.PARTICIPANTE);
    }
 
    public void eliminarTodasLasParticipacionesDelEvento(Evento evento) {
        List<Participacion> participaciones = buscarPorEvento(evento);        

        for (Participacion participacion : participaciones) {
            if (participacion.getActivo()) {
                // Marcar como inactivo (baja lógica)
                marcarComoInactivo(participacion);

                // Guardar cambios en la base (update)
                modificar(participacion);                
            }
        }        
    }
                   
}