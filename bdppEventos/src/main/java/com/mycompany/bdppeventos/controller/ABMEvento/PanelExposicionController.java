
/**
 * Controlador del panel de exposición para el ABM de eventos.
 * Permite gestionar la lógica asociada a la exposición, como agregar tipos de arte.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class PanelExposicionController implements Initializable {



    /**
     * Botón para agregar un nuevo tipo de arte a la exposición.
     */
    @FXML
    private Button btnAgregarTipoArte;
    

    /**
     * Inicializa el panel de exposición al cargar la vista.
     * Aquí se puede agregar lógica de inicialización si es necesario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: agregar lógica de inicialización si es necesario
    }
    
    
    
    
    // Metodos de Controles
    

    /**
     * Acción asociada al botón para agregar un tipo de arte.
     * Actualmente solo imprime un mensaje por consola.
     */
    @FXML
    private void agregarTipoArte() {
        System.out.println("Falta implementar");
    }
    
}
