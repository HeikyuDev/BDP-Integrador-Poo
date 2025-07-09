package com.mycompany.bdppeventos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller para la pantalla principal del sistema Maneja la navegación del
 * menú lateral y cambio de íconos
 */
public class PantallaPrincipalController implements Initializable {

    
    // --- Botones del menú lateral ---
    @FXML
    private ToggleButton btnPanelAdmin;
    @FXML
    private ToggleButton btnGestionEventos;
    @FXML
    private ToggleButton btnGestionPersonas;
    @FXML
    private ToggleButton btnInscribirParticipante;
    @FXML
    private ToggleButton btnCalendarioEventos;
    @FXML
    private ToggleButton btnVerParticipantes;

    // --- Íconos de los botones ---
    @FXML
    private ImageView iconPanelAdmin;
    @FXML
    private ImageView iconGestionEventos;
    @FXML
    private ImageView iconGestionPersonas;
    @FXML
    private ImageView iconInscribirParticipante;
    @FXML
    private ImageView iconCalendarioEventos;
    @FXML
    private ImageView iconVerParticipantes;

    // --- Botones de ventana ---
    @FXML
    private Button btnMinimizar; //si o si se tiene que llamar igual que el elemento Button cuya fx:id es btnMinimizar De lo contrario Excepcion
    @FXML
    private Button btnMaximizar; //si o si se tiene que llamar igual que el elemento Button cuya fx:id es btnMaximizar De lo contrario Excepcion
    @FXML
    private Button btnCerrar; //si o si se tiene que llamar igual que el elemento Button cuya fx:id es btnCerrar De lo contrario Excepcion

    // --- Área de contenido dinámico ---
    @FXML
    private AnchorPane centerContainer;


    private ToggleGroup menuGroup;

    // ==================================================================================
    // INICIALIZACIÓN
    // ==================================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando PantallaPrincipalController...");

        // Configuracion necesaria para que solamente se pueda seleccionar un solo ToggleButton a la vez y que no se pueda deseleccionar ninguno
        configurarToggleGroup(); 

        // Configuración de iconos para que se muestre el icono correcto según el botón seleccionado
        configurarCambiosIconos();
        // Configuración de eventos de navegación. 
        configurarEventosNavegacion();
        configurarBotonesVentana();

        // Seleccionar Panel de Administración por defecto
        btnPanelAdmin.setSelected(true);

        System.out.println("PantallaPrincipalController inicializado correctamente");
    }

    // ==================================================================================
    // CONFIGURACIÓN INICIAL
    // ==================================================================================
    /**
     * Configura el ToggleGroup para que solo un botón esté activo a la vez
     */
    private void configurarToggleGroup() {
        menuGroup = new ToggleGroup();

        btnPanelAdmin.setToggleGroup(menuGroup);
        btnGestionEventos.setToggleGroup(menuGroup);
        btnGestionPersonas.setToggleGroup(menuGroup);
        btnInscribirParticipante.setToggleGroup(menuGroup);
        btnCalendarioEventos.setToggleGroup(menuGroup);
        btnVerParticipantes.setToggleGroup(menuGroup);

        // Prevenir deselección completa - MEJORADO
        menuGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {                
                oldToggle.setSelected(true);
            }
        });

        System.out.println("ToggleGroup configurado con selección obligatoria");
    }

    /**
     * Configura los eventos de navegación
     */
    private void configurarEventosNavegacion() {
        btnPanelAdmin.setOnAction(e -> cargarVistaPanelAdmin()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaPanelAdmin
        btnGestionEventos.setOnAction(e -> cargarVistaGestionEventos()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaGestionEventos
        btnGestionPersonas.setOnAction(e -> cargarVistaGestionPersonas()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaGestionPersonas
        btnInscribirParticipante.setOnAction(e -> cargarVistaInscribirParticipante()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaInscribirParticipante
        btnCalendarioEventos.setOnAction(e -> cargarVistaCalendarioEventos()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaCalendarioEventos
        btnVerParticipantes.setOnAction(e -> cargarVistaVerParticipantes()); // Le seteo al btn que cuando sea presionado se ejecute el método cargarVistaVerParticipantes

        System.out.println("Eventos de navegación configurados");
    }

    /**
     * Configura los botones de ventana (minimizar, maximizar, cerrar)
     */
    private void configurarBotonesVentana() {
        btnCerrar.setOnAction(e -> {
            System.out.println("Cerrando aplicación...");
            System.exit(0);
        });

        btnMinimizar.setOnAction(e -> {
            System.out.println("Minimizando ventana...");
            Stage stage = (Stage) btnMinimizar.getScene().getWindow();
            stage.setIconified(true);
        });

        btnMaximizar.setOnAction(e -> {
            System.out.println("Maximizar/Restaurar ventana...");
            Stage stage = (Stage) btnMaximizar.getScene().getWindow();

            if (stage.isMaximized()) {
                stage.setMaximized(false);
                btnMaximizar.setText("□");
            } else {
                stage.setMaximized(true);
                btnMaximizar.setText("❐");
            }
        });

        System.out.println("Botones de ventana configurados");
    }

    
    /**
     * Configura los listeners para cambiar íconos automáticamente
     */
    private void configurarCambiosIconos() {
        configurarIconoPanelAdmin();
        configurarIconoGestionEventos();
        configurarIconoGestionPersonas();
        configurarIconoInscribirParticipante();
        configurarIconoCalendarioEventos();
        configurarIconoVerParticipantes();

        System.out.println("Listeners de cambio de íconos configurados");
    }

    private void configurarIconoPanelAdmin() {
        btnPanelAdmin.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/PanelPrincipalIconBlanco.png");
                iconPanelAdmin.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/PanelPrincipalIconAzul.png");
                iconPanelAdmin.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarIconoGestionEventos() {
        btnGestionEventos.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/EventoMasBlanco.png");
                iconGestionEventos.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/EventoMasAzul.png");
                iconGestionEventos.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarIconoGestionPersonas() {
        btnGestionPersonas.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/PersonaMasBlanco.png");
                iconGestionPersonas.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/PersonaMasAzul.png");
                iconGestionPersonas.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarIconoInscribirParticipante() {
        btnInscribirParticipante.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/ParticipanteMasBlanco.png");
                iconInscribirParticipante.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/ParticipanteMasAzul.png");
                iconInscribirParticipante.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarIconoCalendarioEventos() {
        btnCalendarioEventos.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/CalendarioEventosBlanco.png");
                iconCalendarioEventos.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/CalendarioEventosAzul.png");
                iconCalendarioEventos.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    private void configurarIconoVerParticipantes() {
        btnVerParticipantes.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                URL imageUrl = getClass().getResource("/images/ParticipantesVistaBlanco.png");
                iconVerParticipantes.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL imageUrl = getClass().getResource("/images/ParticipantesVistaAzul.png");
                iconVerParticipantes.setImage(new Image(imageUrl.toExternalForm()));
            }
        });
    }

    // ==================================================================================
    // NAVEGACIÓN ENTRE VISTAS
    // ==================================================================================
    private void cargarVistaPanelAdmin() {
        mostrarMensajeTemporalmente("Cargando Panel de Administración...");
        // TODO: Implementar carga de vista real
    }

    private void cargarVistaGestionEventos() {
    try {
        System.out.println("=== DEBUG CARGA FXML ===");
        
        URL fxmlUrl = getClass().getResource("/fxml/ABMEvento/FormularioEvento.fxml");
        System.out.println("URL encontrada: " + fxmlUrl);
        
        if (fxmlUrl == null) {
            System.err.println("ERROR: Archivo no encontrado");
            return;
        }
        
        System.out.println("Creando FXMLLoader...");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);  // ⭐ CAMBIO CLAVE
        
        System.out.println("Cargando FXML...");
        Parent vistaEvento = loader.load();

        System.out.println("FXML cargado, configurando contenedor...");
        centerContainer.getChildren().clear();
        centerContainer.getChildren().add(vistaEvento);

        AnchorPane.setTopAnchor(vistaEvento, 0.0);
        AnchorPane.setRightAnchor(vistaEvento, 0.0);
        AnchorPane.setBottomAnchor(vistaEvento, 0.0);
        AnchorPane.setLeftAnchor(vistaEvento, 0.0);
        
        System.out.println("✅ Vista cargada exitosamente");
        
    } catch (Exception ex) {
        System.err.println("❌ Error detallado:");
        ex.printStackTrace();
    }
}

    private void cargarVistaGestionPersonas() {
        mostrarMensajeTemporalmente("Cargando Gestión de Personas...");
        
        //IMPLEMENTACION

        try {
            System.out.println("Cargando FXML...");
            System.out.println(getClass().getResource("/fxml/ABMPersona/FormularioPersona.fxml"));

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ABMPersona/FormularioPersona.fxml"));
            Parent vistaPersonas = loader.load(); // Cambiar AnchorPane por Parent

            // reemplazar contenido del centerContainer
            centerContainer.getChildren().clear();
            centerContainer.getChildren().add(vistaPersonas);

            // hacer que se estire con el container
            AnchorPane.setTopAnchor(vistaPersonas, 0.0);
            AnchorPane.setRightAnchor(vistaPersonas, 0.0);
            AnchorPane.setBottomAnchor(vistaPersonas, 0.0);
            AnchorPane.setLeftAnchor(vistaPersonas, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarVistaInscribirParticipante() {
        mostrarMensajeTemporalmente("Cargando Inscribir Participante...");
        // TODO: Implementar carga de vista real
    }

    private void cargarVistaCalendarioEventos() {
        mostrarMensajeTemporalmente("Cargando Calendario de Eventos...");
        // TODO: Implementar carga de vista real
    }

    private void cargarVistaVerParticipantes() {
        mostrarMensajeTemporalmente("Cargando Ver Participantes...");
        // TODO: Implementar carga de vista real
    }

    // ==================================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================================
    /**
     * Muestra un mensaje temporal en el área de contenido
     */
    private void mostrarMensajeTemporalmente(String mensaje) {
        System.out.println(">>> " + mensaje);
        // TODO: Crear Label temporal en centerContainer
    }
}
