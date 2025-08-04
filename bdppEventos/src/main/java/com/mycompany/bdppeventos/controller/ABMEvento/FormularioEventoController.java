package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.TipoCobertura;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Controlador principal para la gestión de eventos (CRUD).
 * Maneja el formulario de eventos y la tabla de visualización.
 */
public class FormularioEventoController extends ConfiguracionIgu implements Initializable {

    // ===============================
    // CONSTANTES
    // ===============================
    private static final String MENSAJE_EXITO_ALTA = "Evento registrado exitosamente";
    private static final String MENSAJE_EXITO_MODIFICACION = "Evento modificado exitosamente";
    private static final String MENSAJE_EXITO_ELIMINACION = "Evento eliminado correctamente";
    private static final String MENSAJE_ERROR_ELIMINACION = "Error: No se pudo eliminar el Evento";
    private static final String MENSAJE_ERROR_SELECCION = "Debe seleccionar un evento";
    private static final String TEXTO_BOTON_ALTA = "Dar de Alta";
    private static final String TEXTO_LIMITADO = "Ilimitado";
    private static final String TEXTO_SIN_ORGANIZADORES = "Sin Organizadores";

    // ===============================
    // ELEMENTOS FXML
    // ===============================
    @FXML
    private CheckBox chkCupoMaximo, chkTieneInscripcion;
    @FXML
    private TextField txtCupoMaximo, txtNombre, txtUbicacion, txtDuracion;
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private Button btnAlta, btnVisualizacion, btnBaja, btnModificacion, btnCancelar;

    // Tabla y columnas
    @FXML
    private TableView<Evento> tablaEventos;
    @FXML
    private TableColumn<Evento, String> colNombre, colUbicacion, colTieneInscripcion,
            colTieneCupo, colCapacidadMaxima, colOrganizadores, colTipo, colFechaInicio;

    @FXML
    private TableColumn<Evento, Integer> colDuracion;

    @FXML
    private CheckComboBox<Persona> chkComboOrganizadores;
    @FXML
    private AnchorPane contenedorDinamico;

    // ===============================
    // ATRIBUTOS DE INSTANCIA
    // ===============================
    private final ObservableList<Evento> listaEventos = FXCollections.observableArrayList();

    // Servicios
    private EventoServicio eventoServicio;
    private PersonaServicio personaServicio;

    // Controladores de paneles específicos
    private PanelExposicionController panelExposicionController;
    private PanelTallerController panelTallerController;
    private PanelConciertoController panelConciertoController;
    private PanelCicloCineController panelCicloCineController;
    private PanelFeriaController panelFeriaController;

    // Variable para modificación
    private Evento eventoEnEdicion = null;

    //
    /**
     * Inicializa el controlador al cargar la vista FXML. Carga los valores de
     * los combos de estado y tipo de evento.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarServicios();
        inicializarContenedorDinamico();
        configurarInterfaz();
        actualizarDatos();
    }

    private void inicializarServicios() {
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
    }

    private void configurarInterfaz() {
        tablaEventos.setItems(listaEventos);
        configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
        configurarColumnas();
    }

    private void actualizarDatos() {
        actualizarTabla();
        actualizarComboOrganizadores();
    }

    private void inicializarContenedorDinamico() {
        StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelVacio);
    }

    /**
     * Configura la relación entre el CheckBox de cupo máximo y el TextField
     * asociado. Se llama cuando el usuario interactúa con el CheckBox.
     */
    @FXML
    private void configuracionCupoMaximo() {
        configuracionCheckTextfield(chkCupoMaximo, txtCupoMaximo);
    }

    /**
     * Maneja el cambio de selección en el ComboBox de tipo de evento. Carga el
     * panel correspondiente y ajusta los controles según el tipo seleccionado.
     */
    @FXML
    private void onTipoEventoChanged() {
        TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
        // Si el tipo Seleccionado es null "Muestra el panel Vacio"
        cargarPanelEspecifico(tipoSeleccionado);        
    }

    /**
     * Método para dar de alta un evento Aquí se debe colocar la lógica para
     * guardar el evento en la base de datos.
     */
    @FXML
    private void altaEvento() {
        try {
            // Obtener datos básicos
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            String duracion = txtDuracion.getText().trim();
            boolean tieneCupo = chkCupoMaximo.isSelected();
            String cupoMax = txtCupoMaximo.getText().trim();
            int cupoMaximo = 0;
            boolean tieneInscripcion = chkTieneInscripcion.isSelected();

            // Obtener organizadores seleccionados
            List<Persona> organizadoresSeleccionados = new ArrayList<>(
                    chkComboOrganizadores.getCheckModel().getCheckedItems());
            TipoEvento unTipoEvento = cmbTipoEvento.getSelectionModel().getSelectedItem();

            // Validar campos base
            validarCamposBase(nombre, ubicacion, fechaInicio, duracion, unTipoEvento, organizadoresSeleccionados,
                    tieneCupo, cupoMax);

            int duracionEstimada = Integer.parseInt(duracion);

            if (tieneCupo) {
                cupoMaximo = Integer.parseInt(cupoMax);
            }

            if (eventoEnEdicion == null) { // ALTA
                realizarAlta(nombre, ubicacion, fechaInicio, duracionEstimada, tieneCupo, cupoMaximo, tieneInscripcion,
                        organizadoresSeleccionados, unTipoEvento);
            } else {
                realizarModificacion(nombre, ubicacion, fechaInicio, duracionEstimada, tieneCupo, cupoMaximo,
                        tieneInscripcion, organizadoresSeleccionados, unTipoEvento);
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error en los datos: " + e.getMessage());
        } catch (Exception e) {
            Alerta.mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Este bloque se ejecuta SIEMPRE (haya o no error)
            eventoEnEdicion = null;
            limpiarCampos();
            actualizarTabla();
            configuracionFinaly(btnAlta, btnModificacion, btnBaja, btnCancelar, TEXTO_BOTON_ALTA);
            btnVisualizacion.setDisable(false);
            cmbTipoEvento.setDisable(false);
        }
    }

    @FXML
    private void modificacionEvento() {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            Alerta.mostrarError(MENSAJE_ERROR_SELECCION);
            return;
        }

        // Configurar interfaz para modificación
        configuracionBtnModificar(btnAlta, btnModificacion, btnBaja, btnCancelar);
        btnVisualizacion.setDisable(true);

        // Cargar datos del evento seleccionado
        cargarDatosEvento(eventoSeleccionado);
        // Guardar referencia para edición
        eventoEnEdicion = eventoSeleccionado;
        // ProyeccionEnEdicion = proyeccionSeleccionada;
    }

    private void cargarDatosEvento(Evento evento) {
        // Datos básicos
        txtNombre.setText(evento.getNombre());
        txtUbicacion.setText(evento.getUbicacion());
        dpFechaInicio.setValue(evento.getFechaInicio());
        txtDuracion.setText(String.valueOf(evento.getDuracionEstimada()));
        chkCupoMaximo.setSelected(evento.isTieneCupo());
        chkTieneInscripcion.setSelected(evento.isTieneInscripcion());

        if (evento.isTieneCupo()) {
            txtCupoMaximo.setText(String.valueOf(evento.getCapacidadMaxima()));
        }

        // Cargar organizadores
        List<Persona> organizadores = evento.getOrganizadores();
        chkComboOrganizadores.getCheckModel().clearChecks();
        for (Persona organizador : organizadores) {
            chkComboOrganizadores.getCheckModel().check(organizador);
        }

        // Determinar la Visibilidad de elementos
        if (evento.isTieneCupo()) {
            txtCupoMaximo.setDisable(false);
        }

        // Determinar tipo y cargar panel específico
        if (evento instanceof Exposicion) {
            cmbTipoEvento.setValue(TipoEvento.EXPOSICION);
            cmbTipoEvento.setDisable(true);
            cambiarEscenaAExposicion();
            panelExposicionController.cargarDatos((Exposicion) evento);
        } else if (evento instanceof Taller) {
            cmbTipoEvento.setValue(TipoEvento.TALLER);
            cmbTipoEvento.setDisable(true);
            cambiarEscenaATaller();
            panelTallerController.cargarDatos((Taller) evento);
        } else if (evento instanceof Concierto) {
            cmbTipoEvento.setValue(TipoEvento.CONCIERTO);
            cmbTipoEvento.setDisable(true);
            cambiarEscenaAConcierto();
            panelConciertoController.cargarDatos((Concierto) evento);
        } else if (evento instanceof CicloDeCine) {
            cmbTipoEvento.setValue(TipoEvento.CICLO_DE_CINE);
            cmbTipoEvento.setDisable(true);
            cambiarEscenaACicloDeCine();
            panelCicloCineController.cargarDatos((CicloDeCine) evento);
        } else if (evento instanceof Feria) {
            cmbTipoEvento.setValue(TipoEvento.FERIA);
            cmbTipoEvento.setDisable(true);
            cambiarEscenaAFeria();
            panelFeriaController.cargarDatos((Feria) evento);
        }
    }

    @FXML
    private void bajaEvento() {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            Alerta.mostrarError(MENSAJE_ERROR_SELECCION);
            return;
        }

        boolean confirmar = Alerta.confirmarAccion(
                "¿Está seguro que desea eliminar el Evento '" + eventoSeleccionado.getNombre() + "'?");
        if (!confirmar) {
            return; // Sale inmediatamente si preciona cancelar
        }

        try {
            eventoServicio.baja(eventoSeleccionado.getId());
            Alerta.mostrarExito(MENSAJE_EXITO_ELIMINACION);
            actualizarTabla();
        } catch (Exception e) {
            Alerta.mostrarError(MENSAJE_ERROR_ELIMINACION + " " + e.getMessage());
        }
    }

    @FXML
    private void cancelarEdicion() {
        eventoEnEdicion = null;
        limpiarCampos();
        actualizarTabla();
        configuracionBtnCancelar(btnAlta, btnModificacion, btnBaja, btnCancelar, TEXTO_BOTON_ALTA);
        btnVisualizacion.setDisable(false);
        cmbTipoEvento.setDisable(false);
    }

    @FXML
    private void visualizacionEvento() {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            Alerta.mostrarError(MENSAJE_ERROR_SELECCION);
            return;
        }

        Vista vistaAAbrir = determinarVistaPorTipoEvento(eventoSeleccionado);
        if (vistaAAbrir != null) {
            // Preparar el modal y controlador SIN mostrar todavía
            Pair<VisualizacionController, Stage> resultado = StageManager.prepararModalConControlador(vistaAAbrir);

            if (resultado != null) {
                VisualizacionController controller = resultado.getKey();
                Stage modalStage = resultado.getValue();

                // PRIMERO configurar los datos
                controller.mostrarDatos(eventoSeleccionado);

                // DESPUÉS mostrar el modal con los datos ya configurados
                StageManager.mostrarModalPreparado(modalStage);
            }
        } else {
            Alerta.mostrarError("Tipo de evento no reconocido");
        }
    }

    private Vista determinarVistaPorTipoEvento(Evento evento) {
        if (evento instanceof Exposicion) {
            return Vista.VisualizacionExposicion;
        } else if (evento instanceof Taller) {
            return Vista.VisualizacionTaller;
        } else if (evento instanceof Concierto) {
            return Vista.VisualizacionConcierto;
        } else if (evento instanceof CicloDeCine) {
            return Vista.VisualizacionCicloCine;
        } else if (evento instanceof Feria) {
            return Vista.VisualizacionFeria;
        }
        return null;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtUbicacion.clear();
        dpFechaInicio.setValue(null);
        txtDuracion.clear();
        chkCupoMaximo.setSelected(false);
        txtCupoMaximo.clear();
        chkTieneInscripcion.setSelected(false);
        chkComboOrganizadores.getCheckModel().clearChecks();
        cmbTipoEvento.setValue(null);
        inicializarContenedorDinamico();
    }

    private void validarCamposBase(String txtNombre, String txtUbicacion, LocalDate fechaInicio,
            String txtDuracion, TipoEvento tipo, List<Persona> organizadores, boolean tieneCupo, String cupoMax) {
        StringBuilder mensajeError = new StringBuilder();

        if (txtNombre == null || txtNombre.trim().isEmpty()) {
            mensajeError.append("• El nombre es obligatorio y no puede estar vacío.\n");
        }

        if (txtUbicacion == null || txtUbicacion.trim().isEmpty()) {
            mensajeError.append("• La ubicación es obligatoria y no puede estar vacía.\n");
        }

        if (fechaInicio == null) {
            mensajeError.append("• La fecha de inicio es obligatoria.\n");
        } else if (fechaInicio.isBefore(LocalDate.now())) {
            mensajeError.append("• La fecha de inicio no puede ser anterior a hoy.\n");
        }

        if (txtDuracion == null || txtDuracion.trim().isEmpty()) {
            mensajeError.append("• La duración es obligatoria.\n");
        } else {
            try {
                int duracion = Integer.parseInt(txtDuracion);
                if (duracion <= 0) {
                    mensajeError.append("• La duración debe ser un número positivo.\n");
                }
            } catch (NumberFormatException e) {
                mensajeError.append("• La duración debe ser un número válido.\n");
            }
        }

        if (tipo == null) {
            mensajeError.append("• Debe seleccionar un tipo de evento.\n");
        }

        if (organizadores == null || organizadores.isEmpty()) {
            mensajeError.append("• Debe seleccionar al menos un organizador.\n");
        }

        if (tieneCupo && (cupoMax == null || cupoMax.trim().isEmpty())) {
            mensajeError.append("• Si se selecciona que tiene cupo se debe ingresar el cupo máximo.\n");
        }

        if (mensajeError.length() > 0) {
            throw new IllegalArgumentException(mensajeError.toString());
        }
    }

    private void cargarPanelEspecifico(TipoEvento tipo) {
        try {
            // Si tipo es null, mostrar panel vacío
            if (tipo == null) {
                StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelVacio);
                chkCupoMaximo.setDisable(false); 
                return; // Salir del método
            }

            switch (tipo) {
                case EXPOSICION -> {
                    panelExposicionController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(
                            contenedorDinamico, Vista.PanelExposicion);
                    chkCupoMaximo.setDisable(false);
                }
                case TALLER -> {
                    panelTallerController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(
                            contenedorDinamico, Vista.PanelTaller);
                    chkCupoMaximo.setSelected(true);
                    chkCupoMaximo.setDisable(true);
                    txtCupoMaximo.setDisable(false);
                }
                case CONCIERTO -> {
                    panelConciertoController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(
                            contenedorDinamico, Vista.PanelConcierto);
                    chkCupoMaximo.setDisable(false);
                }
                case CICLO_DE_CINE -> {
                    panelCicloCineController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(
                            contenedorDinamico, Vista.PanelCicloCine);
                    chkCupoMaximo.setDisable(false);
                }
                case FERIA -> {
                    panelFeriaController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(
                            contenedorDinamico, Vista.PanelFeria);
                    chkCupoMaximo.setDisable(false);
                }
            }
        } catch (Exception e) {
            Alerta.mostrarError("Error cargando el formulario específico");
        }
    }

    private void realizarAlta(String nombre, String ubicacion, LocalDate fechaInicio, int duracionEstimada,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadoresSeleccionados,
            TipoEvento unTipoEvento) {
        if (!Alerta.confirmarAccion("¿Guardar el Evento '" + nombre + "'?")) {
            return;
        }

        try {
            // Llamar al servicio según el tipo de evento
            switch (unTipoEvento) {
                case EXPOSICION -> {
                    TipoDeArte tipoDeArte = panelExposicionController.getTipoArteSeleccionado();
                    Persona curador = panelExposicionController.getCurador();
                    eventoServicio.altaExposicion(nombre, ubicacion, fechaInicio, duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            tipoDeArte, curador);
                }
                case TALLER -> {
                    boolean esPresencial = panelTallerController.getEsPrecencial();
                    Persona instructor = panelTallerController.getInstructor();

                    eventoServicio.altaTaller(nombre, ubicacion, fechaInicio, duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            esPresencial, instructor);
                }
                case CONCIERTO -> {
                    List<Persona> artistas = panelConciertoController.getArtistas();
                    boolean esPago = panelConciertoController.getEsPago();
                    double monto = esPago ? panelConciertoController.getMonto() : 0.0;

                    eventoServicio.altaConcierto(nombre, ubicacion, fechaInicio, duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            esPago, monto, artistas);
                }
                case CICLO_DE_CINE -> {
                    Proyeccion proyeccion = panelCicloCineController.getProyeccion();
                    boolean charlasPosteriores = panelCicloCineController.getCharlasPosteriores();

                    eventoServicio.altaCicloCine(nombre, ubicacion, fechaInicio, duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            proyeccion, charlasPosteriores);
                }
                case FERIA -> {
                    int cantidadStands = panelFeriaController.getCantidadStands();
                    TipoCobertura tipoCobertura = panelFeriaController.getTipoCobertura();

                    eventoServicio.altaFeria(nombre, ubicacion, fechaInicio, duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            cantidadStands, tipoCobertura);
                }
            }

            Alerta.mostrarExito(MENSAJE_EXITO_ALTA);
            limpiarCampos();
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo Realizar El Alta del Evento " + e.getMessage());
        }
    }

    private void realizarModificacion(String nombre, String ubicacion, LocalDate fechaInicio, int duracionEstimada,
            boolean tieneCupo, int cupoMaximo, boolean tieneInscripcion, List<Persona> organizadoresSeleccionados,
            TipoEvento unTipoEvento) {
        if (!Alerta.confirmarAccion("¿Modificar El evento '" + eventoEnEdicion.getNombre() + "'?")) {
            return;
        }
        try {
            switch (unTipoEvento) {
                case EXPOSICION -> {
                    TipoDeArte tipoDeArte = panelExposicionController.getTipoArteSeleccionado();
                    Persona curador = panelExposicionController.getCurador();

                    eventoServicio.modificarExposicion(eventoEnEdicion.getId(), nombre, ubicacion, fechaInicio,
                            duracionEstimada, tieneCupo,
                            cupoMaximo, tieneInscripcion, organizadoresSeleccionados, tipoDeArte, curador);

                }
                case TALLER -> {
                    boolean esPresencial = panelTallerController.getEsPrecencial();
                    Persona instructor = panelTallerController.getInstructor();

                    eventoServicio.modificarTaller(eventoEnEdicion.getId(), nombre, ubicacion, fechaInicio,
                            duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            esPresencial, instructor);
                }
                case CONCIERTO -> {
                    List<Persona> artistas = panelConciertoController.getArtistas();
                    boolean esPago = panelConciertoController.getEsPago();
                    double monto = esPago ? panelConciertoController.getMonto() : 0.0;

                    eventoServicio.modificarConcierto(eventoEnEdicion.getId(), nombre, ubicacion, fechaInicio,
                            duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            esPago, monto, artistas);
                }
                case CICLO_DE_CINE -> {
                    Proyeccion proyeccion = panelCicloCineController.getProyeccion();
                    boolean charlasPosteriores = panelCicloCineController.getCharlasPosteriores();

                    eventoServicio.modificarCicloCine(eventoEnEdicion.getId(), nombre, ubicacion, fechaInicio,
                            duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            proyeccion, charlasPosteriores);
                }
                case FERIA -> {
                    int cantidadStands = panelFeriaController.getCantidadStands();
                    TipoCobertura tipoCobertura = panelFeriaController.getTipoCobertura();

                    eventoServicio.modificarFeria(eventoEnEdicion.getId(), nombre, ubicacion, fechaInicio,
                            duracionEstimada,
                            tieneCupo, cupoMaximo, tieneInscripcion, organizadoresSeleccionados,
                            cantidadStands, tipoCobertura);
                }
            }
            Alerta.mostrarExito(MENSAJE_EXITO_MODIFICACION);
            limpiarCampos();
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo modificar el Evento " + e.getMessage());
        }
    }

    private void actualizarTabla() {
        try {
            List<Evento> eventos = eventoServicio.buscarTodos();
            if (eventos != null) {
                listaEventos.setAll(FXCollections.observableArrayList(eventos));
                tablaEventos.refresh();
            } else {
                listaEventos.clear();
                Alerta.mostrarError("No se pudieron cargar los eventos");
            }
        } catch (Exception e) {
            listaEventos.clear();
            Alerta.mostrarError("Error al cargar eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarComboOrganizadores() {
        // Utilizamos el servicio de Persona para obtener todos los organizadores
        // Registrados en el sistema.
        configuracionListaEnCheckCombo(chkComboOrganizadores, personaServicio.obtenerPersonasPorRol(TipoRol.ORGANIZADOR));
    }

    private void cambiarEscenaAExposicion() {
        chkCupoMaximo.setDisable(false);
        panelExposicionController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico,
                Vista.PanelExposicion);
    }

    private void cambiarEscenaATaller() {
        chkCupoMaximo.setSelected(true);
        chkCupoMaximo.setDisable(true);
        txtCupoMaximo.setDisable(false);
        panelTallerController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico,
                Vista.PanelTaller);
    }

    private void cambiarEscenaAConcierto() {
        chkCupoMaximo.setDisable(false);
        panelConciertoController = StageManager
                .cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, Vista.PanelConcierto);
    }

    private void cambiarEscenaACicloDeCine() {
        chkCupoMaximo.setDisable(false);
        panelCicloCineController = StageManager
                .cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, Vista.PanelCicloCine);
    }

    private void cambiarEscenaAFeria() {
        chkCupoMaximo.setDisable(false);
        panelFeriaController = StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico,
                Vista.PanelFeria);
    }

    private void configurarColumnas() {
        // Columnas básicas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionEstimada"));

        // Columna de fecha formateada
        colFechaInicio
                .setCellValueFactory(cellData -> ConfiguracionIgu.formatFecha(cellData.getValue().getFechaInicio()));

        // Columnas booleanas
        colTieneCupo.setCellValueFactory(cellData -> formatBoolean(cellData.getValue().isTieneCupo(), "SÍ", "NO"));

        colTieneInscripcion
                .setCellValueFactory(cellData -> formatBoolean(cellData.getValue().isTieneInscripcion(), "SÍ", "NO"));

        // Columna de capacidad condicional
        colCapacidadMaxima.setCellValueFactory(cellData -> ConfiguracionIgu.formatCupoMaximoEvent(cellData.getValue()));

        // Columna de organizadores
        colOrganizadores.setCellValueFactory(cellData -> {
            List<Persona> organizadores = cellData.getValue().getOrganizadores();
            return formatLista(organizadores, Persona::getInformacionPersonal, TEXTO_SIN_ORGANIZADORES);
        });

        // Columna de tipo de evento
        colTipo.setCellValueFactory(cellData -> obtenerTipoEvento(cellData.getValue()));
    }
    

    private SimpleStringProperty obtenerTipoEvento(Evento evento) {
        if (evento instanceof Exposicion) {
            return new SimpleStringProperty(TipoEvento.EXPOSICION.getDescripcion());
        } else if (evento instanceof Taller) {
            return new SimpleStringProperty(TipoEvento.TALLER.getDescripcion());
        } else if (evento instanceof Concierto) {
            return new SimpleStringProperty(TipoEvento.CONCIERTO.getDescripcion());
        } else if (evento instanceof CicloDeCine) {
            return new SimpleStringProperty(TipoEvento.CICLO_DE_CINE.getDescripcion());
        } else if (evento instanceof Feria) {
            return new SimpleStringProperty(TipoEvento.FERIA.getDescripcion());
        } else {
            return new SimpleStringProperty("-");
        }
    }
}