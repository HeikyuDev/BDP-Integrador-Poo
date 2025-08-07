package com.mycompany.bdppeventos.model.interfaces;

import com.mycompany.bdppeventos.model.entities.Evento;


public interface PanelEvento<T extends Evento> {
    void validar();
    void limpiarCampos();
    void cargarDatos(T evento);
}
