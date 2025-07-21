
/**
 * Controlador del panel de concierto para el ABM de eventos.
 * Permite gestionar la lógica asociada a los conciertos, como la configuración de si es pago o no.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.util.ConfiguracionIgu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class PanelConciertoController extends ConfiguracionIgu implements Initializable {



    /**
     * CheckBox para indicar si el concierto es pago.
     */
    @FXML
    private CheckBox chkEsPago;

    /**
     * Campo de texto asociado al CheckBox para ingresar información adicional si es pago.
     */
    @FXML
    private TextField txtEsPago;
    
    

    /**
     * Inicializa el panel de concierto al cargar la vista.
     * Aquí se puede agregar lógica de inicialización si es necesario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: agregar lógica de inicialización si es necesario
    }
    
    
    
    // Metodos específicos
    
    
    // Metodos de Controles

    /**
     * Acción asociada al control para configurar si el concierto es pago.
     * Llama a la función utilitaria para enlazar el CheckBox y el TextField.
     */
    @FXML
    private void configurarEsPago() {
        configuracionCheckTextfield(chkEsPago, txtEsPago);
    }
    
}
