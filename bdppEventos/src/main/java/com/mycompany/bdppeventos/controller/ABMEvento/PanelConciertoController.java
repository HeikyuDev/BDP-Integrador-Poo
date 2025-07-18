package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class PanelConciertoController extends ConfiguracionIguEvento implements Initializable {


    @FXML
    private CheckBox chkEsPago;
    
    @FXML
    private TextField txtEsPago;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
    }    
    
    
    
    // Metodos específicos
    
    
    // Metodos de Controles
    @FXML
    private void configurarEsPago()
    {
        configuracionCheckTextfield(chkEsPago, txtEsPago);
    }
    
}
