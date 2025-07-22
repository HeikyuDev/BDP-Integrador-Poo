
/**
 * Controlador del panel de exposición para el ABM de eventos.
 * Permite gestionar la lógica asociada a la exposición, como agregar tipos de arte.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PanelExposicionController implements Initializable {

    @FXML
    private ComboBox<TipoDeArte> cmbTipoArte;

    /**
     * Botón para agregar un nuevo tipo de arte a la exposición.
     */
    @FXML
    private Button btnAgregarTipoArte;

    /**
     * Inicializa el panel de exposición al cargar la vista.
     * Aquí se puede agregar lógica de inicialización si es necesario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: agregar lógica de inicialización si es necesario
    }

    // Metodos de Controles

    /**
     * Acción asociada al botón para agregar un tipo de arte.
     * Actualmente solo imprime un mensaje por consola.
     */
    @FXML
    private void agregarTipoArte() {
        StageManager.abrirModal(Vista.FormularioTipoDeArte);
    }

    // Metodos específicos
    public TipoDeArte getTipoArteSeleccionado() {
        TipoDeArte tipo = cmbTipoArte.getSelectionModel().getSelectedItem();
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de arte");
        }
        return tipo;
    }
}
