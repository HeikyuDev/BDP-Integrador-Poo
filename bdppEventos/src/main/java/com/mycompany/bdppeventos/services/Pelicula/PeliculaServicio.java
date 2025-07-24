package com.mycompany.bdppeventos.services.Pelicula;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

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

            // Validar si ya existe una película activa con el mismo título
            if (existe(nuevaPelicula, nuevaPelicula.getIdPelicula())) {
                throw new IllegalStateException("La película ya existe.");
            }

            // Insertar en la base
            insertar(nuevaPelicula);

        } catch (Exception e) {
            throw e;
        }
    }

    public void bajaPelicula(Integer idPelicula) throws Exception {
        Pelicula pelicula = buscarPorId(idPelicula);
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no existe.");
        }
        if (!pelicula.getActivo()) {
            throw new IllegalStateException("La película ya está dada de baja.");
        }

        // Marcar como inactivo (baja lógica)
        marcarComoInactivo(pelicula);

        // Guardar cambios en la base (update)
        modificar(pelicula);
    }

    public void modificarPelicula(Pelicula pelicula, String titulo, double duracion) {
        try {
            if (pelicula == null) {
                throw new IllegalArgumentException("La película no es válida para modificar.");
            }

            // Seteamos los atributos
            pelicula.setTitulo(titulo); // Lanza una Excepcion si sale mal
            pelicula.setDuracion(duracion); // Lanza una Excepcion si sale mal
            
            // Validar que la película exista y esté activa
            Pelicula existente = buscarPorId(pelicula.getIdPelicula());
            if (existente == null) {
                throw new IllegalStateException("La película no existe o está inactiva.");
            }

            // Actualizar datos
            existente.setTitulo(pelicula.getTitulo());
            existente.setDuracion(pelicula.getDuracion());

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
