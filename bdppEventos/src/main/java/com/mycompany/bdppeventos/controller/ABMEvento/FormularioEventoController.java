package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FormularioEventoController extends ConfiguracionIguEvento implements Initializable  {

    // Elementos del Controlador
    @FXML
    private ComboBox<EstadoEvento> cmbEstado;  // ← Tipado con enum. /////IMPORTANTISIMO DEFINIR EL TIPO DE DATO DEL ENUM

    @FXML
    private CheckBox chkCupoMaximo;

    @FXML
    private TextField txtCupoMaximo;
   
    
    
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;  // ← Tipado con enum /////IMPORTANTISIMO DEFINIR EL TIPO DE DATO DEL ENUM
    
    @FXML
    private AnchorPane contenedorDinamico;
    
    // Controladores clase Hijas
    private PanelExposicionController unPanelExposicionController;
    private PanelTallerController unPanelTallerController;
    private PanelConciertoController unPanelConciertoController;
    private PanelCicloCineController unPanelCicloCineController;
    private PanelFeriaController unPanelFeriaController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstadoEvento();
        cargarTipoEvento();
    }

    // Metodos Especificos
    private void cargarEstadoEvento() {
        cmbEstado.getItems().addAll(EstadoEvento.values());  // Al pasarle como parametro por defecto utiliza el @ToString(). El .Values me permite obtener un Array de todos los elementos del enum
    }
  
    private void cargarTipoEvento() {
        cmbTipoEvento.getItems().addAll(TipoEvento.values());  // ← Directamente el enum
    }

    private Parent cargarPanelExposicion() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelExposicion.fxml"));
    }

    private Parent cargarPanelTaller() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelTaller.fxml"));
    }
    
    private Parent cargarPanelConcierto() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelConcierto.fxml"));
    }
    
    private Parent cargarPanelCicloCine() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelCicloCine.fxml"));
    }
    
    private Parent cargarPanelFeria() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/ABMEvento/PanelFeria.fxml"));
    }
    
    // Metodos de los controles
    @FXML
    private void configuracionCupoMaximo() {
        configuracionCheckTextfield(chkCupoMaximo,txtCupoMaximo);
    }

    
    @FXML
    private void onTipoEventoChanged() {
        TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();  // ← Sin casting. Obtengo el ENUM
        
        if (tipoSeleccionado == null) {
            return;  // Si no hay selección, no hacer nada
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

    @FXML
    private void altaEvento() {        
        // TODO: Implementar lógica para dar de alta el evento
    }
}