/**
 * Controlador principal para el formulario de alta, baja y modificación de eventos.
 * Gestiona la lógica de la interfaz gráfica y la carga dinámica de paneles según el tipo de evento seleccionado.
 */

package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
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
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

/**
 * Clase controladora para la pantalla de ABM de eventos.
 * Extiende ConfiguracionIguEvento para reutilizar lógica de controles.
 * Implementa Initializable para inicializar combos y controles al cargar la
 * vista.
 */
public class FormularioEventoController extends ConfiguracionIgu implements Initializable {

    // Elementos del Controlador (vinculados con FXML)

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

    // TABLA Y COLUMNAS
    @FXML
    private TableView<Evento> tablaEventos;
    @FXML
    private TableColumn<Evento, String> colNombre;
    @FXML
    private TableColumn<Evento, String> colUbicacion;
    @FXML
    private TableColumn<Evento, LocalDate> colFechaInicio;
    @FXML
    private TableColumn<Evento, Integer> colDuracion;
    @FXML
    private TableColumn<Evento, String> colTieneInscripcion;
    @FXML
    private TableColumn<Evento, String> colTieneCupo;
    @FXML
    private TableColumn<Evento, Integer> colCapacidadMaxima;
    @FXML
    private TableColumn<Evento, String> colOrganizadores;
    @FXML
    private TableColumn<Evento, String> colTipo;

    // Lista observable (VINCULADA AL TABLE VIEW)
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();

    // Control que me permite seleccionar mas de un Organizador
    @FXML
    private CheckComboBox<Persona> chkComboOrganizadores;

    // Lista Observable vinculada al CheckCombo
    private ObservableList<Persona> listaOrganizadores = FXCollections.observableArrayList();

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

    // Servicio de Evento
    private EventoServicio eventoServicio;
    // Servicio de Persona
    private PersonaServicio personaServicio;

    // Constante que representa el Texto del Boton 
    private final String altaTxt = "Dar de Alta";
    
    // Variable de modificacion
    private Evento eventoEnEdicion = null;
    
    
    // 
    /**
     * Inicializa el controlador al cargar la vista FXML.
     * Carga los valores de los combos de estado y tipo de evento.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializo el eventoServicio utilizando el Repositorio
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        // Vinculo la Lista Observable con la tableView de la interfaz
        tablaEventos.setItems(listaEventos);
        // Realizo unas configuraciones en la interfaz
        configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
        configuracionColumnas();
        // Actualizo la tabla con todos los datos obtenidos de la Base de datos "Evento"
        actualizarTabla();
        actualizarComboOrganizadores();
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
                txtCupoMaximo.setDisable(false);
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
     * Método para dar de alta un evento Aquí se debe colocar la lógica para
     * guardar el evento en la base de datos.
     */
    @FXML
    private void altaEvento() {
        try 
        {
            // Obtengo los datos de la IGU Base (Seccion Datos del Evento)
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            String duracion = txtDuracion.getText().trim();
            // Obtengo los datos de la IGU Base (Seccion Configuraciones)
            boolean tieneCupo = chkCupoMaximo.isSelected();
            String cupoMax = txtCupoMaximo.getText().trim();
            boolean tieneInscripcion = chkTieneInscripcion.isSelected();
            
            
            
            // Convertimos la lista Observable devuelta del ChkCombo a ArrayList (Coincide con el tipo de dato del modelo)
            List<Persona> listaPersonas = new ArrayList<>(chkComboOrganizadores.getCheckModel().getCheckedItems());
            
            // Obtengo el Tipo de Evento (Para determinar que tipo de instancia creo)
            TipoEvento unTipoEvento = cmbTipoEvento.getSelectionModel().getSelectedItem();
                                    
            // Valido los datos ingresados por el usuario (Retorna una Excepcion si falla)
            validarCamposBase(nombre, ubicacion, fechaInicio, duracion, unTipoEvento, listaPersonas, tieneCupo, cupoMax);
            
            // Realizo las conversiones correspondientes
            int duracionEstimada = Integer.parseInt(duracion); // Si sale mal retorna una excepcion
            int cupoMaximo = Integer.parseInt(cupoMax);
            
            
            
            
            if(eventoEnEdicion == null) // Significa que es ALTA
            {
                // Preguntamos al usuario si esta seguro de dar de alta un evento
                if (!Alerta.confirmarAccion("¿Guardar el Evento'" + nombre + "'")) {
                    //Si el usuario preciona la opcion cancelar "Sale inmediatamente del metodo" 
                    return;
                }
                // Datos Especificos que tiene cada clase hija
                Object[] datosEspecificos = null;
                
                // Obtenemos los datos específicos de la clase hija seleccionada
                switch (unTipoEvento) {
                case EXPOSICION -> {
                    TipoDeArte tipoDeArte = unPanelExposicionController.getTipoArteSeleccionado(); // Ya validado en el Controlador del Panel
                    Persona unCurador = unPanelExposicionController.getCurador(); // Si el Tipo de Evento es Exposicion Si O SI debe haber UN CURADOR
                    datosEspecificos = new Object[] {tipoDeArte, unCurador};
                }
                case TALLER -> {
                    boolean esPresencial = unPanelTallerController.getEsPrecencial(); 
                    Persona unInstructor = unPanelTallerController.getInstructor();
                    datosEspecificos = new Object[] { esPresencial, unInstructor };
                }
                case CONCIERTO -> {
                    List<Persona> listaArtistas = unPanelConciertoController.getArtistas();
                    boolean esPago = unPanelConciertoController.getEsPago();
                    double monto = 0.0;
                    if (esPago) {
                        monto = unPanelConciertoController.getMonto(); 
                    }
                    datosEspecificos = new Object[] { esPago, monto, listaArtistas};
                }
                case CICLO_DE_CINE -> {
                    Proyeccion proyeccion = unPanelCicloCineController.getProyeccion(); //Ya validado                    
                    boolean charlasPosteriores = unPanelCicloCineController.getCharlasPosteriores();
                    datosEspecificos = new Object[] { proyeccion, charlasPosteriores };
                }
                case FERIA -> {
                    int cantidadStands = unPanelFeriaController.getCantidadStands(); 
                    TipoCobertura tipoCobertura = unPanelFeriaController.getTipoCobertura(); // Valida no null
                    datosEspecificos = new Object[] { cantidadStands, tipoCobertura };
                }
            }
                eventoServicio.altaEvento(nombre, ubicacion, fechaInicio, duracionEstimada, tieneCupo, cupoMaximo, tieneInscripcion, unTipoEvento, listaPersonas, datosEspecificos);
                Alerta.mostrarExito("Evento registrado exitosamente");
                
            }
            else
            {
                System.out.println("Coming soon..."); // Aca iría la seccion de modificaicon.
            }            
        } 
        catch (IllegalArgumentException e) 
        {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        } 
        catch(Exception e)
        {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        }
        finally 
        {
            actualizarTabla(); 
        }
    }

    @FXML
    private void modificacionEvento() {

    }

    @FXML
    private void bajaEvento() {

    }

    @FXML
    private void cancelarEdicion() {
    }
    
    @FXML
    private void visualizacionEvento()
    {
        
    }

    private void limpiarCampos()
    {
        txtNombre.clear();
        txtUbicacion.clear();
        dpFechaInicio.setValue(null);
        txtDuracion.clear();
        chkComboOrganizadores.getCheckModel().clearChecks();
    }
    
    private void validarCamposBase(String txtNombre, String txtUbicacion, LocalDate fechaInicio,
            String txtDuracion, TipoEvento tipo, List<Persona> organizadores, boolean tieneCupo, String cupoMax) {
        StringBuilder mensajeError = new StringBuilder();

        // Validar nombre
        if (txtNombre == null || txtNombre.trim().isEmpty()) {
            mensajeError.append("• El nombre es obligatorio y no puede estar vacío.\n");
        }

        // Validar ubicación
        if (txtUbicacion == null || txtUbicacion.trim().isEmpty()) {
            mensajeError.append("• La ubicación es obligatoria y no puede estar vacía.\n");
        }

        // Validar fecha
        if (fechaInicio == null) {
            mensajeError.append("• La fecha de inicio es obligatoria.\n");
        } else if (fechaInicio.isBefore(LocalDate.now())) {
            mensajeError.append("• La fecha de inicio no puede ser anterior a hoy.\n");
        }

        // Validar duración
        if (txtDuracion == null || txtDuracion.trim().isEmpty()) {
            mensajeError.append("• La duración es obligatoria.\n");
        }

        // Validar tipo
        if (tipo == null) {
            mensajeError.append("• Debe seleccionar un tipo de evento.\n");
        }

        // Validar organizadores
        if (organizadores == null || organizadores.isEmpty()) {
            mensajeError.append("• Debe seleccionar al menos un organizador.\n");
        }
        
        if(tieneCupo == true && cupoMax.isEmpty() || cupoMax == null)
        {
            mensajeError.append("• Si se selecciona Que tiene cupo se debe ingresar el cupo maximo.\n");
        }

        // Lanzar excepción si hay errores
        if (mensajeError.length() > 0) {
            throw new IllegalArgumentException(mensajeError.toString());
        }                
    }

    private void actualizarTabla() {
    try {
        // Obtener todos los eventos del servicio
        List<Evento> eventos = eventoServicio.buscarTodos();
        
        // Verificar que la lista no sea null y actualizar
        if (eventos != null) {
            // setAll() automáticamente limpia la lista y agrega los nuevos elementos
            // También notifica a la tabla de los cambios
            listaEventos.setAll(eventos);
            tablaEventos.refresh();
        } else {
            // Limpiar la lista si no hay eventos
            listaEventos.clear();
            Alerta.mostrarError("No se pudieron cargar los eventos");
        }
    } catch (Exception e) {
        // En caso de error, limpiar la lista para evitar datos inconsistentes
        listaEventos.clear();
        Alerta.mostrarError("Error al cargar eventos: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void actualizarComboOrganizadores() {                
        // Utilizamos el servicio de Persona para obtener todos los organizadores Registrados en el sistema.
        configuracionListaEnCheckCombo(chkComboOrganizadores, personaServicio.buscarOrganizadores());
    }

    private void configuracionColumnas() {
        // Columnas de la Clase Base Eventos
        colNombre.setCellValueFactory(new PropertyValueFactory<Evento, String>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<Evento, String>("ubicacion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Evento, LocalDate>("fechaInicio"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<Evento, Integer>("duracionEstimada"));
        colTieneCupo.setCellValueFactory(cellData -> formatBoolean(cellData.getValue().isTieneCupo(), "SI", "NO"));
        colCapacidadMaxima.setCellValueFactory(new PropertyValueFactory<Evento, Integer>("capacidadMaxima"));
        colTieneInscripcion.setCellValueFactory(cellData -> formatBoolean(cellData.getValue().isTieneInscripcion(), "SI", "NO"));        
        colOrganizadores.setCellValueFactory(cellData ->{
            Evento unEvento = cellData.getValue();
            List<Persona> listaPersonas = unEvento.getListaPersona();
            List<Persona> listaOrganizadores = new ArrayList<>();
            for(Persona unaPersona:listaPersonas)
            {
                if(unaPersona.getUnaListaRoles().contains(TipoRol.ORGANIZADOR))
                {
                    listaOrganizadores.add(unaPersona);
                }
            }
            return formatLista(listaOrganizadores, Persona::getInformacionPersonal ,"Sin Organizadores");
        });
        colTipo.setCellValueFactory(cellData ->{
            Evento unEvento = cellData.getValue();
            if (unEvento instanceof Exposicion) {
                return new SimpleStringProperty(TipoEvento.EXPOSICION.getDescripcion());
            }
            else if(unEvento instanceof Taller)
            {
                return new SimpleStringProperty(TipoEvento.TALLER.getDescripcion());
            }
            else if(unEvento instanceof Concierto)
            {
                return new SimpleStringProperty(TipoEvento.CONCIERTO.getDescripcion());
            }
            else if(unEvento instanceof CicloDeCine)
            {
                return new SimpleStringProperty(TipoEvento.CICLO_DE_CINE.getDescripcion());
            }
            else if(unEvento instanceof Feria)
            {
                return new SimpleStringProperty(TipoEvento.FERIA.getDescripcion());
            }
            else
            {
                return new SimpleStringProperty("-");
            }
        });
    }
    
    
}