package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.Feria;
import java.net.URL;
import java.util.ResourceBundle;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PanelFeriaController  implements Initializable {
       
    @FXML
    private TextField txtCantidadStands;
   
    @FXML
    private ComboBox<TipoCobertura> cmbTipoCobertura;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // mostramos los tipos de cobertura disponibles en el comboBox
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoCobertura, TipoCobertura.class);
    }

    // Metodos Especificos

    public int getCantidadStands() {
    StringBuilder mensajeError = new StringBuilder();
    String cantidadTexto = txtCantidadStands.getText().trim();
    
    // Validar que no esté vacío
    if (cantidadTexto.isEmpty()) {
        mensajeError.append("• Debe ingresar la cantidad de stands.\n");
    } else {
        // Validar que sea un número válido
        try {
            int cantidad = Integer.parseInt(cantidadTexto);
            
            // Validar que sea mayor que cero
            if (cantidad <= 0) {
                mensajeError.append("• La cantidad de stands debe ser mayor que cero.\n");
            } else {
                // Si todo está bien, retornar el valor
                return cantidad;
            }
        } catch (NumberFormatException e) {
            mensajeError.append("• La cantidad de stands debe ser un número entero válido.\n");
        }
    }
    
    // Si hay errores, lanzar la excepción
    if (mensajeError.length() > 0) {
        throw new IllegalArgumentException(mensajeError.toString());
    }
    
    // Esta línea nunca debería ejecutarse, pero Java requiere un return
    return 0;
}
    protected void limpiarCampos()
    {
        txtCantidadStands.setText("");
        cmbTipoCobertura.getSelectionModel().clearSelection();
    }
        
    public TipoCobertura getTipoCobertura() {
        TipoCobertura tipo = cmbTipoCobertura.getSelectionModel().getSelectedItem();
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de Cobertura inválido, por favor asegurese de seleccionar uno.");
        }
        return tipo;
    }

    protected void cargarDatos(Feria feria) {        
        txtCantidadStands.setText(String.valueOf(feria.getCantidadStands()));
        cmbTipoCobertura.setValue(feria.getTipoCobertura());
    }
}
