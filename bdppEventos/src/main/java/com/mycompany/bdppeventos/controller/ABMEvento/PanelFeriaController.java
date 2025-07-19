
/**
 * Controlador del panel de feria para el ABM de eventos.
 * Permite gestionar la cantidad de stands y el tipo de cobertura de la feria.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PanelFeriaController extends ConfiguracionIguEvento implements Initializable {


    // Atributos
    
    

    /**
     * Campo de texto para ingresar la cantidad de stands de la feria.
     */
    @FXML 
    private TextField txtCantidadStands;

    /**
     * ComboBox para seleccionar el tipo de cobertura de la feria.
     */
    @FXML
    private ComboBox<TipoCobertura> cmbTipoCobertura;
    
    

    /**
     * Inicializa el panel de feria al cargar la vista.
     * Carga los valores posibles de tipo de cobertura en el ComboBox.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configuracionEnumEnCombo(cmbTipoCobertura, TipoCobertura.class);
    }
    
    
    
    
    
    
}
