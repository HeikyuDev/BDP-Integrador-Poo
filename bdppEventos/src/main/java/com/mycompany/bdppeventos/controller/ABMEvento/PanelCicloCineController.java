
/**
 * Panel controlador para la gestión de ciclos de cine dentro del ABM de eventos.
 * Permite inicializar la vista y manejar la acción de agregar un nuevo ciclo de cine.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class PanelCicloCineController implements Initializable {

    /**
     * Inicializa el panel de ciclo de cine al cargar la vista.
     * Aquí se debe implementar la lógica para mostrar todos los ciclos cargados en la base de datos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // IMPLEMENTAR: cargar y mostrar todos los ciclos de cine desde la base de datos
    }

    /**
     * Acción asociada al botón para agregar un nuevo ciclo de cine.
     * Debe abrir una ventana emergente para ingresar los datos del nuevo ciclo.
     */
    @FXML
    private void agregarCiclo() {
        // IMPLEMENTAR: lógica que abre ventana emergente para agregar ciclo
    }
}
