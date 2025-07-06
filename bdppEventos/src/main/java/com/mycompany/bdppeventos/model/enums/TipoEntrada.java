package com.mycompany.bdppeventos.model.enums;

/**
 * Enum que define los tipos de entrada para los conciertos
 */
public enum TipoEntrada {
    GRATUITA("Gratuita"),
    PAGA("Paga");

    private final String descripcion;

    TipoEntrada(String descripcion) {
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