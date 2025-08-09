package com.mycompany.bdppeventos.controller.VerParticipantes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.time.LocalDate;
import java.util.Arrays;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VerParticipantesController implements Initializable {

    // === CONTROLES    
    // ComboBox
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;    
    @FXML 
    private ComboBox<EstadoEvento> cmbEstado;
    // TableView
    @FXML
    private TableView<Evento> tblEvento;
    @FXML
    private TableColumn<Evento, String> colNombre, colUbicacion;    
    @FXML
    private TableColumn<Evento, Integer> colInscriptos;    
    @FXML
    private TableColumn<Evento, LocalDate> colFechaInicio;
    @FXML
    private TableColumn<Evento, EstadoEvento> colEstado;
    @FXML
    private TableView<Persona> tblParticipantes;    
    @FXML
    private TableColumn<Persona, String> colDNI, colNombreCompleto, colTelefono, colCorreo;

    // === LISTAS OBSERVABLES ===        
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    private ObservableList<Persona> listaParticipantes = FXCollections.observableArrayList();
        
    // === SERVICIOS ===
    private EventoServicio eventoServicio;
    
    // === LISTA DE EVENTOS === 
    private List<Evento> listaEventosCompleta;
    
    // === CONSTANTES === 
    private final List<EstadoEvento> estadosHabilitados = Arrays.asList(
        EstadoEvento.CONFIRMADO, EstadoEvento.EN_EJECUCION, EstadoEvento.FINALIZADO);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los Servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());

        // Configuramos la IGU
        configuracionIgu();        

        // Asociamos las listas Observables a Las tablas
        tblEvento.setItems(listaEventos);
        tblParticipantes.setItems(listaParticipantes);

        // Cargamos todos los eventos al inicializar
        cargarTodosLosEventos();
    }
    
    private void configuracionIgu() {
        configurarTabla();
        configurarColumnas();
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
        ConfiguracionIgu.configuracionListaEnCombo(cmbEstado, estadosHabilitados);
    }

    private void cargarTodosLosEventos() {
        try {
            // Utilizando el evento Servicio, obtengo todos los eventos servicios (Confirmados/EnEjecucion/Finalizados) Inscribibles
            listaEventosCompleta = eventoServicio.obtenerEventosPorEstado(estadosHabilitados, true);
            // Agrego todos los elementos a la lista observable asociada a la tabla de los eventos
            listaEventos.setAll(listaEventosCompleta);
        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar los eventos: " + e.getMessage());
        }
    }

    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblEvento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurar texto para tablas vacías
        tblEvento.setPlaceholder(new Label("No hay eventos para mostrar"));
        tblParticipantes.setPlaceholder(new Label("Seleccione un evento para ver los participantes"));

        // Simula proporciones para tblEvento 
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        colInscriptos.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        colEstado.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        // Simula proporciones para tblParticipantes
        colDNI.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        colNombreCompleto.setMaxWidth(1f * Integer.MAX_VALUE * 35);
        colTelefono.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        colCorreo.setMaxWidth(1f * Integer.MAX_VALUE * 25);
    }

    private void configurarColumnas() {
        // Configuraciones de las columnas de la tabla Eventos
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colInscriptos.setCellValueFactory(cellData ->{return new SimpleObjectProperty<>(cellData.getValue().getPersonasPorRol(TipoRol.PARTICIPANTE).size());});
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configuracion de las columnas de la tabla Participantes
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombreCompleto.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getNombre() + " " + cellData.getValue().getApellido());});
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        
        // Configuraciones de celdas
        ConfiguracionIgu.configurarColumnaFecha(colFechaInicio);
        ConfiguracionIgu.configurarColumnaEstado(colEstado);
    }

    // === MÉTODO DE FILTRADO COMBINADO ===
    @FXML
    private void filtrarEvento() {
        try {
            // Obtenemos ambos filtros seleccionados
            TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
            EstadoEvento estadoSeleccionado = cmbEstado.getSelectionModel().getSelectedItem();
            
            // Aplicamos el filtrado combinado
            List<Evento> eventosFiltrados = aplicarFiltrosCombinados(tipoSeleccionado, estadoSeleccionado);

            listaEventos.setAll(eventosFiltrados);
            tblEvento.refresh();
            
            // Limpiar tabla de participantes al cambiar filtros
            limpiarTablaParticipantes();
            
        } catch (Exception e) {
            Alerta.mostrarError("Error al aplicar filtros: " + e.getMessage());
        }
    }
    
    private List<Evento> aplicarFiltrosCombinados(TipoEvento tipo, EstadoEvento estado) {
        return listaEventosCompleta.stream()
                .filter(evento -> cumpleFiltroTipo(evento, tipo))
                .filter(evento -> cumpleFiltroEstado(evento, estado))
                .collect(Collectors.toList());
    }
    
    private boolean cumpleFiltroTipo(Evento evento, TipoEvento tipo) {
        // Si no hay filtro de tipo, acepta todos
        if (tipo == null) {
            return true;
        }
        
        // Verificar el tipo específico usando instanceof
        return switch (tipo) {
            case EXPOSICION -> evento instanceof Exposicion;
            case TALLER -> evento instanceof Taller;
            case CONCIERTO -> evento instanceof Concierto;
            case CICLO_DE_CINE -> evento instanceof CicloDeCine;
            case FERIA -> evento instanceof Feria;
        };
    }
    
    private boolean cumpleFiltroEstado(Evento evento, EstadoEvento estado) {
        // Si no hay filtro de estado, acepta todos
        return estado == null || evento.getEstado().equals(estado);
    }

    // === MÉTODO DE PARTICIPANTES 
    @FXML
    private void verParticipantes() {
        try {
            // Obtenemos el Evento seleccionado
            Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();

            // Validamos que haya un evento seleccionado
            if (eventoSeleccionado == null) {
                Alerta.mostrarError("Debe seleccionar un evento para ver sus participantes");
                return;
            }

            // Obtenemos la lista de Participantes/Asistentes asociados a ese evento
            List<Persona> listaParticipantesDelEvento = eventoSeleccionado.getPersonasPorRol(TipoRol.PARTICIPANTE);

            if (!listaParticipantesDelEvento.isEmpty()) {
                // Si la lista no estra vacia, le agrego a la lista obserbalble la lista de los participantes del evento seleccionado
                listaParticipantes.setAll(listaParticipantesDelEvento);
                // Y le agrego a la tabla el nuevo placeHolder.
                tblParticipantes.setPlaceholder(new Label("Seleccione un evento para ver los participantes"));
            } else {
                // Si la lista de participantes asociada al evento esta vacia, muestro un mensaje indicando que no hay participantes inscriptos
                tblParticipantes.setPlaceholder(new Label("Este evento no tiene participantes inscriptos"));
                // Limpio los elementos de la lista de los participantes
                listaParticipantes.clear();
            }
            tblParticipantes.refresh(); 

        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar los participantes: " + e.getMessage());
            listaParticipantes.clear();
        }
    }
    
    // === MÉTODO AUXILIAR ===
    private void limpiarTablaParticipantes() {
        listaParticipantes.clear();
        tblParticipantes.setPlaceholder(new Label("Seleccione un evento para ver los participantes"));
    }
}