
/**
 * Controlador del panel de taller para el ABM de eventos.
 * Gestiona la selección del tipo de modalidad (presencial o virtual) y la configuración
 * de los controles relacionados con el cupo del taller.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PanelTallerController implements Initializable {

    /**
     * Inicializa el panel de taller al cargar la vista.
     * Configura el ToggleGroup para los RadioButton de modalidad.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarToggleGroup();
    }

    // Controles

    /**
     * RadioButton para seleccionar modalidad presencial.
     */
    @FXML
    private RadioButton rbtnPresencial;

    /**
     * RadioButton para seleccionar modalidad virtual.
     */
    @FXML
    private RadioButton rbtnVirtual;

    /**
     * Grupo de botones para asegurar que solo se seleccione una modalidad a la vez.
     */
    private final ToggleGroup rBtnGroup = new ToggleGroup();

    /**
     * Configura el ToggleGroup para los RadioButton de modalidad.
     * Si se deselecciona la opción actual, vuelve a seleccionarla para evitar que
     * ambas queden desmarcadas.
     */
    private void configurarToggleGroup() {
        rbtnPresencial.setToggleGroup(rBtnGroup);
        rbtnVirtual.setToggleGroup(rBtnGroup);

        rBtnGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });
    }

    // Metodos Específicos

    public boolean getEsPrecencial() {
        return rbtnPresencial.isSelected();                 
    }

}
