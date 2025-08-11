package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import com.mycompany.bdppeventos.dto.DatosComunesEvento;
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
            colTieneCupo, colCapacidadMaxima, colOrganizadores, colTipo;

    @FXML
    private TableColumn<Evento, LocalDate> colFechaInicio;
    
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
    
    // === METODOS DE CONFIGURACION ===

    // Metodo Utilizado para inicializar los servicios que se van a utilizar
    private void inicializarServicios() {
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
    }
    
    // Metodo utilizado para mostrar un panel por defecto 
    private void inicializarContenedorDinamico() {
        StageManager.cambiarEscenaEnContenedor(contenedorDinamico, Vista.PanelVacio);
    }

    // Metodo utilizado para configurar componentes de Interfaz
    private void configurarInterfaz() {
        tablaEventos.setItems(listaEventos);
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
        configurarColumnas();
    }
    
    // Metodo utilizado para Establecer el formato de como de van a mostrar los datos en las columnas
    private void configurarColumnas() {
        // Columnas básicas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionEstimada"));        
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        // Columnas booleanas
        colTieneCupo.setCellValueFactory(cellData -> ConfiguracionIgu.formatBoolean(cellData.getValue().isTieneCupo()));
        colTieneInscripcion.setCellValueFactory(cellData -> ConfiguracionIgu.formatBoolean(cellData.getValue().isTieneInscripcion()));
        // Columna de capacidad 
        colCapacidadMaxima.setCellValueFactory(cellData -> ConfiguracionIgu.formatCupoMaximoEvent(cellData.getValue()));
        // Columna de organizadores
        colOrganizadores.setCellValueFactory(cellData -> {
            List<Persona> organizadores = cellData.getValue().getOrganizadores();
            return ConfiguracionIgu.formatLista(organizadores, Persona::getInformacionPersonal, TEXTO_SIN_ORGANIZADORES);
        });
        // Columna de tipo de evento
        colTipo.setCellValueFactory(cellData -> ConfiguracionIgu.obtenerTipoEvento(cellData.getValue()));        
        // Configuracion de las Celdas
        ConfiguracionIgu.configurarColumnaFecha(colFechaInicio);
        
    }
    
    
  

    // Metodo Utilizado para actualizar tanto los datos de la tabla de eventos como los de la combo de Organizadores
    private void actualizarDatos() {
        actualizarTabla();
        actualizarComboOrganizadores();
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
        ConfiguracionIgu.configuracionListaEnCheckCombo(chkComboOrganizadores, personaServicio.obtenerPersonasPorRol(TipoRol.ORGANIZADOR));
    }    
    
    // === METODOS DE CONTROLES ===
    
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
                case EXPOSICION -> panelExposicionController = cambiarEscena(Vista.PanelExposicion, false);                                    
                case TALLER -> panelTallerController = cambiarEscena(Vista.PanelTaller, true);                
                case CONCIERTO -> panelConciertoController = cambiarEscena(Vista.PanelConcierto, false);                
                case CICLO_DE_CINE -> panelCicloCineController = cambiarEscena(Vista.PanelCicloCine, false);                
                case FERIA -> panelFeriaController = cambiarEscena(Vista.PanelFeria, false);         
            }
        } catch (Exception e) {
            Alerta.mostrarError("Error cargando el formulario específico: " + e.getMessage());
        }
    }
    
    // Metodo Genérico que me retorna el controlador
    private <T> T cambiarEscena(Vista vista, boolean esTaller) {
        if (esTaller) {
            // Solo para TALLER: forzar selección y deshabilitar
            chkCupoMaximo.setSelected(true);
            chkCupoMaximo.setDisable(true);
            txtCupoMaximo.setDisable(false);
        } else {
            // Para otros: solo habilitar (mantener estado actual)
            chkCupoMaximo.setDisable(false);
            // txtCupoMaximo.setDisable(!chkCupoMaximo.isSelected());
        }

        return StageManager.cambiarEscenaEnContenedorYObtenerControlador(contenedorDinamico, vista);
    }

    @FXML
    private void altaEvento() {
        try {
            // Obtener datos básicos desde la UI
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            String duracionStr = txtDuracion.getText().trim();
            boolean tieneCupo = chkCupoMaximo.isSelected();
            String cupoMaxStr = txtCupoMaximo.getText().trim();
            boolean tieneInscripcion = chkTieneInscripcion.isSelected();

            List<Persona> organizadoresSeleccionados = new ArrayList<>(
                    chkComboOrganizadores.getCheckModel().getCheckedItems()
            );
            TipoEvento unTipoEvento = cmbTipoEvento.getSelectionModel().getSelectedItem();

            // Validar campos (esto lanza excepción si hay errores)
            validarCamposBase(nombre, ubicacion, fechaInicio, duracionStr, unTipoEvento,
                    organizadoresSeleccionados, tieneCupo, cupoMaxStr);

            // Conversión segura (ya fue validado)
            int duracionEstimada = Integer.parseInt(duracionStr);
            int cupoMaximo = tieneCupo ? Integer.parseInt(cupoMaxStr) : 0;

            // Creamos un DTO que tendra los datos comunes del Evento base            
            DatosComunesEvento datosComunes = new DatosComunesEvento(nombre,ubicacion,fechaInicio,duracionEstimada,tieneCupo,
                    cupoMaximo,tieneInscripcion,organizadoresSeleccionados);
            
            // Decidir si es alta o modificación
            if (eventoEnEdicion == null) {
                realizarAlta(datosComunes, unTipoEvento);
            } else {
                realizarModificacion(datosComunes, unTipoEvento);
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error en los datos:\n" + e.getMessage());
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
                configurarCmbTipoEvento(TipoEvento.EXPOSICION);
                panelExposicionController = cambiarEscena(Vista.PanelExposicion, false);
                panelExposicionController.cargarDatos((Exposicion) evento);
            } else if (evento instanceof Taller) {
                configurarCmbTipoEvento(TipoEvento.TALLER);
                panelTallerController = cambiarEscena(Vista.PanelTaller, true);
                panelTallerController.cargarDatos((Taller) evento);
            } else if (evento instanceof Concierto) {
                configurarCmbTipoEvento(TipoEvento.CONCIERTO);
                panelConciertoController = cambiarEscena(Vista.PanelConcierto, false);
                panelConciertoController.cargarDatos((Concierto) evento);
            } else if (evento instanceof CicloDeCine) {
                configurarCmbTipoEvento(TipoEvento.CICLO_DE_CINE);
                panelCicloCineController = cambiarEscena(Vista.PanelCicloCine, false);
                panelCicloCineController.cargarDatos((CicloDeCine) evento);
            } else if (evento instanceof Feria) {
                configurarCmbTipoEvento(TipoEvento.FERIA);
                panelFeriaController = cambiarEscena(Vista.PanelFeria, false);
                panelFeriaController.cargarDatos((Feria) evento);
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: Asegurese de ingresar datos Validos");
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error Inesperado");
        }
    }
    
    private void configurarCmbTipoEvento(TipoEvento unTipoEvento) {
        cmbTipoEvento.setValue(unTipoEvento);
        cmbTipoEvento.setDisable(true);
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
        // Configuracion que deja la pantalla en el estado inicial {ALTA}
        configuracionBase();
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
    
    // === MODULOS DE ALTA Y MODIFICACION 

    private void realizarAlta(DatosComunesEvento datosComunes,TipoEvento unTipoEvento) {        
        try {
            //Validar datos especificos del tipo de evento
            validarDatosPorTipo(unTipoEvento);
            // Confirmacion alta
            if (!Alerta.confirmarAccion("¿Guardar el Evento '" + datosComunes.nombre() + "'?")) {
            return;
            }
            // Ejecutar alta segun el tipo
            altaEventoPorTipo(unTipoEvento, datosComunes);            
            // Mostrar Mensaje de Exito
            Alerta.mostrarExito(MENSAJE_EXITO_ALTA);   
            //Pongo el formulario en su forma por defecto
            configuracionBase();
            // Actualizo la tabla
            actualizarTabla();
            
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo Realizar El Alta del Evento " + e.getMessage());
        }
    }
    
    private void altaEventoPorTipo(TipoEvento tipo, DatosComunesEvento datosComunes) {
        switch (tipo) {
            case EXPOSICION -> {
                // Obtenemos los datos espécificos de la exposicion
                TipoDeArte tipoDeArte = panelExposicionController.getTipoArteSeleccionado();
                Persona curador = panelExposicionController.getCurador();
                // Llamamos al metodo del Servicio que persiste el alta de la exposicion
                eventoServicio.altaExposicion(datosComunes, tipoDeArte, curador);
            }
            case TALLER -> {
                // Obtenemos los datos espécificos del taller
                boolean esPresencial = panelTallerController.getEsPrecencial();
                Persona instructor = panelTallerController.getInstructor();
                // Llamamos al metodo del Servicio que persiste el alta del taller
                eventoServicio.altaTaller(datosComunes, esPresencial, instructor);
            }
            case CONCIERTO -> {
                // Obtenemos los datos espécificos del Concierto
                List<Persona> artistas = panelConciertoController.getArtistas();
                boolean esPago = panelConciertoController.getEsPago();
                double monto = esPago ? panelConciertoController.getMonto() : 0.0;
                // Llamamos al metodo del Servicio que persiste el alta del concierto
                eventoServicio.altaConcierto(datosComunes, esPago, monto, artistas);
            }
            case CICLO_DE_CINE -> {
                // Obtenemos los datos espécificos del Ciclo de Cine
                Proyeccion proyeccion = panelCicloCineController.getProyeccion();
                boolean charlasPosteriores = panelCicloCineController.getCharlasPosteriores();
                // Llamamos al metodo del Servicio que persiste el alta del Ciclo de Cine
                eventoServicio.altaCicloCine(datosComunes, proyeccion, charlasPosteriores);
            }
            case FERIA -> {
                // Obtenemos los datos espécificos de la feria
                int cantidadStands = panelFeriaController.getCantidadStands();
                TipoCobertura tipoCobertura = panelFeriaController.getTipoCobertura();
                // Llamamos al metodo del Servicio que persiste el alta de la Feria
                eventoServicio.altaFeria(datosComunes, cantidadStands, tipoCobertura);
            }
        }
    }        

    private void realizarModificacion(DatosComunesEvento datosComunes,TipoEvento unTipoEvento) {                        
        try {
            // Validar datos específicos del tipo de evento
            validarDatosPorTipo(unTipoEvento);            
            // Confirmar modificación
            if (!Alerta.confirmarAccion("¿Modificar el evento '" + eventoEnEdicion.getNombre() + "'?")) {
                return;
            }            
            // Ejecutar modificación según tipo
            modificarEventoPorTipo(unTipoEvento, datosComunes);
            // Mostrar Mensaje de Exito
            Alerta.mostrarExito(MENSAJE_EXITO_MODIFICACION);  
            // Dejamos la escena en su estado inicial
            configuracionBase();
        } catch (Exception e) {
            Alerta.mostrarError("No se pudo modificar el Evento " + e.getMessage());
        }
    }
    
    private void modificarEventoPorTipo(TipoEvento tipo, DatosComunesEvento datosComunes) {
        switch (tipo) {
            case EXPOSICION -> {
                // Obtenemos los datos espécificos de la exposicion
                TipoDeArte tipoDeArte = panelExposicionController.getTipoArteSeleccionado();
                Persona curador = panelExposicionController.getCurador();
                // Llamamos al metodo del Servicio que persiste la modificacion de la exposicion
                eventoServicio.modificarExposicion(eventoEnEdicion.getId(), datosComunes, tipoDeArte, curador);
            }
            case TALLER -> {
                // Obtenemos los datos espécificos del taller
                boolean esPresencial = panelTallerController.getEsPrecencial();
                Persona instructor = panelTallerController.getInstructor();
                // Llamamos al metodo del Servicio que persiste la modificacion del taller
                eventoServicio.modificarTaller(eventoEnEdicion.getId(), datosComunes, esPresencial, instructor);
            }
            case CONCIERTO -> {
                // Obtenemos los datos espécificos del Concierto
                List<Persona> artistas = panelConciertoController.getArtistas();
                boolean esPago = panelConciertoController.getEsPago();
                double monto = esPago ? panelConciertoController.getMonto() : 0.0;
                // Llamamos al metodo del Servicio que persiste la modificacion del concierto
                eventoServicio.modificarConcierto(eventoEnEdicion.getId(), datosComunes , esPago, monto, artistas);
            }
            case CICLO_DE_CINE -> {
                // Obtenemos los datos espécificos del Ciclo de Cine
                Proyeccion proyeccion = panelCicloCineController.getProyeccion();
                boolean charlasPosteriores = panelCicloCineController.getCharlasPosteriores();
                // Llamamos al metodo del Servicio que persiste la modificacion del Ciclo de Cine
                eventoServicio.modificarCicloCine(eventoEnEdicion.getId(), datosComunes , proyeccion, charlasPosteriores);
            }
            case FERIA -> {
                // Obtenemos los datos espécificos de la feria
                int cantidadStands = panelFeriaController.getCantidadStands();
                TipoCobertura tipoCobertura = panelFeriaController.getTipoCobertura();
                // Llamamos al metodo del Servicio que persiste la modificacion de la Feria
                eventoServicio.modificarFeria(eventoEnEdicion.getId(), datosComunes, cantidadStands, tipoCobertura);
            }
        }
    }    
    
    // === METODOS AUXILIARES === 
                       
    // Metodo utilizado para validar los datos que el usuario ingresa
    private void validarCamposBase(String txtNombre, String txtUbicacion, LocalDate fechaInicio,
            String txtDuracion, TipoEvento tipo, List<Persona> organizadores, boolean tieneCupo, String cupoMax) {

        StringBuilder mensajeError = new StringBuilder();

        if (txtNombre == null || txtNombre.isBlank()) {
            mensajeError.append("• El nombre es obligatorio.\n");
        }

        if (txtUbicacion == null || txtUbicacion.isBlank()) {
            mensajeError.append("• La ubicación es obligatoria.\n");
        }

        if (fechaInicio == null) {
            mensajeError.append("• La fecha de inicio es obligatoria.\n");
        } else if (fechaInicio.isBefore(LocalDate.now())) {
            mensajeError.append("• La fecha de inicio no puede ser anterior a hoy.\n");
        }

        if (txtDuracion == null || txtDuracion.isBlank()) {
            mensajeError.append("• La duración es obligatoria.\n");
        } else {
            try {
                int duracion = Integer.parseInt(txtDuracion);
                if (duracion <= 0) {
                    mensajeError.append("• La duración debe ser mayor a cero.\n");
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

        if (tieneCupo) {
            if (cupoMax == null || cupoMax.isBlank()) {
                mensajeError.append("• Si se indica que tiene cupo, debe especificar el cupo máximo.\n");
            } else {
                try {
                    int cupo = Integer.parseInt(cupoMax);
                    if (cupo <= 0) {
                        mensajeError.append("• El cupo máximo debe ser mayor a cero.\n");
                    }
                } catch (NumberFormatException e) {
                    mensajeError.append("• El cupo máximo debe ser un número válido.\n");
                }
            }
        }

        if (mensajeError.length() > 0) {
            throw new IllegalArgumentException(mensajeError.toString());
        }
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
    
    private void validarDatosPorTipo(TipoEvento tipo) {
        switch (tipo) {
            case EXPOSICION ->
                panelExposicionController.validar();
            case TALLER ->
                panelTallerController.validar();
            case CONCIERTO ->
                panelConciertoController.validar();
            case CICLO_DE_CINE ->
                panelCicloCineController.validar();
            case FERIA ->
                panelFeriaController.validar();
        }
    }
    
    private void configuracionBase() {
        eventoEnEdicion = null;
        limpiarCampos();
        actualizarTabla();
        ConfiguracionIgu.configuracionBase(btnAlta, btnModificacion, btnBaja, btnCancelar, TEXTO_BOTON_ALTA);
        // Ajustes adicionales de igu
        btnVisualizacion.setDisable(false);
        cmbTipoEvento.setDisable(false);
        txtCupoMaximo.setDisable(true);
    }

}
