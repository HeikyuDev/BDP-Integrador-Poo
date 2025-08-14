package com.mycompany.bdppeventos.model.enums;

public enum TipoRol {
    SIN_ROL("Sin Rol"), // Rol por defecto
    ARTISTA("Artista"),
    CURADOR("Curador"),
    INSTRUCTOR("Instructor"),
    ORGANIZADOR("Organizador"),
    PARTICIPANTE("Participante");

    private final String descripcion; // final porque no queremos que la descripcion cambie una vez asignada

    TipoRol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna el rol por defecto para nuevos usuarios
     * 
     * @return TipoRol por defecto
     */
    public static TipoRol getRolPorDefecto() {
        return SIN_ROL;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
