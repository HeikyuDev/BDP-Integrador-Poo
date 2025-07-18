package com.mycompany.bdppeventos.model.enums;


public enum TipoConcierto {
    ACUSTICO("Acustico"),
    SINFONICO("Sinfonico"),
    FESTIVAL("Festival"),
    TRIBUTO("Tributo"),
    ELECTRONICO("Electronico"),
    OPERA("Opera"),
    JAZZ("Jazz"),
    FLAMENCO("Flamenco"),
    INDIE("Indie"),
    MULTIGENERO("Multigenero");

    private final String descripcion;

    TipoConcierto(String descripcion) {
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