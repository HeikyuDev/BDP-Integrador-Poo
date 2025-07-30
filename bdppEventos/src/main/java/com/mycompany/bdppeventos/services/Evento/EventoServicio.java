package com.mycompany.bdppeventos.services.Evento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;

public class EventoServicio extends CrudServicio<Evento> {

    public EventoServicio(Repositorio repositorio) {
        // Inicializo el objeto con el constructor del padre pasandole el repositorio, y
        // el evento
        super(repositorio, Evento.class);
    }

    @Override
    protected boolean estaActivo(Evento unEvento) {
        return unEvento.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Evento unEvento) {
        unEvento.desactivar();
    }

    public void altaEvento(String nombre, String ubicacion, LocalDate fechaInicio, int duracion, boolean tieneCupo,
            int cupoMaximo, boolean tieneInscripcion, TipoEvento tipoEvento, List<Persona> listaPersona,
            Object[] datosEspecificos) {
        try {
            switch (tipoEvento) {
                case EXPOSICION -> {
                    // Creamos la Subclase Exposicion
                    Exposicion unaExposicion = new Exposicion();
                    unaExposicion.setNombre(nombre);
                    unaExposicion.setUbicacion(ubicacion);
                    unaExposicion.setFechaInicio(fechaInicio);
                    unaExposicion.setDuracionEstimada(duracion);
                    unaExposicion.setTieneCupo(tieneCupo);
                    unaExposicion.setCapacidadMaxima(cupoMaximo);
                    unaExposicion.setTieneInscripcion(tieneInscripcion);
                    unaExposicion.setUnTipoArte((TipoDeArte) datosEspecificos[0]);
                    listaPersona.add((Persona)datosEspecificos[1]); // Agrego el CURADOR a la lista de Personas
                    unaExposicion.setUnaListaPersona(listaPersona);
                    this.insertar(unaExposicion);
                    asociarEventosAPersonas(listaPersona, unaExposicion);
                }
                case TALLER -> {
                    // Creamos la Subclase Taller
                    Taller unTaller = new Taller();
                    unTaller.setNombre(nombre);
                    unTaller.setUbicacion(ubicacion);
                    unTaller.setFechaInicio(fechaInicio);
                    unTaller.setDuracionEstimada(duracion);                    
                    unTaller.setTieneCupo(tieneCupo);
                    unTaller.setCapacidadMaxima(cupoMaximo);
                    unTaller.setTieneInscripcion(tieneInscripcion);
                    unTaller.setEsPresencial((boolean) datosEspecificos[0]);
                    listaPersona.add((Persona) datosEspecificos[1]); // Agrego el INSTRUCTOR a la lista de Personas; 
                    unTaller.setUnaListaPersona(listaPersona);
                    this.insertar(unTaller);
                    asociarEventosAPersonas(listaPersona, unTaller);
                }
                case CONCIERTO -> {
                    Concierto unConcierto = new Concierto();
                    unConcierto.setNombre(nombre);
                    unConcierto.setUbicacion(ubicacion);
                    unConcierto.setFechaInicio(fechaInicio);
                    unConcierto.setDuracionEstimada(duracion);                    
                    unConcierto.setTieneCupo(tieneCupo);
                    unConcierto.setCapacidadMaxima(cupoMaximo);
                    unConcierto.setTieneInscripcion(tieneInscripcion);
                    unConcierto.setEsPago((boolean) datosEspecificos[0]);
                    unConcierto.setMonto((double) datosEspecificos[1]);
                    listaPersona.addAll((List<Persona>)datosEspecificos[2]);
                    unConcierto.setUnaListaPersona(listaPersona);
                    this.insertar(unConcierto);
                    asociarEventosAPersonas(listaPersona, unConcierto);
                }
                case CICLO_DE_CINE -> {
                    CicloDeCine unCicloDeCine = new CicloDeCine();
                    unCicloDeCine.setNombre(nombre);
                    unCicloDeCine.setUbicacion(ubicacion);
                    unCicloDeCine.setFechaInicio(fechaInicio);
                    unCicloDeCine.setDuracionEstimada(duracion);                    
                    unCicloDeCine.setTieneCupo(tieneCupo);
                    unCicloDeCine.setCapacidadMaxima(cupoMaximo);
                    unCicloDeCine.setTieneInscripcion(tieneInscripcion);
                    unCicloDeCine.setUnaProyeccion((Proyeccion) datosEspecificos[0]);
                    unCicloDeCine.setCharlasPosteriores((boolean) datosEspecificos[1]);
                    unCicloDeCine.setUnaListaPersona(listaPersona);
                    this.insertar(unCicloDeCine);
                    asociarEventosAPersonas(listaPersona, unCicloDeCine);
                }
                case FERIA -> {
                    Feria unaFeria = new Feria();
                    unaFeria.setNombre(nombre);
                    unaFeria.setUbicacion(ubicacion);
                    unaFeria.setFechaInicio(fechaInicio);
                    unaFeria.setDuracionEstimada(duracion);                    
                    unaFeria.setTieneCupo(tieneCupo);
                    unaFeria.setCapacidadMaxima(cupoMaximo);
                    unaFeria.setTieneInscripcion(tieneInscripcion);
                    unaFeria.setCantidadStands((int) datosEspecificos[0]);
                    unaFeria.setTipoCobertura((TipoCobertura) datosEspecificos[1]);
                    unaFeria.setUnaListaPersona(listaPersona);
                    this.insertar(unaFeria);
                    asociarEventosAPersonas(listaPersona, unaFeria);
                }
            }            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Exposicion> obtenerExposiciones() {
        List<Evento> listaEventos = buscarTodos();
        List<Exposicion> listaExposiciones = new ArrayList<>();

        for (Evento unEvento : listaEventos) {
            if (unEvento instanceof Exposicion) {
                listaExposiciones.add((Exposicion) unEvento);
            }
        }

        return listaExposiciones;
    }

    public List<Feria> obtenerFerias() {
        List<Evento> listaEventos = buscarTodos();
        List<Feria> listaFerias = new ArrayList<>();

        for (Evento unEvento : listaEventos) {
            if (unEvento instanceof Feria) {
                listaFerias.add((Feria) unEvento);
            }
        }

        return listaFerias;
    }

    public List<Concierto> obtenerConciertos() {
        List<Evento> listaEventos = buscarTodos();
        List<Concierto> listaConciertos = new ArrayList<>();

        for (Evento unEvento : listaEventos) {
            if (unEvento instanceof Concierto) {
                listaConciertos.add((Concierto) unEvento);
            }
        }

        return listaConciertos;
    }

    public List<CicloDeCine> obtenerCiclosDeCine() {
        List<Evento> listaEventos = buscarTodos();
        List<CicloDeCine> listaCiclosDeCine = new ArrayList<>();

        for (Evento unEvento : listaEventos) {
            if (unEvento instanceof CicloDeCine) {
                listaCiclosDeCine.add((CicloDeCine) unEvento);
            }
        }

        return listaCiclosDeCine;
    }

    public List<Taller> obtenerTalleres() {
        List<Evento> listaEventos = buscarTodos();
        List<Taller> listaTalleres = new ArrayList<>();

        for (Evento unEvento : listaEventos) {
            if (unEvento instanceof Taller) {
                listaTalleres.add((Taller) unEvento);
            }
        }

        return listaTalleres;
    }
    


// VERSIÓN MEJORADA CON VALIDACIONES
private void asociarEventosAPersonas(List<Persona> unaListaPersona, Evento unEvento) {
    if (unaListaPersona == null || unaListaPersona.isEmpty()) {
        return; // No hay personas para asociar
    }
    
    if (unEvento == null) {
        throw new IllegalArgumentException("El evento no puede ser null");
    }
    
    for (Persona unaPersona : unaListaPersona) {
        if (unaPersona != null) {
            // Verificar que la lista de eventos esté inicializada
            if (unaPersona.getListaEvento() == null) {
                unaPersona.setUnaListaEventos(new ArrayList<>());
            }
            
            // Evitar duplicados (opcional)
            if (!unaPersona.getListaEvento().contains(unEvento)) {
                unaPersona.getListaEvento().add(unEvento);
                repositorio.modificar(unaPersona);
            }
        }
    }
}

}
