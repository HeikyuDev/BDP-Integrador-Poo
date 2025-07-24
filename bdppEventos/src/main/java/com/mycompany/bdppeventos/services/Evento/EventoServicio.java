package com.mycompany.bdppeventos.services.Evento;

import java.time.LocalDate;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

public class EventoServicio extends CrudServicio<Evento> {

    public EventoServicio(Repositorio repositorio) {
        // Inicializo el objeto con el constructor del padre pasandole el repositorio, y el evento
        super(repositorio, Evento.class);
    }


    @Override
    protected  boolean estaActivo(Evento unEvento)
    {
        return unEvento.getActivo();
    }

    @Override
    protected  void marcarComoInactivo(Evento unEvento)
    {
        unEvento.desactivar();
    }

    

    public void altaEvento(String nombre, String ubicacion, LocalDate fechaInicio, int duracion, boolean tieneCupo,
            int cupoMaximo, boolean tieneInscripcion, EstadoEvento estado, TipoEvento tipoEvento,
            Object[] datosEspecificos) {
        try
        {
            switch (tipoEvento) { 
            case EXPOSICION -> {
                // Creamos la Subclase Exposicion
                Exposicion unaExposicion = new Exposicion();
                unaExposicion.setNombre(nombre);
                unaExposicion.setUbicacion(ubicacion);
                unaExposicion.setFechaInicio(fechaInicio);
                unaExposicion.setDuracionEstimada(duracion);
                unaExposicion.setEstado(estado);
                unaExposicion.setTieneCupo(tieneCupo);
                unaExposicion.setCapacidadMaxima(cupoMaximo);
                unaExposicion.setTieneInscripcion(tieneInscripcion);
                unaExposicion.setUnTipoArte((TipoDeArte) datosEspecificos[0]);
                this.insertar(unaExposicion);
            }
            case TALLER -> {
                // Creamos la Subclase Taller
                Taller unTaller = new Taller();                
                unTaller.setNombre(nombre);
                unTaller.setUbicacion(ubicacion);
                unTaller.setFechaInicio(fechaInicio);
                unTaller.setDuracionEstimada(duracion);
                unTaller.setEstado(estado);
                unTaller.setTieneCupo(tieneCupo);
                unTaller.setCapacidadMaxima(cupoMaximo);
                unTaller.setTieneInscripcion(tieneInscripcion);
                unTaller.setEsPresencial((boolean)datosEspecificos[0]);
                this.insertar(unTaller);                
            }
            case CONCIERTO -> {
                Concierto unConcierto = new Concierto();
                unConcierto.setNombre(nombre);
                unConcierto.setUbicacion(ubicacion);
                unConcierto.setFechaInicio(fechaInicio);
                unConcierto.setDuracionEstimada(duracion);
                unConcierto.setEstado(estado);
                unConcierto.setTieneCupo(tieneCupo);
                unConcierto.setCapacidadMaxima(cupoMaximo);
                unConcierto.setTieneInscripcion(tieneInscripcion);
                unConcierto.setEsPago((boolean)datosEspecificos[0]);
                unConcierto.setMonto((double)datosEspecificos[1]);
                this.insertar(unConcierto);
            }
            case CICLO_DE_CINE ->
            {
                CicloDeCine unCicloDeCine = new CicloDeCine();
                unCicloDeCine.setNombre(nombre);
                unCicloDeCine.setUbicacion(ubicacion);
                unCicloDeCine.setFechaInicio(fechaInicio);
                unCicloDeCine.setDuracionEstimada(duracion);
                unCicloDeCine.setEstado(estado);
                unCicloDeCine.setTieneCupo(tieneCupo);
                unCicloDeCine.setCapacidadMaxima(cupoMaximo);
                unCicloDeCine.setTieneInscripcion(tieneInscripcion);
                unCicloDeCine.setUnaProyeccion((Proyeccion)datosEspecificos[0]);
                unCicloDeCine.setCharlasPosteriores((boolean)datosEspecificos[1]);
                this.insertar(unCicloDeCine);
            }
            case FERIA ->
            {
                Feria unaFeria = new Feria();
                unaFeria.setNombre(nombre);
                unaFeria.setUbicacion(ubicacion);
                unaFeria.setFechaInicio(fechaInicio);
                unaFeria.setDuracionEstimada(duracion);
                unaFeria.setEstado(estado);
                unaFeria.setTieneCupo(tieneCupo);
                unaFeria.setCapacidadMaxima(cupoMaximo);
                unaFeria.setTieneInscripcion(tieneInscripcion);
                unaFeria.setCantidadStands((int)datosEspecificos[0]);
                unaFeria.setTipoCobertura((TipoCobertura)datosEspecificos[1]);
                this.insertar(unaFeria);
            }

        }
        }
        catch(IllegalArgumentException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw e;
        }
    }

}
