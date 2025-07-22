
/**
 * Panel controlador para la gestión de ciclos de cine dentro del ABM de eventos.
 * Permite inicializar la vista y manejar la acción de agregar un nuevo ciclo de cine.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class PanelCicloCineController implements Initializable {

    @FXML
    private ComboBox<Proyeccion> cmbCiclo;

    @FXML
    private CheckBox ChkCharlasPosteriores;

    /**
     * Inicializa el panel de ciclo de cine al cargar la vista.
     * Aquí se debe implementar la lógica para mostrar todos los ciclos cargados en
     * la base de datos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // IMPLEMENTAR: cargar y mostrar todos los ciclos de cine desde la base de datos
    }

    @FXML
    private void agregarCiclo() {
        StageManager.abrirModal(Vista.FormularioProyeccion);
    }

    // Metodos Especificos

    public Proyeccion getProyeccion() {
        Proyeccion seleccion = cmbCiclo.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            throw new IllegalArgumentException("Debe seleccionar una proyección.");
        }
        return seleccion;
    }

    public boolean getCharlasPosteriores()
    {
        return ChkCharlasPosteriores.isSelected();
    }

}
