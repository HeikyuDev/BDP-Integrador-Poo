package com.mycompany.bdppeventos.services.Pelicula;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;
import com.mycompany.bdppeventos.services.proyeccion.ProyeccionServicio;
import java.util.List;

public class PeliculaServicio extends CrudServicio<Pelicula> {

    // Inicializo los valores a traves del constructor de la clase padre
    // "CrudServicio<Pelicula>"
    public PeliculaServicio(Repositorio repositorio) {
        super(repositorio, Pelicula.class);
    }

    
    
    public void altaPelicula(String titulo, Double duracion) {
        try {
            // Crear la entidad
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setTitulo(titulo);
            nuevaPelicula.setDuracion(duracion);            
            // Insertar en la base de datos
            insertar(nuevaPelicula);

        } catch (Exception e) {
            throw e;
        }
    }

    public void bajaPelicula(Integer idPelicula) {
    try {
        Pelicula pelicula = buscarPorId(idPelicula);
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no existe.");
        }
        if (!pelicula.getActivo()) {
            throw new IllegalStateException("La película ya está dada de baja."); 
        }

        // eliminamos esta película de todas las proyecciones asociadas
        List<Proyeccion> todasLasProyecciones = repositorio.buscarTodos(Proyeccion.class);
        for (Proyeccion proy : todasLasProyecciones) {
            if (proy.getUnaListaPelicula().contains(pelicula)) {
                proy.getUnaListaPelicula().remove(pelicula);
                repositorio.modificar(proy); // Guardar el cambio en la proyección
            }
        }

        // Marcamos como inactiva la película
        marcarComoInactivo(pelicula);
        modificar(pelicula);

    } catch (Exception e) {
        throw e;
    }
}


    public void modificarPelicula(Pelicula pelicula, String titulo, double duracion) {
        try {
            if (pelicula == null) {
                throw new IllegalArgumentException("La película no es válida para modificar.");
            }
                        
            // Validar que la película exista y esté activa
            Pelicula existente = buscarPorId(pelicula.getIdPelicula());
            if (existente == null) {
                throw new IllegalStateException("La película no existe o está inactiva.");
            }

            // Actualizar datos
            existente.setTitulo(titulo);
            existente.setDuracion(duracion);

            // Guardar cambios en la base
            modificar(existente);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    protected boolean estaActivo(Pelicula unaPelicula) {
        return unaPelicula.getActivo();
    }

    @Override
    protected void marcarComoInactivo(Pelicula unaPelicula) {
        unaPelicula.desactivar();
    }

}
