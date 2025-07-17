package com.mycompany.bdppeventos.model.enums;

public enum TipoRol {
    ARTISTA("Artista"),
    CURADOR("Curador"),
    INSTRUCTOR("Instructor"),
    ORGANIZADOR("Organizador"),
    PARTICIPANTE("Participante");
    
    private final String descripcion;
    
    TipoRol(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    } 
}
