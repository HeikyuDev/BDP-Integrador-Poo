package com.mycompany.bdppeventos.services.proyeccion;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import java.util.List;


public class ProyeccionServicio extends CrudServicio<Proyeccion>{
    
    
    public ProyeccionServicio(Repositorio repositorio) {
        super(repositorio, Proyeccion.class);
    }
        
    public void altaProyeccion(String nombre, List<Pelicula> listaPeliculas)
    {
        try {
            // Creamos El Objeto Proyeccion
            Proyeccion unaProyeccion = new Proyeccion();
            // Seteamos los Atributos
            unaProyeccion.setNombre(nombre);
            unaProyeccion.setUnaListaPelicula(listaPeliculas);
            // Guardamos la instancia en Base de Datos
            insertar(unaProyeccion);            
        } catch (Exception e) {
            throw e; // Lanzamos la excepcion al controlador de la vista
        }
    }
    
    public void modificarProyeccion(Proyeccion proyeccion, String nombre, List<Pelicula> listaPelicula) {
        try {
            if (proyeccion == null) {
                throw new IllegalArgumentException("La Proyeccion no es válida para modificar.");
            }

            // Validar que la película exista y esté activa
            Proyeccion existente = buscarPorId(proyeccion.getIdProyeccion());
            if (existente == null) {
                throw new IllegalStateException("La Proyeccion no existe o está inactiva.");
            }

            // Actualizar datos
            existente.setNombre(nombre);
            existente.setUnaListaPelicula(listaPelicula);

            // Guardar cambios en la base
            modificar(existente);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void bajaProyeccion(Integer idProyeccion) {
        try {
            Proyeccion proyeccion = buscarPorId(idProyeccion);
            if (proyeccion == null) {
                throw new IllegalArgumentException("La Proyeccion no existe.");
            }
            if (!proyeccion.getActivo()) {
                throw new IllegalStateException("La Proyeccion ya está dada de baja.");
            }

            
            //  Eliminar esta Proyección de todos los Ciclos que la usen
            List<CicloDeCine> listaCiclos = repositorio.buscarTodos(CicloDeCine.class);
            for (CicloDeCine unCiclo : listaCiclos) {
                if (unCiclo.getUnaProyeccion() != null && unCiclo.getUnaProyeccion().equals(proyeccion)) {
                    unCiclo.setUnaProyeccion(null);
                    repositorio.modificar(unCiclo); // Persistir el cambio
                }
            }
            
            // Marcar como inactivo (baja lógica)
            marcarComoInactivo(proyeccion);

            // Guardar cambios en la base (update)
            modificar(proyeccion);
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Metodos Abstractos 
    @Override
    protected boolean estaActivo(Proyeccion unaProyeccion) {
        return unaProyeccion.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Proyeccion unaProyeccion) {
        unaProyeccion.desactivar();
    }
}
