package com.mycompany.bdppeventos.util;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public abstract class ConfiguracionIgu {
    
    /**
     * Clase dirigida a reutilizar metodos de las interfaces de la seccion "ABM EVENTOS".
     * Permite centralizar la lógica de configuración de controles en los formularios de eventos.
     */
    
    
    // Configuraciion que permite que Cuando se precione un Checkbox se Habilie el txtfield
    
    protected void configuracionCheckTextfield(CheckBox unCheckbox, TextField unTextfield) {
        if (unCheckbox.isSelected()) {
            unTextfield.setDisable(false);
            unTextfield.setEditable(true);
        } else {
            unTextfield.setText("");
            unTextfield.setDisable(true);
        }
    }
    
    // Configuracion que llena los comboBox
    
    public static <T extends Enum<T>> void configuracionEnumEnCombo(ComboBox<T> combo, Class<T> enumClass) {
        // Limpia Los elementos del combo antes
        combo.getItems().clear();
        // Obtiene todos los valores y los pone en el ComboBox:
        combo.getItems().addAll(enumClass.getEnumConstants());
    }


    
    
}
