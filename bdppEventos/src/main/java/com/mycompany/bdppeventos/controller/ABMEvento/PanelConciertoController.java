package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.enums.TipoConcierto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;


public class PanelConciertoController implements Initializable {


    @FXML
    private ComboBox<TipoConcierto> cmbTipoConcierto;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTipoConcierto();
    }    
    
    
    
    // Metodos específicos
    
    private void cargarTipoConcierto()
    {
        cmbTipoConcierto.getItems().addAll(TipoConcierto.values());
    }
}
