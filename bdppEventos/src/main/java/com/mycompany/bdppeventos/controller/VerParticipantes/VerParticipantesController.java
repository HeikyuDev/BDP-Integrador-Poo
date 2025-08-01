package com.mycompany.bdppeventos.controller.VerParticipantes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;

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

    // Controles
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;

    // Tablas
    @FXML
    private TableView<Evento> tblEvento;

    @FXML
    private TableColumn<Evento, String> colNombre;

    @FXML
    private TableColumn<Evento, String> colUbicacion;

    @FXML
    private TableColumn<Evento, String> colFechaInicio;

    @FXML
    private TableView<Persona> tblParticipantes;

    @FXML
    private TableColumn<Persona, String> colDNI;

    @FXML
    private TableColumn<Persona, String> colNombreCompleto;

    @FXML
    private TableColumn<Persona, String> colTelefono;

    @FXML
    private TableColumn<Persona, String> colCorreo;

    // Listas Observables Asociadas a las table view
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    private ObservableList<Persona> listaParticipantes = FXCollections.observableArrayList();

    // Servicios
    private EventoServicio eventoServicio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los Servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());

        // Configuramos la IGU
        configurarTabla();
        configurarColumnas();
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);

        // Asociamos las listas Observables a Las tablas
        tblEvento.setItems(listaEventos);
        tblParticipantes.setItems(listaParticipantes);

        // Cargamos todos los eventos al inicializar
        cargarTodosLosEventos();
    }

    private void cargarTodosLosEventos() {
        try {
            List<Evento> todosLosEventos = eventoServicio.obtenerEventosConfirmadosInscribibles();
            listaEventos.setAll(todosLosEventos);
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

        // Simula proporciones para tblEvento (ejemplo: 30%-40%-30%)
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40%
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%

        // Simula proporciones para tblParticipantes (ejemplo: 15%-35%-25%-25%)
        colDNI.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        colNombreCompleto.setMaxWidth(1f * Integer.MAX_VALUE * 35);
        colTelefono.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        colCorreo.setMaxWidth(1f * Integer.MAX_VALUE * 25);
    }

    private void configurarColumnas() {
        // Configuraciones de las columnas de la tabla Eventos
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colFechaInicio
                .setCellValueFactory(cellData -> ConfiguracionIgu.formatFecha(cellData.getValue().getFechaInicio()));

        // Configuracion de las columnas de la tabla Participantes
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombreCompleto.setCellValueFactory(cellData -> {
            Persona persona = cellData.getValue();
            return new SimpleStringProperty(persona.getNombre() + " " + persona.getApellido());
        });
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
    }

    // Metodos de Controles
    @FXML
    private void filtrarEvento() {
        try {
            // Obtenemos el Tipo de Evento Seleccionado
            TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
            List<Evento> listaPorTipo = obtenerListaPorTipo(tipoSeleccionado);

            listaEventos.setAll(listaPorTipo);
            tblEvento.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error Inesperado: " + e.getMessage());
        }
    }

    private List<Evento> obtenerListaPorTipo(TipoEvento unTipoEvento) {
        // Si es null, devolver todos los eventos
        if (unTipoEvento == null) {
            return eventoServicio.obtenerEventosConfirmadosInscribibles();
        }

        List<Evento> listaEventoFiltrada = new ArrayList<>();
        switch (unTipoEvento) {
            case EXPOSICION -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Exposicion) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case TALLER -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Taller) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CONCIERTO -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Concierto) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CICLO_DE_CINE -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof CicloDeCine) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case FERIA -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Feria) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
        }
        return listaEventoFiltrada;
    }

    @FXML
    private void verParticipantes() {
        System.out.println("Participantes Mostrados");
        // TODO: Implementar funcionalidad para mostrar participantes del evento
        // seleccionado
    }

    // Metodos Especificos
}