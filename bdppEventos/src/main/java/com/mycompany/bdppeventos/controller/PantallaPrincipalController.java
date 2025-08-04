package com.mycompany.bdppeventos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PantallaPrincipalController implements Initializable {

    // Elementos de la interfaz 
    @FXML
    private ToggleButton btnPanelAdmin, btnGestionEventos, btnGestionPersonas,
            btnInscribirParticipante, btnCalendarioEventos, btnVerParticipantes, btnControlEstado;
    @FXML
    private ImageView iconPanelAdmin, iconGestionEventos, iconGestionPersonas,
            iconInscribirParticipante, iconCalendarioEventos, iconVerParticipantes, iconControlEstado;
    @FXML
    private Button btnMinimizar, btnCerrar;
    @FXML
    private AnchorPane centerContainer;

    private ToggleGroup menuGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarToggleGroup();
        configurarIconos();
        configurarBotonesVentana();
        seleccionarPanelAdminPorDefecto();
    }

    // Método para configurar iconos
    private void configurarIconos() {
        configurarIcono(btnPanelAdmin, iconPanelAdmin, "PanelPrincipalIcon");
        configurarIcono(btnGestionEventos, iconGestionEventos, "EventoMas");
        configurarIcono(btnGestionPersonas, iconGestionPersonas, "PersonaMas");
        configurarIcono(btnInscribirParticipante, iconInscribirParticipante, "ParticipanteMas");
        configurarIcono(btnCalendarioEventos, iconCalendarioEventos, "CalendarioEventos");
        configurarIcono(btnVerParticipantes, iconVerParticipantes, "ParticipantesVista");
        configurarIcono(btnControlEstado, iconControlEstado, "Control");
    }

    private void configurarIcono(ToggleButton boton, ImageView icono, String nombreBaseIcono) {
        boton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            String color;
            if (newVal) {
                color = "Blanco";
            } else {
                color = "Azul";
            }

            String ruta = "/images/" + nombreBaseIcono + color + ".png";
            URL imageUrl = getClass().getResource(ruta);

            if (imageUrl != null) {
                icono.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarToggleGroup() {
        menuGroup = new ToggleGroup();

        btnPanelAdmin.setToggleGroup(menuGroup);
        btnGestionEventos.setToggleGroup(menuGroup);
        btnGestionPersonas.setToggleGroup(menuGroup);
        btnInscribirParticipante.setToggleGroup(menuGroup);
        btnCalendarioEventos.setToggleGroup(menuGroup);
        btnVerParticipantes.setToggleGroup(menuGroup);
        btnControlEstado.setToggleGroup(menuGroup);

        menuGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });
    }

    private void configurarBotonesVentana() {
        // Boton "X": Basicamnete cierra el programa
        btnCerrar.setOnAction(e -> System.exit(0));
        // Boton "-": Basicamente minimiza
        btnMinimizar.setOnAction(e -> {
            Stage stage = (Stage) btnMinimizar.getScene().getWindow();
            stage.setIconified(true);
        });
        
    }

    private void seleccionarPanelAdminPorDefecto() {
        btnPanelAdmin.setSelected(true);
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.PanelAdministracion);
    }

    
   
        
    @FXML
    private void cargarVistaPanelAdmin() {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.PanelAdministracion);
    }

    @FXML
    private void cargarVistaGestionEventos() {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.FormularioEvento);
    }
    
    @FXML
    private void cargarVistaControlEstado()
    {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.PanelControlEstado);
    }

    @FXML
    private void cargarVistaGestionPersonas() {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.ListaDePersonas);
    }

    @FXML
    private void cargarVistaInscribirParticipante() {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.PanelInscribirParticipantes);
    }

    @FXML
    private void cargarVistaCalendarioEventos() {
        // TODO: Implementar view
    }

    @FXML
    private void cargarVistaVerParticipantes() {
        StageManager.cambiarEscenaEnContenedor(centerContainer, Vista.PanelVerParticipantes);
    }
}
