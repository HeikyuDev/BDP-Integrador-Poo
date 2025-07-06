package com.mycompany.bdppeventos.model.enums;

/**
 * Enum que define los tipos de modalidad para los talleres
 */

public enum TipoModalidad {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual");

    private final String descripcion;

    TipoModalidad(String descripcion) {
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