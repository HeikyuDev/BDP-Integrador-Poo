
/**
 * Controlador del panel de feria para el ABM de eventos.
 * Permite gestionar la cantidad de stands y el tipo de cobertura de la feria.
 */
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

public class PanelFeriaController extends ConfiguracionIgu implements Initializable {

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

    void cargarDatos(Feria feria) {        
        txtCantidadStands.setText(String.valueOf(feria.getCantidadStands()));
        cmbTipoCobertura.setValue(feria.getTipoCobertura());
    }
}
