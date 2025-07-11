package com.mycompany.bdppeventos.services.Evento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.CrudServicio;

public class EventoServicio extends CrudServicio<Evento> {

    public EventoServicio(Repositorio repositorio) {
        super(repositorio, Evento.class);

    }

    @Override
    public Evento validarEInsertar(Object... datos) {
        if (datos.length != 6) {
            throw new IllegalArgumentException("Número incorrecto de parámetros.");
        }

        String nombre = (String) datos[0];
        LocalDate fechaInicio = (LocalDate) datos[1];
        int duracionEstimada = (int) datos[2];
        boolean tieneCupo = (boolean) datos[3];
        int capacidadMaxima = (int) datos[4];
        boolean tieneInscripcion = (boolean) datos[5];
        String ubicacion = (String) datos[6];
        EstadoEvento estado = (EstadoEvento) datos[7];
        boolean esPago = (boolean) datos[8];
        Double monto = (Double) datos[9];

        List<String> errores = new ArrayList<>();

        Evento evento = new Evento();

        try {
            evento = new Evento(nombre, fechaInicio, duracionEstimada, tieneCupo,
                    capacidadMaxima, tieneInscripcion, ubicacion, estado, esPago, monto);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errores));
        }

        insertar(evento);
        return evento;
    }

    @Override
    public void validarYModificar(Libro libro, Object... datos) {
        if (datos.length != 5) {
            throw new IllegalArgumentException("Número incorrecto de parámetros.");
        }

        String titulo = (String) datos[0];
        Categoria categoria = (Categoria) datos[1];
        Editorial editorial = (Editorial) datos[2];
        Idioma idioma = (Idioma) datos[3];
        @SuppressWarnings("unchecked")
        Set<Autor> autores = (Set<Autor>) datos[4];
        Libro aux = new Libro();

        List<String> errores = new ArrayList<>();

        // Validamos si el miembro o la copia seleccionados existen
        if (categoria != null) {
            if (!servicioCategoria.existe(categoria, categoria.getId())) {
                errores.add("La categoría seleccionada no se encuentra en la base de datos.");
            }
        }

        if (editorial != null) {
            if (!servicioEditorial.existe(editorial, editorial.getId())) {
                errores.add("La editorial seleccionada no se encuentra en la base de datos.");
            }
        }

        if (idioma != null) {
            if (!servicioIdioma.existe(idioma, idioma.getId())) {
                errores.add("El idioma seleccionado no se encuentra en la base de datos.");
            }
        }

        for (Autor autor : autores) {
            if (autor != null) {
                if (!servicioAutor.existe(autor, autor.getId())) {
                    errores.add("El autor " + autor + " no se encuentra en la base de datos.");
                }
            }

        }

        try {
            aux.setTitulo(titulo);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        try {
            aux.setCategoria(categoria);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        try {
            aux.setEditorial(editorial);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        try {
            aux.setIdioma(idioma);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        try {
            aux.setAutores(autores);
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errores));
        }

        agregarLibroAEntidades(libro, autores, categoria, editorial, idioma);
        libro.setTitulo(titulo);
        libro.setCategoria(categoria);
        libro.setEditorial(editorial);
        libro.setIdioma(idioma);
        libro.setAutores(autores);
        modificar(libro);
    }

    @Override
    public void validarYBorrar(Libro libro) {

        for (CopiaLibro copia : libro.getCopias()) {
            if (!copia.isBaja()) {
                throw new IllegalArgumentException("No se puede eliminar el libro porque tiene copias asociadas.");
            }
        }

        borrar(libro);
    }

    @Override
    protected boolean esInactivo(Libro libro) {
        return libro.isBaja();
    }

    @Override
    protected void marcarComoInactivo(Libro libro) {
        libro.setBaja();
    }

    public String verificarCopias(Libro libro) {
        List<CopiaLibro> copias = new ArrayList<>(libro.getCopias());

        if (copias.isEmpty()) {
            throw new IllegalArgumentException("El libro seleccionado no tiene ninguna copia disponible.");
        } else {
            String respuesta = "El libro: " + libro + " tiene las siguientes copias disponibles: \n";
            for (CopiaLibro copia : copias) {
                respuesta += "Tipo: " + copia.getTipo() + " | Precio: " + copia.getPrecio() + "\n";
            }
            return respuesta;
        }
    }

    public void agregarQuitarCopia(Libro libroNuevo, CopiaLibro copia) {
        try {
            Libro libroViejo = copia.getLibro();
            libroNuevo.agregarCopia(copia);
            modificar(libroNuevo);
            if (!libroNuevo.equals(libroViejo)) {
                libroViejo.quitarCopia(copia);
                modificar(libroViejo);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public void agregarAutor(Libro libro, Autor autor) {
        try {
            libro.agregarAutor(autor);
            modificar(libro);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    private void agregarLibroAEntidades(Libro libro, Set<Autor> autoresNuevos, Categoria categoria,
            Editorial editorial,
            Idioma idioma) {
        Set<Autor> autoresViejos = libro.getAutores();
        for (Autor autor : autoresNuevos) {
            servicioAutor.agregarLibro(autor, libro);
        }
        if (!autoresNuevos.equals(autoresViejos)) {
            for (Autor autor : autoresViejos) {
                if (!autoresNuevos.contains(autor)) {
                    servicioAutor.quitarLibro(autor, libro);
                }
            }
        }
        servicioCategoria.agregarQuitarLibro(categoria, libro);
        servicioEditorial.agregarQuitarLibro(editorial, libro);
        servicioIdioma.agregarQuitarLibro(idioma, libro);
    }
}