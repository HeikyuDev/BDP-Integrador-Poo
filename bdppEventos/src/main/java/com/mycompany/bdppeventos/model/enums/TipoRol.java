package com.mycompany.bdppeventos.model.enums;

public enum TipoRol {
    SIN_ROL("Sin Rol"),    // Rol por defecto
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
    
    /**
     * Retorna el rol por defecto para nuevos usuarios
     * @return TipoRol por defecto
     */
    public static TipoRol getRolPorDefecto() {
        return SIN_ROL;
    }

     
    /**
     * Retorna el TipoRol correspondiente a la descripcion proporcionada
     * @param descripcion Descripcion del rol
     * @return TipoRol correspondiente o SIN_ROL si no se encuentra
     */
    /*public static TipoRol fromDescripcion(String descripcion) {
        for (TipoRol rol : TipoRol.values()) {
            if (rol.descripcion.equalsIgnoreCase(descripcion)) {
                return rol;
            }
        }
        return SIN_ROL; // Retorna SIN_ROL si no se encuentra coincidencia
    }
    */

    @Override
    public String toString() {
        return descripcion;
    } 
}
