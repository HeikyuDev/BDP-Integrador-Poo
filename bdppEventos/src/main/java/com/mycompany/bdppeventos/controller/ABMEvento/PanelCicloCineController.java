package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.services.proyeccion.ProyeccionServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class PanelCicloCineController implements Initializable {

    @FXML
    private ComboBox<Proyeccion> cmbCiclo;

    // Lista Observable

    private ObservableList<Proyeccion> listaProyeccion = FXCollections.observableArrayList();

    @FXML
    private CheckBox ChkCharlasPosteriores;
    
    @FXML
    private Button btnAgregarCiclo;

    // Servicios
    private ProyeccionServicio proyeccionServicio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialimos los servicios        
        proyeccionServicio = new ProyeccionServicio(RepositorioContext.getRepositorio());
        // Vinculamos la ObservableList con el Combo
        cmbCiclo.setItems(listaProyeccion);
        // Actualizamos la combo para mostrar todas las Proyecciones Cargadas en BD
        actualizarCombo();
    }

    @FXML
    private void agregarCiclo() {
        // Limpiamos los Campos
        limpiarCampos();
        // Abrimos el Modal del ABM de Proyeccion
        StageManager.abrirModal(Vista.FormularioProyeccion);
        // Actualizamos el Combo para ver todas las proyecciones
        actualizarCombo();
    }

    // ==Metodos Especificos==

    protected void limpiarCampos() {
        cmbCiclo.getSelectionModel().clearSelection();
        ChkCharlasPosteriores.setSelected(false);
    }

    public Proyeccion getProyeccion() {
        Proyeccion seleccion = cmbCiclo.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            throw new IllegalArgumentException("Debe seleccionar una proyección.");
        }
        return seleccion;
    }

    public boolean getCharlasPosteriores() {
        return ChkCharlasPosteriores.isSelected();
    }

    // Metodo Que obtiene todas las proyecciones activas
    private ObservableList<Proyeccion> obtenerProyecciones() {
        // 1. Obtenemos la lista del servicio
        List<Proyeccion> lista = proyeccionServicio.buscarTodos();

        // 2. Verificamos si la lista es nula
        if (lista != null) {
            // 3. Si no es nula, la convertimos a ObservableList
            return FXCollections.observableArrayList(lista);
        } else {
            // 4. Si es nula, devolvemos una ObservableList vacía
            return FXCollections.observableArrayList();
        }
    }

    private void actualizarCombo() {
        try {
            // Borramos los elementos del CheckCombo
            cmbCiclo.getItems().clear();
            // Cargamos el Combo con todas las proyecciones Activas
            cmbCiclo.getItems().setAll(obtenerProyecciones());
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar proyecciones:" + e.getMessage());
        }
    }

    void cargarDatos(CicloDeCine cicloDeCine) {        
        cmbCiclo.setValue(cicloDeCine.getUnaProyeccion());
        ChkCharlasPosteriores.setSelected(cicloDeCine.isCharlasPosteriores());
    }


}
