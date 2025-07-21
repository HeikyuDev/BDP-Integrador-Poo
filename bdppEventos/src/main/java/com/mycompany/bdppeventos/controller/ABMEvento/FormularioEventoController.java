
/**
 * Controlador principal para el formulario de alta, baja y modificación de eventos.
 * Gestiona la lógica de la interfaz gráfica y la carga dinámica de paneles según el tipo de evento seleccionado.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Clase controladora para la pantalla de ABM de eventos.
 * Extiende ConfiguracionIguEvento para reutilizar lógica de controles.
 * Implementa Initializable para inicializar combos y controles al cargar la
 * vista.
 */
public class FormularioEventoController extends ConfiguracionIgu implements Initializable {

    // Elementos del Controlador (vinculados con FXML)

    /** ComboBox para seleccionar el estado del evento (usa enum EstadoEvento) */
    @FXML
    private ComboBox<EstadoEvento> cmbEstado;

    /** CheckBox para indicar si el evento tiene cupo máximo */
    @FXML
    private CheckBox chkCupoMaximo;

    /** Campo de texto para ingresar el cupo máximo */
    @FXML
    private TextField txtCupoMaximo;

    /** ComboBox para seleccionar el tipo de evento (usa enum TipoEvento) */
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;

    /**
     * Contenedor dinámico donde se cargan los paneles específicos según el tipo de
     * evento
     */
    @FXML
    private AnchorPane contenedorDinamico;

    // Controladores de paneles hijos (para lógica específica de cada tipo de
    // evento)
    private PanelExposicionController unPanelExposicionController = new PanelExposicionController();
    private PanelTallerController unPanelTallerController = new PanelTallerController();
    private PanelConciertoController unPanelConciertoController = new PanelConciertoController();
    private PanelCicloCineController unPanelCicloCineController = new PanelCicloCineController();
    private PanelFeriaController unPanelFeriaController = new PanelFeriaController();

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * Carga los valores de los combos de estado y tipo de evento.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configuracionEnumEnCombo(cmbEstado, EstadoEvento.class);
        configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
    }

    


    /**
     * Configura la relación entre el CheckBox de cupo máximo y el TextField
     * asociado.
     * Se llama cuando el usuario interactúa con el CheckBox.
     */
    @FXML
    private void configuracionCupoMaximo() {
        configuracionCheckTextfield(chkCupoMaximo, txtCupoMaximo);
    }

    /**
     * Maneja el cambio de selección en el ComboBox de tipo de evento.
     * Carga el panel correspondiente y ajusta los controles según el tipo
     * seleccionado.
     */
    @FXML
    private void onTipoEventoChanged() {
        TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
        if (tipoSeleccionado == null) {
            return;
        }
        switch (tipoSeleccionado) {
            case EXPOSICION -> {
                chkCupoMaximo.setDisable(false);
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelExposicion);
            }
            case TALLER -> {                
                unPanelTallerController.configurarCupo(chkCupoMaximo, txtCupoMaximo);
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelTaller);
            }

            case CONCIERTO -> {
                chkCupoMaximo.setDisable(false);
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelConcierto);
            }
            case CICLO_DE_CINE -> {
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelCicloCine);
                chkCupoMaximo.setDisable(false);
            }
            case FERIA -> {
                chkCupoMaximo.setDisable(false);
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelFeria);
            }
        }
    }

    /**
     * Método para dar de alta un evento (a implementar).
     * Aquí se debe colocar la lógica para guardar el evento en la base de datos.
     */
    @FXML
    private void altaEvento() {
        // TODO: Implementar lógica para dar de alta el evento
    }
}