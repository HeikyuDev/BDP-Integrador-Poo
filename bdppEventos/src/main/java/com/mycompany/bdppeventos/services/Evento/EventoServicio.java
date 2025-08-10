package com.mycompany.bdppeventos.services.Evento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.dto.DatosComunesEvento;
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
    public void altaExposicion(DatosComunesEvento datosComunes ,TipoDeArte tipoArte, Persona curador) {
        try {
            // 1. Crear la exposición sin las participaciones
            Exposicion exposicion = new Exposicion();
            configurarEventoBase(exposicion, datosComunes);
            exposicion.setUnTipoArte(tipoArte);

            // 2. Guardar el evento
            this.insertar(exposicion);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(exposicion, datosComunes.organizadores());
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
    public void altaTaller(DatosComunesEvento datosComunes,boolean esPresencial, Persona instructor) {
        try {
            // 1. Crear el taller
            Taller taller = new Taller();
            configurarEventoBase(taller, datosComunes);
            taller.setEsPresencial(esPresencial);

            // 2. Guardar el evento
            this.insertar(taller);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(taller, datosComunes.organizadores());
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
    public void altaConcierto(DatosComunesEvento datosComunes,boolean esPago, double monto, List<Persona> artistas) {

        try {
            // 1. Crear el concierto
            Concierto concierto = new Concierto();
            configurarEventoBase(concierto, datosComunes);
            concierto.setEsPago(esPago);
            if (esPago) {
                concierto.setMonto(monto);
            }

            // 2. Guardar el evento
            this.insertar(concierto);

            // 3. Crear participaciones
            participacionServicio.registrarOrganizadores(concierto, datosComunes.organizadores());
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
    public void altaCicloCine(DatosComunesEvento datosComunes,Proyeccion proyeccion, boolean charlasPosteriores) {

        try {
            // 1. Crear el ciclo de cine
            CicloDeCine cicloCine = new CicloDeCine();
            configurarEventoBase(cicloCine, datosComunes);
            cicloCine.setUnaProyeccion(proyeccion);
            cicloCine.setCharlasPosteriores(charlasPosteriores);

            // 2. Guardar el evento
            this.insertar(cicloCine);

            // 3. Crear participaciones (solo organizadores)
            participacionServicio.registrarOrganizadores(cicloCine, datosComunes.organizadores());
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
    public void altaFeria(DatosComunesEvento datosComunes,int cantidadStands, TipoCobertura tipoCobertura) {

        try {
            // 1. Crear la feria
            Feria feria = new Feria();
            configurarEventoBase(feria, datosComunes);
            feria.setCantidadStands(cantidadStands);
            feria.setTipoCobertura(tipoCobertura);

            // 2. Guardar el evento
            this.insertar(feria);

            // 3. Crear participaciones (solo organizadores)
            participacionServicio.registrarOrganizadores(feria, datosComunes.organizadores());
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
    public void modificarExposicion(int eventoId, DatosComunesEvento datosComunes, TipoDeArte tipoArte, Persona nuevoCurador) {

        try {
            Exposicion exposicion = (Exposicion) this.buscarPorId(eventoId);
            if (exposicion == null) {
                throw new IllegalArgumentException("No se encontró la exposición con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(exposicion, datosComunes);
            exposicion.setUnTipoArte(tipoArte);

            // Actualizar participaciones si es necesario
            if (datosComunes.organizadores() != null) {
                actualizarOrganizadores(exposicion, datosComunes.organizadores());
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
    public void modificarTaller(int eventoId, DatosComunesEvento datosComunes, boolean esPresencial, Persona nuevoInstructor) {

        try {
            Taller taller = (Taller) this.buscarPorId(eventoId);
            if (taller == null) {
                throw new IllegalArgumentException("No se encontró el taller con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(taller, datosComunes);
            taller.setEsPresencial(esPresencial);

            // Actualizar participaciones si es necesario
            if (datosComunes.organizadores() != null) {
                actualizarOrganizadores(taller, datosComunes.organizadores());
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
    public void modificarConcierto(int eventoId, DatosComunesEvento datosComunes, boolean esPago, double monto, List<Persona> nuevosArtistas) {

        try {
            Concierto concierto = (Concierto) this.buscarPorId(eventoId);
            if (concierto == null) {
                throw new IllegalArgumentException("No se encontró el concierto con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(concierto, datosComunes);
            concierto.setEsPago(esPago);
            if (esPago) {
                concierto.setMonto(monto);
            }

            // Actualizar participaciones si es necesario
            if (datosComunes.organizadores() != null) {
                actualizarOrganizadores(concierto, datosComunes.organizadores());
            }

            // Actualizar Artistas si cambiaron            
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
    public void modificarCicloCine(int eventoId, DatosComunesEvento datosComunes, Proyeccion proyeccion, boolean charlasPosteriores) {

        try {
            CicloDeCine cicloCine = (CicloDeCine) this.buscarPorId(eventoId);
            if (cicloCine == null) {
                throw new IllegalArgumentException("No se encontró el ciclo de cine con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(cicloCine, datosComunes);
            cicloCine.setUnaProyeccion(proyeccion);
            cicloCine.setCharlasPosteriores(charlasPosteriores);

            // Actualizar participaciones si es necesario
            if (datosComunes.organizadores() != null) {
                actualizarOrganizadores(cicloCine, datosComunes.organizadores());
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
    public void modificarFeria(int eventoId, DatosComunesEvento datosComunes, int cantidadStands, TipoCobertura tipoCobertura) {

        try {
            Feria feria = (Feria) this.buscarPorId(eventoId);
            if (feria == null) {
                throw new IllegalArgumentException("No se encontró la feria con ID: " + eventoId);
            }

            // Actualizar datos básicos
            configurarEventoBase(feria, datosComunes);
            feria.setCantidadStands(cantidadStands);
            feria.setTipoCobertura(tipoCobertura);

            // Actualizar participaciones si es necesario
            if (datosComunes.organizadores() != null) {
                actualizarOrganizadores(feria, datosComunes.organizadores());
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
    private void configurarEventoBase(Evento evento, DatosComunesEvento datosComunes) {
        try {
            evento.setNombre(datosComunes.nombre());
            evento.setUbicacion(datosComunes.ubicacion());
            evento.setFechaInicio(datosComunes.fechaInicio());
            evento.setDuracionEstimada(datosComunes.duracion());
            evento.setTieneCupo(datosComunes.tieneCupo());
            if (datosComunes.tieneCupo()) {
                evento.setCapacidadMaxima(datosComunes.cupoMaximo());
            }
            evento.setTieneInscripcion(datosComunes.tieneInscripcion());
        } catch (Exception e) {
            throw e;
        }
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
                    throw e;
                }
            }
        }
    }

    /**
     * Actualiza la lista de artistas de un concierto
     */
    private void actualizarArtistas(Concierto concierto, List<Persona> nuevosArtistas) {
        try {
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
        } catch (Exception e) {
            throw e;
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

            if (!evento.getActivo()) {
                throw new IllegalStateException("El Evento ya está dado de baja.");
            }

            // 1. Primero dar de baja todas las participaciones asociadas al evento
            participacionServicio.eliminarTodasLasParticipacionesDelEvento(evento);

            // 2. Luego marcar el evento como inactivo (baja lógica)
            marcarComoInactivo(evento);

            // 3. Guardar los cambios del evento
            modificar(evento);          
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el evento: " + e.getMessage(), e);
        }
    }

    /// === METODOS DE OBTENCION ===
        
    public List<Evento> obtenerEventosPorEstado(List<EstadoEvento> estados, boolean inscribible) {
        // Validación de entrada
        if (estados == null || estados.isEmpty()) {
            return new ArrayList<>();
        }

        List<Evento> eventosFiltrados = new ArrayList<>();

        for (Evento evento : buscarTodos()) {
            // Verificar que el evento y su estado no sean null
            if (evento == null || evento.getEstado() == null) {
                continue;
            }

            // Verificar si está en los estados deseados
            if (!estados.contains(evento.getEstado())) {
                continue;
            }

            // Si requiere inscripción, verificar que la tenga
            if (inscribible && !evento.isTieneInscripcion()) {
                continue;
            }

            eventosFiltrados.add(evento);
        }

        return eventosFiltrados;
    }
    
    // Obtiene eventos que ocurren dentro de un rango de fechas específico y con estados determinados
    
    public List<Evento> obtenerEventosEnRango(LocalDate fechaInicio, LocalDate fechaFin, List<EstadoEvento> estados) {
        // Validación de entrada
        if (fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }

        if (fechaInicio.isAfter(fechaFin)) {
            return new ArrayList<>();
        }

        if (estados == null || estados.isEmpty()) {
            return new ArrayList<>();
        }

        List<Evento> eventosFiltrados = new ArrayList<>();

        for (Evento evento : buscarTodos()) {
            // Verificar que el evento y sus fechas no sean null
            if (evento == null || evento.getEstado() == null
                    || evento.getFechaInicio() == null || evento.getFechaFin() == null) {
                continue;
            }

            // Verificar si está en los estados deseados
            if (!estados.contains(evento.getEstado())) {
                continue;
            }

            // Verificar si el evento intersecta con el rango de fechas
            // Un evento intersecta si:
            // - Su fecha de inicio es <= fechaFin del rango Y
            // - Su fecha de fin es >= fechaInicio del rango
            if (!evento.getFechaInicio().isAfter(fechaFin)
                    && !evento.getFechaFin().isBefore(fechaInicio)) {
                eventosFiltrados.add(evento);
            }
        }

        return eventosFiltrados;
    }
                    
    
    //=== METODOS ESPECIFICOS === 

    public void inscribirParticipantes(Evento unEvento, Persona unaPersona)
    {
        try {
            // Innscribimos una Persona a un evento utilizando el tipo de Rol PARTICIPANTE
            Participacion unaParticipacion = participacionServicio.inscribirParticipante(unEvento, unaPersona);
            // Agregamos la particpacion a la lista de participaciones asociada al evento
            unEvento.agregarParticipacion(unaParticipacion);
            // Modifcamos la instancia de evento para Persistir los cambios
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
    public void confirmarEvento(Evento unEvento) {
        try {
            if(unEvento.getEstado().equals(EstadoEvento.PLANIFICADO))
            {
                // Seteamos el estado de Confirmado al evento
                unEvento.setEstado(EstadoEvento.CONFIRMADO);
                // Modificamos la instancia existente en la BD
                modificar(unEvento);
            }
            else
            {
                throw new IllegalArgumentException("Solo se pueden Confirmar eventos, cuyo estado es: PLANIFICADO");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Metodo para Cancelar un Evento (Pasar de PLANIFICADO/CONFIRMADO/EN_EJECUCION A CANCELADO)
    public void cancelarEvento(Evento unEvento) {
        try {
            if (unEvento.getEstado() == EstadoEvento.PLANIFICADO
                    || unEvento.getEstado() == EstadoEvento.CONFIRMADO
                    || unEvento.getEstado() == EstadoEvento.EN_EJECUCION) {
                unEvento.setEstado(EstadoEvento.CANCELADO);
                modificar(unEvento);
            } else {
                throw new IllegalArgumentException(
                        "Solo se pueden cancelar eventos en estado PLANIFICADO, CONFIRMADO o EN_EJECUCION"
                );
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Funcionalidad: ACTUAALIZAR ESTADOS
    // Este metodo esta orientado a actualzar el estado de los eventos de forma automatica                
public void actualizarEstadoEventos() {
    LocalDate hoy = LocalDate.now();

    for (Evento unEvento : buscarTodos()) {
        LocalDate inicio = unEvento.getFechaInicio();
        LocalDate fin = unEvento.getFechaFin();
        EstadoEvento estado = unEvento.getEstado();

        // Si el evento está planificado y la fecha actual supera la fecha de fin,
        // significa que nunca se confirmó ni ejecutó, por lo tanto se cancela.
        if (estado == EstadoEvento.PLANIFICADO) {
            if (hoy.isAfter(fin)) {
                unEvento.setEstado(EstadoEvento.CANCELADO);
            }
        } 
        // Si el evento está confirmado:
        // - Y la fecha actual está entre la fecha de inicio y fin (inclusive), pasa a EN_EJECUCION.
        // - O si la fecha actual ya superó la fecha de fin, se marca como FINALIZADO.
        else if (estado == EstadoEvento.CONFIRMADO) {
            if (!hoy.isBefore(inicio) && !hoy.isAfter(fin)) {
                unEvento.setEstado(EstadoEvento.EN_EJECUCION);
            } else if (hoy.isAfter(fin)) {
                unEvento.setEstado(EstadoEvento.FINALIZADO);
            }
        } 
        // Si el evento está en ejecución y la fecha actual ya superó la fecha de fin,
        // se marca como FINALIZADO.
        else if (estado == EstadoEvento.EN_EJECUCION) {
            if (hoy.isAfter(fin)) {
                unEvento.setEstado(EstadoEvento.FINALIZADO);
            }
        }

        // Se actualiza el evento en la base de datos 
        modificar(unEvento);
    }
}

}


