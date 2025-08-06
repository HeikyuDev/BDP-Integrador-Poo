package com.mycompany.bdppeventos.dto;

import com.mycompany.bdppeventos.model.entities.Persona;
import java.time.LocalDate;
import java.util.List;


// Un DTO es un Objeto de Tranferencia de Datos
// un record que se usa solo para transportar datos entre capas de una aplicación, sin lógica de negocio ni comportamiento complejo.
public record DatosComunesEvento(
    String nombre,
    String ubicacion,
    LocalDate fechaInicio,
    int duracion,
    boolean tieneCupo,
    int cupoMaximo,
    boolean tieneInscripcion,
    List<Persona> organizadores
) {}
