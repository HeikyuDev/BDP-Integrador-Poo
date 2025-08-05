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

public class FormularioEventoController implements Initializable {

    // === CONSTANTES ===    
    private static final String MENSAJE_EXITO_ALTA = "Evento registrado exitosamente";
    private static final String MENSAJE_EXITO_MODIFICACION = "Evento modificado exitosamente";
    private static final String MENSAJE_EXITO_ELIMINACION = "Evento eliminado correctamente";
    private static final String MENSAJE_ERROR_ELIMINACION = "Error: No se pudo eliminar el Evento";
    private static final String MENSAJE_ERROR_SELECCION = "Debe seleccionar un evento";
    private static final String TEXTO_BOTON_ALTA = "Dar de Alta";
    private static final String TEXTO_SIN_ORGANIZADORES = "Sin Organizadores";

    // === ELEMENTOS FXML ===    
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

    // === ATRIBUTOS DE INSTANCIA ===
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializo los servicios que se utilizaran {Evento - Persona}
        inicializarServicios();
        // Inicializo el panel que se va a mostrar por defecto
        inicializarContenedorDinamico();
        // Configuro la tabla y los combos
        configurarInterfaz();
        // Actualizo la tabla de eventos registrados "Activos" y la Combo de Organizadores
        actualizarDatos();
    }

    // Metodo Utilizado para inicializar los servicios que se van a utilizar
    private void inicializarServicios() {
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
    }

    // Metodo utilizado para configurar componentes de Interfaz
    private void configurarInterfaz() {
        tablaEventos.setItems(listaEventos);
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
        configurarColumnas();
    }

    // Metodo Utilizado para actualizar tanto los datos de la tabla de eventos como los de la combo de Organizadores
    private void actualizarDatos() {
        actualizarTabla();
        actualizarComboOrganizadores();
    }

    // Metodo utilizado para mostrar un panel por defecto 
    private void inicializarContenedorDinamico() {
        StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelVacio);
    }

    @FXML
    private void configuracionCupoMaximo() {
        // Configuracion que permite que cuando se seleccione un CheckBox se pueda escribir en un textfield
        ConfiguracionIgu.configuracionCheckTextfield(chkCupoMaximo, txtCupoMaximo);
    }

    @FXML
    private void onTipoEventoChanged() {
        // Configuracion utilizada para que se cambie de panel en base a la seleccion del combo
        TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
        // Si el tipo Seleccionado es null "Muestra el panel Vacio"
        cargarPanelEspecifico(tipoSeleccionado);
    }

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

            // Realizamos las conversiones necesarias
            int duracionEstimada = Integer.parseInt(duracion);

            if (tieneCupo) {
                cupoMaximo = Integer.parseInt(cupoMax);
            }

            if (eventoEnEdicion == null) { // ALTA
                realizarAlta(nombre, ubicacion, fechaInicio, duracionEstimada, tieneCupo, cupoMaximo, tieneInscripcion,
                        organizadoresSeleccionados, unTipoEvento);
            } else {
                // Realizo la modificacion de la instancia existente, estableciendo los nuevos valore
                realizarModificacion(nombre, ubicacion, fechaInicio, duracionEstimada, tieneCupo, cupoMaximo,
                        tieneInscripcion, organizadoresSeleccionados, unTipoEvento);
                
                
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error en los datos: " + e.getMessage());
            return;
        } catch (Exception e) {
            Alerta.mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } 
    }

    @FXML
    private void modificacionEvento() {
        try {
            // Obtengo evento seleccionado del ComboBox
            Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
            if (eventoSeleccionado == null) {
                // Si no se selecciona un evento del combo muestro un Error
                Alerta.mostrarError(MENSAJE_ERROR_SELECCION);
                return;
            }

            // Configurar interfaz para modificación
            ConfiguracionIgu.configuracionBtnModificar(btnAlta, btnModificacion, btnBaja, btnCancelar);
            btnVisualizacion.setDisable(true);

            // Cargar datos del evento seleccionado {Mostrar en pantalla lo que ingreso el usuario}
            cargarDatosEvento(eventoSeleccionado);
            // Guardar referencia para edición
            eventoEnEdicion = eventoSeleccionado;
            // ProyeccionEnEdicion = proyeccionSeleccionada;
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        }
    }

    private void cargarDatosEvento(Evento evento) {
        try {

            // Validamos si el evento es nulo
            if (evento == null) {
                // Validamos si el evento es nulo
                Alerta.mostrarError("Error: No se puede cargar un evento nulo");
                return;
            }
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
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: Asegurese de ingresar datos Validos");
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error Inesperado");
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
        ConfiguracionIgu.configuracionBtnCancelar(btnAlta, btnModificacion, btnBaja, btnCancelar, TEXTO_BOTON_ALTA);
        btnVisualizacion.setDisable(false);
        cmbTipoEvento.setDisable(false);
    }

    @FXML
    private void visualizacionEvento() {
        // Obtengo el evento seleccionado de la tabla
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

    // Metodo utilizado para determinar a que subclase corresponde el parametro
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

    // Metodo utilizado para limpiar los campos de la IGU
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

    // Metodo utilizado para validar los datos que el usuario ingresa
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

    // Metodo utilziado para mostrar un determinado panel dependiendo del tipo de evento
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
            Alerta.mostrarError("Error cargando el formulario específico: " + e.getMessage());
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
            // Limpio los campos
            limpiarCampos();
            // Actualizo la tabla
            actualizarTabla();
            
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
            // Limpio los campos
            limpiarCampos();
            // Establesco que la variable de edicion como null, indicando que vuelve a ser ALTA
            eventoEnEdicion = null;
            // Actualizamos la tabla para que impacten los cambios (La modificacion del registro)
            actualizarTabla();            
            // Configuramos el estado base
            ConfiguracionIgu.configuracionBase(btnAlta, btnModificacion, btnBaja, btnCancelar, TEXTO_BOTON_ALTA);
            // Ajustes adicionales de igu
            btnVisualizacion.setDisable(false);
            cmbTipoEvento.setDisable(false);            
            txtCupoMaximo.setDisable(true);
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo modificar el Evento " + e.getMessage());
        }
    }

    private void actualizarTabla() {
        try {
            // Obtengo todos los eventos de la BD
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
        ConfiguracionIgu.configuracionListaEnCheckCombo(chkComboOrganizadores, personaServicio.obtenerPersonasPorRol(TipoRol.ORGANIZADOR));
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
        colTieneCupo.setCellValueFactory(cellData -> ConfiguracionIgu.formatBoolean(cellData.getValue().isTieneCupo(), "SÍ", "NO"));

        colTieneInscripcion
                .setCellValueFactory(cellData -> ConfiguracionIgu.formatBoolean(cellData.getValue().isTieneInscripcion(), "SÍ", "NO"));

        // Columna de capacidad condicional
        colCapacidadMaxima.setCellValueFactory(cellData -> ConfiguracionIgu.formatCupoMaximoEvent(cellData.getValue()));

        // Columna de organizadores
        colOrganizadores.setCellValueFactory(cellData -> {
            List<Persona> organizadores = cellData.getValue().getOrganizadores();
            return ConfiguracionIgu.formatLista(organizadores, Persona::getInformacionPersonal, TEXTO_SIN_ORGANIZADORES);
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
