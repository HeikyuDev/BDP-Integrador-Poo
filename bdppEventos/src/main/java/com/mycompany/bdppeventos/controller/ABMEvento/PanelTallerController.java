
/**
 * Controlador del panel de taller para el ABM de eventos.
 * Gestiona la selección del tipo de modalidad (presencial o virtual) y la configuración
 * de los controles relacionados con el cupo del taller.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.Persona;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PanelTallerController implements Initializable {

    // Controles

    
    @FXML
    private RadioButton rbtnPresencial;

   
    @FXML
    private RadioButton rbtnVirtual;
    
   
    @FXML
    private ComboBox<Persona> cmbInstructor;
    
    // Lista Observable asociada al Combo    
    private ObservableList<Persona> listaInstructores = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuramos los Radios Buttons
        configurarToggleGroup();
        // Configuramos la lista Obserbable
        cmbInstructor.setItems(listaInstructores);
        // Actualziamos La Combo
    }
    

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
        rbtnPresencial.setSelected(true);
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

    Persona getInstructor() {
        if (cmbInstructor == null || cmbInstructor.getSelectionModel() == null) {
            throw new IllegalStateException("ComboBox no inicializado correctamente");
        }

        Persona unInstructor = cmbInstructor.getSelectionModel().getSelectedItem();
        if (unInstructor == null) {
            throw new IllegalArgumentException("Debe seleccionar un curador");
        }
        return unInstructor;
    }

}
