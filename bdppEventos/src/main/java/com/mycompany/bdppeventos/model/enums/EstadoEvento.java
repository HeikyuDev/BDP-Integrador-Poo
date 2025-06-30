package com.mycompany.bdppeventos.model.enums;

public enum EstadoEvento {
    PLANIFICADO("Planificado"),
    CONFIRMADO("Confirmado"),
    EN_EJECUCION("En Ejecución"),      // ← Sin acento en código
    FINALIZADO("Finalizado");          // ← Punto y coma OBLIGATORIO
    
    // Atributo
    private final String descripcion;
    
    // Constructor
    EstadoEvento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Getter
    public String getDescripcion() {
        return descripcion;
    }
    
    // Para mostrar en ComboBox
    @Override
    public String toString() {
        return descripcion;
    }
}