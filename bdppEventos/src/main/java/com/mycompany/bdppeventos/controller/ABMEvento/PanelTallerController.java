package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PanelTallerController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarToggleGroup();
    }

    // Controles
    @FXML
    private RadioButton rbtnPresencial;
    
    @FXML
    private RadioButton rbtnVirtual;
    
    private ToggleGroup rBtnGroup = new ToggleGroup();;
            
    
    // Metodos especificos
    
    private void configurarToggleGroup()
    {
        rbtnPresencial.setToggleGroup(rBtnGroup);
        rbtnVirtual.setToggleGroup(rBtnGroup);
        
        rBtnGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {                
                oldToggle.setSelected(true);
            }
        });
    }
}
