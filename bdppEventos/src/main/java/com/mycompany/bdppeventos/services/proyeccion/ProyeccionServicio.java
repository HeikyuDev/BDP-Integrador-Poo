package com.mycompany.bdppeventos.services.proyeccion;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import java.util.List;
import javafx.collections.ObservableList;


public class ProyeccionServicio extends CrudServicio<Proyeccion>{
    
    
    public ProyeccionServicio(Repositorio repositorio) {
        super(repositorio, Proyeccion.class);
    }
    
    // ALTA
    public void altaProyeccion(String nombre, ObservableList<Pelicula> listaPeliculas)
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
    
    //MODIFICACION
        public void modificarProyeccion(Proyeccion proyeccion, String nombre, List<Pelicula> listaPelicula) {
        try {
            if (proyeccion == null) {
                throw new IllegalArgumentException("La película no es válida para modificar.");
            }
                                    
            // Validar que la película exista y esté activa
            Proyeccion existente = buscarPorId(proyeccion.getIdProyeccion());
            if (existente == null) {
                throw new IllegalStateException("La película no existe o está inactiva.");
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
        
        
    // BAJA
     public void bajaProyeccion(Integer idProyeccion) throws Exception {
        Proyeccion proyeccion = buscarPorId(idProyeccion);
        if (proyeccion == null) {
            throw new IllegalArgumentException("La Proyeccion no existe.");
        }
        if (!proyeccion.getActivo()) {
            throw new IllegalStateException("La Proyeccion ya está dada de baja.");
        }

        // Marcar como inactivo (baja lógica)
        marcarComoInactivo(proyeccion);

        // Guardar cambios en la base (update)
        modificar(proyeccion);
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
