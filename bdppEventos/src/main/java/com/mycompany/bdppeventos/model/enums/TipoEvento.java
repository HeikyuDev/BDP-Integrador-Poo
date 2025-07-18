package com.mycompany.bdppeventos.model.enums;


public enum TipoEvento {
    EXPOSICION("Exposicion"),
    TALLER("Taller"),
    CONCIERTO("Concierto"),
    CICLO_DE_CINE("Ciclo de Cine"),
    FERIA("Feria");
    
    private  final String descripcion;
            
    private TipoEvento(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }            

    @Override
    public String toString() {
        return descripcion;  // Retorna la descripción formateada, no el nombre del enum
    }
}
