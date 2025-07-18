package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PanelFeriaController implements Initializable {


    // Atributos
    
    
    @FXML 
    private TextField txtCantidadStands;
    
    @FXML
    private ComboBox<TipoCobertura> cmbTipoCobertura;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTipoCobertura();
    }    
    
    
    // Metodos Específicos
    
    private void cargarTipoCobertura()
    {
        cmbTipoCobertura.getItems().addAll(TipoCobertura.values());
    }
    
    
}
