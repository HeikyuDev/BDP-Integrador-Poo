package com.mycompany.bdppeventos.model.interfaces;

public interface Activable {
    
    // Constantes
    Boolean ACTIVO = true;
    Boolean INACTIVO = false;
    
    // Métodos que DEBEN implementar las clases
    void activar();
    void desactivar();
    Boolean getActivo();
    void setActivo(Boolean activo);
    
    // Método con implementación por defecto
    default boolean estaActivo() {
        Boolean activo = getActivo();
        
        if (activo == null) {
            return false;
        }
        
        if (activo == true) {
            return true;
        } else {
            return false;
        }
    }
    
    default String getEstadoTexto() {
        boolean activo = estaActivo();
        
        if (activo) {
            return "Activo";
        } else {
            return "Inactivo";
        }
    }
}