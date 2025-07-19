package com.mycompany.bdppeventos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
            btnInscribirParticipante, btnCalendarioEventos, btnVerParticipantes;
    @FXML
    private ImageView iconPanelAdmin, iconGestionEventos, iconGestionPersonas,
            iconInscribirParticipante, iconCalendarioEventos, iconVerParticipantes;
    @FXML
    private Button btnMinimizar, btnMaximizar, btnCerrar;
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

        menuGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });
    }

    private void configurarBotonesVentana() {
        btnCerrar.setOnAction(e -> System.exit(0));

        btnMinimizar.setOnAction(e -> {
            Stage stage = (Stage) btnMinimizar.getScene().getWindow();
            stage.setIconified(true);
        });

        btnMaximizar.setOnAction(e -> {
            Stage stage = (Stage) btnMaximizar.getScene().getWindow();
            if (stage.isMaximized()) {
                stage.setMaximized(false);
                btnMaximizar.setText("□");
            } else {
                stage.setMaximized(true);
                btnMaximizar.setText("❐");
            }
        });
    }

    private void seleccionarPanelAdminPorDefecto() {
        btnPanelAdmin.setSelected(true);
    }

    // Método centralizado para cargar vistas
    private void cargarVista(String rutaFXML) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(rutaFXML));
            mostrarVistaEnContenedor(vista);
        } catch (IOException ex) {
            mostrarError("Error al cargar vista", "No se pudo cargar: " + rutaFXML);
        }
    }

    private void mostrarVistaEnContenedor(Parent vista) {
        centerContainer.getChildren().clear();
        centerContainer.getChildren().add(vista);

        AnchorPane.setTopAnchor(vista, 0.0);
        AnchorPane.setRightAnchor(vista, 0.0);
        AnchorPane.setBottomAnchor(vista, 0.0);
        AnchorPane.setLeftAnchor(vista, 0.0);
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos de navegación (simplificados)
    @FXML
    private void cargarVistaPanelAdmin() {
        cargarVista("/fxml/PanelAdmin.fxml");
    }

    @FXML
    private void cargarVistaGestionEventos() {
        cargarVista("/fxml/ABMEvento/FormularioEvento.fxml");
    }

    @FXML
    private void cargarVistaGestionPersonas() {
        cargarVista("/fxml/ABMPersona/FormularioPersona.fxml");
    }

    @FXML
    private void cargarVistaInscribirParticipante() {
        cargarVista("/fxml/InscripcionParticipante.fxml");
    }

    @FXML
    private void cargarVistaCalendarioEventos() {
        cargarVista("/fxml/CalendarioEventos.fxml");
    }

    @FXML
    private void cargarVistaVerParticipantes() {
        cargarVista("/fxml/VerParticipantes.fxml");
    }
}
