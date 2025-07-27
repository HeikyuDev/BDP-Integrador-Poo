
/**
 * Controlador del panel de exposición para el ABM de eventos.
 * Permite gestionar la lógica asociada a la exposición, como agregar tipos de arte.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.services.TipoDeArte.TipoDeArteServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    
    //lista Obserable
    private ObservableList<TipoDeArte> listaTiposDeArtes = FXCollections.observableArrayList();
    
    //Servicios
    private TipoDeArteServicio tipoDeArteServicio;

    /**
     * Inicializa el panel de exposición al cargar la vista.
     * Aquí se puede agregar lógica de inicialización si es necesario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializamos el tipoDeArteServicio
        tipoDeArteServicio = new TipoDeArteServicio(RepositorioContext.getRepositorio());
        //Vinculamos la Observable List con la combo
        cmbTipoArte.setItems(listaTiposDeArtes);
        // Actualizamos la Combo
        actualizarCombo();
    }

    // Metodos de Controles

    /**
     * Acción asociada al botón para agregar un tipo de arte.
     * Actualmente solo imprime un mensaje por consola.
     */
    @FXML
    private void agregarTipoArte() {
        // Limpiamos los campos
        limpiarCampos();
        // Abrimos el Modal que nos permite hacer el ABM de Tipos de Arte
        StageManager.abrirModal(Vista.FormularioTipoDeArte);
        // Actualizamos la ComboBox
        actualizarCombo();
    }

    // Metodos específicos
    
    private void limpiarCampos()
    {
        cmbTipoArte.getSelectionModel().clearSelection();
    }
    
    public TipoDeArte getTipoArteSeleccionado() {
        TipoDeArte tipo = cmbTipoArte.getSelectionModel().getSelectedItem();
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de arte");
        }
        return tipo;
    }
    
    private ObservableList<TipoDeArte> obtenerTiposDeArte() {
        // 1. Obtenemos la lista del servicio
        List<TipoDeArte> lista = tipoDeArteServicio.buscarTodos();
        // 2. Verificamos si la lista es nula
        if (lista != null) {
            // 3. Si no es nula, la convertimos a ObservableList
            return FXCollections.observableArrayList(lista);
        } else {
            // 4. Si es nula, devolvemos una ObservableList vacía
            return FXCollections.observableArrayList();
        }
    }
    
    private void actualizarCombo()
    {
        try {
            // Obtenemos todos los registros de la base de datos y la almacenamos en un a lista
            cmbTipoArte.getItems().setAll(obtenerTiposDeArte());
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo Actualizar el ComboBox " + e.getMessage());
        }
    }
}
