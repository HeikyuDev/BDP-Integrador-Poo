package com.mycompany.bdppeventos.model.enums;

public enum TipoCobertura {
    
    AIRE_LIBRE("Aire Libre"),
    TECHADO("Techado");
    
    // Atributo
    private final String descripcion;

    // Constructor
    private TipoCobertura(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Metodos

    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }        
}
