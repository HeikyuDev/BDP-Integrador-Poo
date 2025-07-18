package com.mycompany.bdppeventos.controller.ABMEvento;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class ConfiguracionIguEvento {
    
    
    /*
    
    Clase dirigida a reutilizar metodos de las interfaces de la seccion "ABM EVENTOS"
        
    */
    
    
    protected void configuracionCheckTextfield(CheckBox unCheckbox, TextField unTextfield)
    {
        if (unCheckbox.isSelected()) {
            unTextfield.setDisable(false);
            unTextfield.setEditable(true);
        } else {
            unTextfield.setText("");
            unTextfield.setDisable(true);
        }
    }
    
}
