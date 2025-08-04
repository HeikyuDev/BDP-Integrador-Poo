package com.mycompany.bdppeventos.services.Evento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Participacion;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import com.mycompany.bdppeventos.services.Participacion.ParticipacionServicio;
import java.util.Arrays;

/**
 * Servicio para gestionar eventos con métodos específicos para cada tipo
 */
public class EventoServicio extends CrudServicio<Evento> {

    private final ParticipacionServicio participacionServicio;

    public EventoServicio(Repositorio repositorio) {
        super(repositorio, Evento.class);
        this.participacionServicio = new ParticipacionServicio(repositorio);
    }

    @Override
    protected boolean estaActivo(Evento evento) {
        return evento.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Evento evento) {
        evento.desactivar();
    }

    // ===== MÉTODOS DE ALTA ESPECÍFICOS POR TIPO =====

    /**
     * Crea una nueva exposición
     */
    public void altaExposicion(String nombre, String ubicacion, LocalDate fechaInicio, int duracion,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadores,
            TipoDeArte tipoArte, Persona curador) {

        try {
            // 1. Crear la exposición
            Exposicion exposicion = new Exposicion();
            configurarEventoBase(exposicion, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            exposicion.setUnTipoArte(tipoArte);

            // 2. Guardar el evento
            this.insertar(exposicion);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(exposicion, organizadores);
            participacionServicio.registrarCurador(exposicion, curador);
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(exposicion);
            exposicion.setParticipaciones(listaParticipaciones);
            // 4. Actualizar evento para sincronizar relaciones
            this.modificar(exposicion);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la exposición: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un nuevo taller
     */
    public void altaTaller(String nombre, String ubicacion, LocalDate fechaInicio, int duracion,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadores,
            boolean esPresencial, Persona instructor) {

        try {
            // 1. Crear el taller
            Taller taller = new Taller();
            configurarEventoBase(taller, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            taller.setEsPresencial(esPresencial);

            // 2. Guardar el evento
            this.insertar(taller);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(taller, organizadores);
            participacionServicio.registrarInstructor(taller, instructor);
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(taller);
            taller.setParticipaciones(listaParticipaciones);
            // 4. Actualizar evento para sincronizar relaciones
            this.modificar(taller);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el taller: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un nuevo concierto
     */
    public void altaConcierto(String nombre, String ubicacion, LocalDate fechaInicio, int duracion,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadores,
            boolean esPago, double monto, List<Persona> artistas) {

        try {
            // 1. Crear el concierto
            Concierto concierto = new Concierto();
            configurarEventoBase(concierto, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            concierto.setEsPago(esPago);
            if (esPago) {
                concierto.setMonto(monto);
            }

            // 2. Guardar el evento
            this.insertar(concierto);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(concierto, organizadores);
            participacionServicio.registrarArtistas(concierto, artistas);

            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(concierto);
            concierto.setParticipaciones(listaParticipaciones);
            // 4. Actualizar evento para sincronizar relaciones
            this.modificar(concierto);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el concierto: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un nuevo ciclo de cine
     */
    public void altaCicloCine(String nombre, String ubicacion, LocalDate fechaInicio, int duracion,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadores,
            Proyeccion proyeccion, boolean charlasPosteriores) {

        try {
            // 1. Crear el ciclo de cine
            CicloDeCine cicloCine = new CicloDeCine();
            configurarEventoBase(cicloCine, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            cicloCine.setUnaProyeccion(proyeccion);
            cicloCine.setCharlasPosteriores(charlasPosteriores);

            // 2. Guardar el evento
            this.insertar(cicloCine);

            // 3. Crear participaciones (solo organizadores)
            participacionServicio.registrarOrganizadores(cicloCine, organizadores);
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(cicloCine);
            cicloCine.setParticipaciones(listaParticipaciones);
            // 4. Actualizar evento para sincronizar relaciones
            this.modificar(cicloCine);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el ciclo de cine: " + e.getMessage(), e);
        }
    }

    /**
     * Crea una nueva feria
     */
    public void altaFeria(String nombre, String ubicacion, LocalDate fechaInicio, int duracion,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadores,
            int cantidadStands, TipoCobertura tipoCobertura) {

        try {
            // 1. Crear la feria
            Feria feria = new Feria();
            configurarEventoBase(feria, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            feria.setCantidadStands(cantidadStands);
            feria.setTipoCobertura(tipoCobertura);

            // 2. Guardar el evento
            this.insertar(feria);

            // 3. Crear participaciones (solo organizadores)
            participacionServicio.registrarOrganizadores(feria, organizadores);
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(feria);
            feria.setParticipaciones(listaParticipaciones);

            // 4. Actualizar evento para sincronizar relaciones
            this.modificar(feria);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la feria: " + e.getMessage(), e);
        }
    }

    // ===== MÉTODOS DE MODIFICACIÓN ESPECÍFICOS =====

    /**
     * Modifica una exposición existente
     */
    public void modificarExposicion(int eventoId, String nombre, String ubicacion, LocalDate fechaInicio,
            int duracion, boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion,
            List<Persona> nuevosOrganizadores, TipoDeArte tipoArte, Persona nuevoCurador) {

        try {
            Exposicion exposicion = (Exposicion) this.buscarPorId(eventoId);
            if (exposicion == null) {
                throw new IllegalArgumentException("No se encontró la exposición con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(exposicion, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            exposicion.setUnTipoArte(tipoArte);

            // Actualizar participaciones si es necesario
            if (nuevosOrganizadores != null) {
                actualizarOrganizadores(exposicion, nuevosOrganizadores);
            }

            // Actualizar curador si cambió
            Persona curadorActual = exposicion.getCurador();
            if (curadorActual == null || !curadorActual.equals(nuevoCurador)) {
                // Eliminar curador anterior si existe
                if (curadorActual != null) {
                    participacionServicio.eliminarParticipacion(exposicion, curadorActual, TipoRol.CURADOR);
                }
                // Agregar nuevo curador
                participacionServicio.registrarCurador(exposicion, nuevoCurador);
            }

            // Seteamos la lista de participaciones actuales al Evento
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(exposicion);
            exposicion.setParticipaciones(listaParticipaciones);
            // Guardar cambios
            this.modificar(exposicion);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar la exposición: " + e.getMessage(), e);
        }
    }

    /**
     * Modificar un Taller Existente
     */
    public void modificarTaller(int eventoId, String nombre, String ubicacion, LocalDate fechaInicio,
            int duracion, boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion,
            List<Persona> nuevosOrganizadores, boolean esPresencial, Persona nuevoInstructor) {

        try {
            Taller taller = (Taller) this.buscarPorId(eventoId);
            if (taller == null) {
                throw new IllegalArgumentException("No se encontró el taller con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(taller, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            taller.setEsPresencial(esPresencial);

            // Actualizar participaciones si es necesario
            if (nuevosOrganizadores != null) {
                actualizarOrganizadores(taller, nuevosOrganizadores);
            }

            // Actualizar Instructor si cambió
            Persona instructorActual = taller.getInstructor();
            if (instructorActual == null || !instructorActual.equals(nuevoInstructor)) {
                // Eliminar instructor anterior si existe
                if (instructorActual != null) {
                    participacionServicio.eliminarParticipacion(taller, instructorActual, TipoRol.INSTRUCTOR);
                }
                // Agregar nuevo Instructor
                participacionServicio.registrarInstructor(taller, nuevoInstructor);
            }

            // Seteamos la lista de participaciones actuales al Evento
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(taller);
            taller.setParticipaciones(listaParticipaciones);
            // Guardar cambios
            this.modificar(taller);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar el Taller: " + e.getMessage(), e);
        }
    }

    /**
     * Modificar un Concierto Existente
     */
    public void modificarConcierto(int eventoId, String nombre, String ubicacion, LocalDate fechaInicio,
            int duracion, boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion,
            List<Persona> nuevosOrganizadores, boolean esPago, double monto, List<Persona> nuevosArtistas) {

        try {
            Concierto concierto = (Concierto) this.buscarPorId(eventoId);
            if (concierto == null) {
                throw new IllegalArgumentException("No se encontró el concierto con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(concierto, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            concierto.setEsPago(esPago);
            if (esPago) {
                concierto.setMonto(monto);
            }

            // Actualizar participaciones si es necesario
            if (nuevosOrganizadores != null) {
                actualizarOrganizadores(concierto, nuevosOrganizadores);
            }

            // Actualizar Artistas si cambiaron
            List<Persona> artistasActuales = concierto.getArtistas();
            if (nuevosArtistas != null) {
                actualizarArtistas(concierto, nuevosArtistas);
            }

            // Seteamos la lista de participaciones actuales al Evento
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(concierto);
            concierto.setParticipaciones(listaParticipaciones);
            // Guardar cambios
            this.modificar(concierto);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar el Concierto: " + e.getMessage(), e);
        }
    }

    /**
     * Modificar un Ciclo de Cine Existente
     */
    public void modificarCicloCine(int eventoId, String nombre, String ubicacion, LocalDate fechaInicio,
            int duracion, boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion,
            List<Persona> nuevosOrganizadores, Proyeccion proyeccion, boolean charlasPosteriores) {

        try {
            CicloDeCine cicloCine = (CicloDeCine) this.buscarPorId(eventoId);
            if (cicloCine == null) {
                throw new IllegalArgumentException("No se encontró el ciclo de cine con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(cicloCine, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            cicloCine.setUnaProyeccion(proyeccion);
            cicloCine.setCharlasPosteriores(charlasPosteriores);

            // Actualizar participaciones si es necesario
            if (nuevosOrganizadores != null) {
                actualizarOrganizadores(cicloCine, nuevosOrganizadores);
            }

            // Seteamos la lista de participaciones actuales al Evento
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(cicloCine);
            cicloCine.setParticipaciones(listaParticipaciones);
            // Guardar cambios
            this.modificar(cicloCine);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar el Ciclo de Cine: " + e.getMessage(), e);
        }
    }

    /**
     * Modificar una Feria Existente
     */
    public void modificarFeria(int eventoId, String nombre, String ubicacion, LocalDate fechaInicio,
            int duracion, boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion,
            List<Persona> nuevosOrganizadores, int cantidadStands, TipoCobertura tipoCobertura) {

        try {
            Feria feria = (Feria) this.buscarPorId(eventoId);
            if (feria == null) {
                throw new IllegalArgumentException("No se encontró la feria con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(feria, nombre, ubicacion, fechaInicio, duracion,
                    tieneCupo, cupoMaximo, tieneInscripcion);
            feria.setCantidadStands(cantidadStands);
            feria.setTipoCobertura(tipoCobertura);

            // Actualizar participaciones si es necesario
            if (nuevosOrganizadores != null) {
                actualizarOrganizadores(feria, nuevosOrganizadores);
            }

            // Seteamos la lista de participaciones actuales al Evento
            List<Participacion> listaParticipaciones = participacionServicio.buscarPorEvento(feria);
            feria.setParticipaciones(listaParticipaciones);

            // Guardar cambios
            this.modificar(feria);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar la Feria: " + e.getMessage(), e);
        }
    }

    // ===== MÉTODOS AUXILIARES =====

    /**
     * Configura las propiedades base comunes a todos los eventos
     */
    private void configurarEventoBase(Evento evento, String nombre, String ubicacion,
            LocalDate fechaInicio, int duracion, boolean tieneCupo, int cupoMaximo,
            boolean tieneInscripcion) {
        evento.setNombre(nombre);
        evento.setUbicacion(ubicacion);
        evento.setFechaInicio(fechaInicio);
        evento.setDuracionEstimada(duracion);
        evento.setTieneCupo(tieneCupo);
        if (tieneCupo) {
            evento.setCapacidadMaxima(cupoMaximo);
        }
        evento.setTieneInscripcion(tieneInscripcion);
    }

    /**
     * Actualiza la lista de organizadores de un evento
     */
    private void actualizarOrganizadores(Evento evento, List<Persona> nuevosOrganizadores) {
        // Obtener organizadores actuales
        List<Persona> organizadoresActuales = evento.getOrganizadores();

        // Eliminar organizadores que ya no están en la nueva lista
        for (Persona organizadorActual : organizadoresActuales) {
            if (!nuevosOrganizadores.contains(organizadorActual)) {
                participacionServicio.eliminarParticipacion(evento, organizadorActual, TipoRol.ORGANIZADOR);
            }
        }

        // Agregar nuevos organizadores
        for (Persona nuevoOrganizador : nuevosOrganizadores) {
            if (!organizadoresActuales.contains(nuevoOrganizador)) {
                try {
                    participacionServicio.crearParticipacion(evento, nuevoOrganizador, TipoRol.ORGANIZADOR);
                } catch (IllegalArgumentException e) {
                    // Ya existe la participación, continuar
                }
            }
        }
    }

    /**
     * Actualiza la lista de artistas de un concierto
     */
    private void actualizarArtistas(Concierto concierto, List<Persona> nuevosArtistas) {
        // Obtener artistas actuales
        List<Persona> artistasActuales = concierto.getArtistas();

        // Eliminar artistas que ya no están en la nueva lista
        for (Persona artistaActual : artistasActuales) {
            if (!nuevosArtistas.contains(artistaActual)) {
                participacionServicio.eliminarParticipacion(concierto, artistaActual, TipoRol.ARTISTA);
            }
        }

        // Agregar nuevos artistas
        for (Persona nuevoArtista : nuevosArtistas) {
            if (!artistasActuales.contains(nuevoArtista)) {
                try {
                    participacionServicio.crearParticipacion(concierto, nuevoArtista, TipoRol.ARTISTA);
                } catch (IllegalArgumentException e) {
                    // Ya existe la participación, continuar
                }
            }
        }
    }

    /**
     * Elimina un evento (baja lógica)
     */
    public void baja(int eventoId) {
        try {
            Evento evento = this.buscarPorId(eventoId);
            if (evento == null) {
                throw new IllegalArgumentException("No se encontró el evento con ID: " + eventoId);
            }

            // 1. Primero dar de baja todas las participaciones asociadas al evento
            int participacionesEliminadas = participacionServicio.eliminarTodasLasParticipacionesDelEvento(evento);

            // 2. Luego marcar el evento como inactivo (baja lógica)
            marcarComoInactivo(evento);

            // 3. Guardar los cambios del evento
            modificar(evento);

            // Log informativo (opcional)
            System.out.println("Evento eliminado. Participaciones afectadas: " + participacionesEliminadas);

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el evento: " + e.getMessage(), e);
        }
    }

    /// === METODOS DE OBTENCION ===
    
    public List<Evento> obtenerEventosConfirmadosInscribibles() {
        List<Evento> listaEventosFiltrados = new ArrayList<>();

        for (Evento unEvento : buscarTodos()) {
            if (unEvento.getEstado() == EstadoEvento.CONFIRMADO && unEvento.isTieneInscripcion()) {
                listaEventosFiltrados.add(unEvento);
            }
        }
        return listaEventosFiltrados;
    }

    public List<Evento> obtenerEventosInscribiblesEnEstadosHabilitados() {
        List<Evento> listaEventosFiltrados = new ArrayList<>();
        List<EstadoEvento> estadosPermitidos = Arrays.asList(
                EstadoEvento.CONFIRMADO,
                EstadoEvento.EN_EJECUCION,
                EstadoEvento.FINALIZADO,
                EstadoEvento.CANCELADO
        );

        for (Evento unEvento : buscarTodos()) {
            if (estadosPermitidos.contains(unEvento.getEstado()) && unEvento.isTieneInscripcion()) {
                listaEventosFiltrados.add(unEvento);
            }
        }
        return listaEventosFiltrados;
    }
    
    public List<Evento> obtenerEventosPorEstado(EstadoEvento unEstado)
    {
        // Declaramos una lista que contendra los eventos filtrados
        List<Evento> listaEventosFiltrados = new ArrayList<>();
        // Recorremos todos los eventos persistidos y Agregamos a la lista aquellos cuyo estado sea igual al pasado por parametro
        for (Evento unEvento : buscarTodos()) {
            if (unEvento.getEstado() == unEstado) {
                listaEventosFiltrados.add(unEvento);
            }
        }
        return listaEventosFiltrados;        
    }
    

    
    //=== METODOS ESPECIFICOS === 

    public void inscribirParticipantes(Evento unEvento, Persona unaPersona)
    {
        try {
            // Innscribimos una Persona a un evento utilizando el tipo de Rol PARTICIPANTE
            participacionServicio.inscribirParticipante(unEvento, unaPersona);
            unEvento.setParticipaciones(participacionServicio.buscarPorEvento(unEvento));
            this.modificar(unEvento);
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Metodo orientado a determinar si una persona con X rol participa o no ya con ese rol en dicho evento
    public boolean participaPorTipo(Evento unEvento, Persona unaPersona, TipoRol rol)
    {
        return participacionServicio.existeParticipacion(unEvento, unaPersona, rol);        
    }
    
    // Metodo para confirmar un EVento (Pasar de PLANIFICADO A CONFIRMADO)
    public void confirmarEvento(Evento unEvento)
    {
        try {
            // Seteamos el estado de Confirmado al evento
            unEvento.setEstado(EstadoEvento.CONFIRMADO);
            // Modificamos la instancia existente en la BD
            modificar(unEvento);
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Metodo para Cancelar un Evento (Pasar de PLANIFICADO/CONFIRMADO/EN_EJECUCION A CANCELADO)
    public void cancelarEvento(Evento unEvento)
    {
        try {
            // Seteamos el estado de Confirmado al evento
            unEvento.setEstado(EstadoEvento.CANCELADO);
            // Modificamos la instancia existente en la BD
            modificar(unEvento);
        } catch (Exception e) {
            throw e;
        }
    }
    
    

}