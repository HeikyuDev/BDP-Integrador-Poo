
/**
 * Controlador del panel de feria para el ABM de eventos.
 * Permite gestionar la cantidad de stands y el tipo de cobertura de la feria.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

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
        String cantidadTexto = txtCantidadStands.getText().trim();
        if (cantidadTexto.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar la cantidad de stands.");
        }
        try {
            int cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad de stands debe ser mayor que cero.");
            }
            return cantidad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad de stands debe ser un número entero válido.");
        }
    }

    public TipoCobertura getTipoCobertura() {
        TipoCobertura tipo = cmbTipoCobertura.getSelectionModel().getSelectedItem();
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de Cobertura inválido, por favor asegurese de seleccionar uno.");
        }
        return tipo;
    }
}
