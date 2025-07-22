/**
 * Controlador principal para el formulario de alta, baja y modificación de eventos.
 * Gestiona la lógica de la interfaz gráfica y la carga dinámica de paneles según el tipo de evento seleccionado.
 */

package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

    /** Textfield para ingresar el nombre del evento */
    @FXML
    private TextField txtNombre;

    /** Textfield para ingresar la ubicacion del evento */
    @FXML
    private TextField txtUbicacion;

    /** DatePicker para seleccionar la fecha de inicio del evento */
    @FXML
    private DatePicker dpFechaInicio;

    /** Textfield para ingresar la Duracion del evento */
    @FXML
    private TextField txtDuracion;

    @FXML
    private CheckBox chkTieneInscripcion;

    /**
     * Contenedor dinámico donde se cargan los paneles específicos según el tipo de
     * evento
     */
    @FXML
    private AnchorPane contenedorDinamico;

    // Controladores de paneles hijos (para lógica específica de cada tipo de
    // evento)
    private PanelExposicionController unPanelExposicionController;
    private PanelTallerController unPanelTallerController;
    private PanelConciertoController unPanelConciertoController;
    private PanelCicloCineController unPanelCicloCineController;
    private PanelFeriaController unPanelFeriaController;

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
                unPanelExposicionController = StageManager
                        .cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, Vista.PanelExposicion);
            }
            case TALLER -> {
                chkCupoMaximo.setSelected(true);
                chkCupoMaximo.setDisable(true);
                unPanelTallerController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico,
                        Vista.PanelTaller);
            }

            case CONCIERTO -> {
                chkCupoMaximo.setDisable(false);
                unPanelConciertoController = StageManager
                        .cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, Vista.PanelConcierto);
            }
            case CICLO_DE_CINE -> {
                chkCupoMaximo.setDisable(false);
                unPanelCicloCineController = StageManager
                        .cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, Vista.PanelCicloCine);
            }
            case FERIA -> {
                chkCupoMaximo.setDisable(false);
                unPanelFeriaController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico,
                        Vista.PanelFeria);
            }
        }
    }

    /**
     * Método para dar de alta un evento (a implementar).
     * Aquí se debe colocar la lógica para guardar el evento en la base de datos.
     */

    @FXML
    private void altaEvento() {
        try {
            // Validación campos generales
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            String duracionTexto = txtDuracion.getText().trim();
            EstadoEvento estado = cmbEstado.getSelectionModel().getSelectedItem();
            TipoEvento tipoEvento = cmbTipoEvento.getSelectionModel().getSelectedItem();

            if (esCampoVacio(nombre) || esCampoVacio(ubicacion) || fechaInicio == null ||
                    esCampoVacio(duracionTexto) || estado == null || tipoEvento == null) {
                throw new IllegalArgumentException("Debe completar todos los campos obligatorios.");
            }

            int duracion = Integer.parseInt(duracionTexto);

            boolean tieneCupo = chkCupoMaximo.isSelected();
            int cupoMaximo = 0;
            if (tieneCupo) {
                String cupoTexto = txtCupoMaximo.getText().trim();
                if (esCampoVacio(cupoTexto)) {
                    throw new IllegalArgumentException("Debe ingresar el cupo máximo si está seleccionado.");
                }
                cupoMaximo = Integer.parseInt(cupoTexto);
            }

            boolean tieneInscripcion = chkTieneInscripcion.isSelected();

            // Datos específicos según tipo de evento, obtenidos con validación en cada
            // panel
            Object[] datosEspecificos = null;
            switch (tipoEvento) {
                case EXPOSICION -> {
                    TipoDeArte tipoDeArte = unPanelExposicionController.getTipoArteSeleccionado();
                    if (tipoDeArte == null) {
                        throw new IllegalArgumentException("Debe seleccionar un tipo de arte.");
                    }
                    datosEspecificos = new Object[] { tipoDeArte };
                }
                case TALLER -> {
                    boolean esPresencial = unPanelTallerController.getEsPrecencial();
                    datosEspecificos = new Object[] { esPresencial };
                }
                case CONCIERTO -> {
                    boolean esPago = unPanelConciertoController.getEsPago();
                    double monto = 0.0;
                    if (esPago) {
                        monto = unPanelConciertoController.getMonto(); 
                    }
                    datosEspecificos = new Object[] { esPago, monto };
                }
                case CICLO_DE_CINE -> {
                    Proyeccion proyeccion = unPanelCicloCineController.getProyeccion();
                    if (proyeccion == null) {
                        throw new IllegalArgumentException("Debe seleccionar una proyección.");
                    }
                    boolean charlasPosteriores = unPanelCicloCineController.getCharlasPosteriores();
                    datosEspecificos = new Object[] { proyeccion, charlasPosteriores };
                }
                case FERIA -> {
                    int cantidadStands = unPanelFeriaController.getCantidadStands(); 
                    TipoCobertura tipoCobertura = unPanelFeriaController.getTipoCobertura(); // Valida no null
                    datosEspecificos = new Object[] { cantidadStands, tipoCobertura };
                }
            }

            // Aquí iría la lógica para guardar el evento usando los datos recolectados...

            System.out.println("Datos específicos: " + java.util.Arrays.toString(datosEspecificos));

        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: " + e.getMessage());
        } catch (Exception e) {
            Alerta.mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean esCampoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

}