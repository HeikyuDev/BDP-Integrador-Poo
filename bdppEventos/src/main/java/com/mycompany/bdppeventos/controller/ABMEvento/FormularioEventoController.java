
/**
 * Controlador principal para el formulario de alta, baja y modificación de eventos.
 * Gestiona la lógica de la interfaz gráfica y la carga dinámica de paneles según el tipo de evento seleccionado.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/**
 * Clase controladora para la pantalla de ABM de eventos.
 * Extiende ConfiguracionIguEvento para reutilizar lógica de controles.
 * Implementa Initializable para inicializar combos y controles al cargar la vista.
 */
public class FormularioEventoController extends ConfiguracionIguEvento implements Initializable  {


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

    /** Contenedor dinámico donde se cargan los paneles específicos según el tipo de evento */
    @FXML
    private AnchorPane contenedorDinamico;

    // Controladores de paneles hijos (para lógica específica de cada tipo de evento)
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
    
    // Métodos para cargar los paneles específicos según el tipo de evento

    /**
     * Carga el panel de Exposición y habilita el control de cupo máximo.
     */
    private Parent cargarPanelExposicion() throws IOException {
        chkCupoMaximo.setDisable(false);
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelExposicion.fxml"));
    }

    /**
     * Carga el panel de Taller y configura el cupo máximo por defecto.
     */
    private Parent cargarPanelTaller() throws IOException {
        unPanelTallerController.configurarCupo(chkCupoMaximo, txtCupoMaximo);
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelTaller.fxml"));
    }

    /**
     * Carga el panel de Concierto y habilita el control de cupo máximo.
     */
    private Parent cargarPanelConcierto() throws IOException {
        chkCupoMaximo.setDisable(false);
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelConcierto.fxml"));
    }

    /**
     * Carga el panel de Ciclo de Cine y habilita el control de cupo máximo.
     */
    private Parent cargarPanelCicloCine() throws IOException {
        chkCupoMaximo.setDisable(false);
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelCicloCine.fxml"));
    }

    /**
     * Carga el panel de Feria y habilita el control de cupo máximo.
     */
    private Parent cargarPanelFeria() throws IOException {
        chkCupoMaximo.setDisable(false);
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelFeria.fxml"));
    }
    

    // Métodos de los controles de la interfaz

    /**
     * Configura la relación entre el CheckBox de cupo máximo y el TextField asociado.
     * Se llama cuando el usuario interactúa con el CheckBox.
     */
    @FXML
    private void configuracionCupoMaximo() {
        configuracionCheckTextfield(chkCupoMaximo, txtCupoMaximo);
    }

    /**
     * Maneja el cambio de selección en el ComboBox de tipo de evento.
     * Carga el panel correspondiente y ajusta los controles según el tipo seleccionado.
     */
    @FXML
    private void onTipoEventoChanged() {
        TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
        if (tipoSeleccionado == null) {
            return;
        }
        try {
            Parent nuevoPanel = null;
            switch (tipoSeleccionado) {
                case EXPOSICION -> nuevoPanel = cargarPanelExposicion();
                case TALLER -> nuevoPanel = cargarPanelTaller();
                case CONCIERTO -> nuevoPanel = cargarPanelConcierto();
                case CICLO_DE_CINE -> nuevoPanel = cargarPanelCicloCine();
                case FERIA -> nuevoPanel = cargarPanelFeria();
                default -> System.out.println("Error: Tipo de evento no reconocido");
            }
            if (nuevoPanel != null) {
                contenedorDinamico.getChildren().setAll(nuevoPanel);
                AnchorPane.setTopAnchor(nuevoPanel, 0.0);
                AnchorPane.setRightAnchor(nuevoPanel, 0.0);
                AnchorPane.setBottomAnchor(nuevoPanel, 0.0);
                AnchorPane.setLeftAnchor(nuevoPanel, 0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
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