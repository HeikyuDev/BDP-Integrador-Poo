package com.mycompany.bdppeventos.controller.ControlEstado;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControlEstadoController implements Initializable {

    // Elementos FXML    
    @FXML
    private ComboBox<EstadoEvento> cmbEstado;
    
    // Tabla
    @FXML
    private TableView<Evento> tblEvento;
    
    // Columnas
    @FXML
    private TableColumn<Evento,String> colNombre, colUbicacion;
    
    @FXML
    private TableColumn<Evento,LocalDate> colFechaInicio, colFechaFin;
    
    @FXML
    private TableColumn<Evento, EstadoEvento> colEstado;
        
    // Servicios
    private EventoServicio eventoServicio;
    
    // Lista Observable asociada a la tabla
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos el eventoServices
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        // Configuramos la IGU
        configurarIGU();    
        // Vinculamos la lista Obserbable con la tabla de eventos
        tblEvento.setItems(listaEventos);        
        // Actualizamos la tabla para obtener todos los eventos de la BD
        actualizarTablaEventos();
    }    
    
    
    // Configiraciones de la IGU
    private void configurarIGU()
    {
        configurarComboEstado();
        configurarTabla();
        configurarColumnas();
    }
    
    
    private void configurarComboEstado()
    {
        ConfiguracionIgu.configuracionEnumEnCombo(cmbEstado, EstadoEvento.class);
        
    }
    
    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblEvento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        

        // Configurar texto para tablas vacías
        tblEvento.setPlaceholder(new Label("No hay eventos para mostrar"));        

        // Simula proporciones para tblEvento 
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 20); 
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 35); 
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 15); 
        colFechaFin.setMaxWidth(1f * Integer.MAX_VALUE * 15); 
        colEstado.setMaxWidth(1f * Integer.MAX_VALUE * 15); 
    }
    
    private void configurarColumnas()
    {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));        
        colFechaFin.setCellValueFactory(cellData -> { return new SimpleObjectProperty<>(cellData.getValue().getFechaFin());});            
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Configuracionws de Celdas
        ConfiguracionIgu.configurarColumnaEstado(colEstado);
        ConfiguracionIgu.configurarColumnaFecha(colFechaInicio);
        ConfiguracionIgu.configurarColumnaFecha(colFechaFin);
    }
    
    @FXML
    private void filtrarPorEstado()
    {        
        try {
            // Obtengo el estado seleccionado por el usuario (Puede ser null)
            EstadoEvento estadoSeleccionado = cmbEstado.getSelectionModel().getSelectedItem();
            // Declaramos la listaFiltrada de los elementos qur va a tener la tabla
            List<Evento> listaFiltrada = obtenerListaPorEstado(estadoSeleccionado);
            // Agrego los nuevos elementos a la tabla (A traves de su observableList)
            listaEventos.setAll(listaFiltrada);
            // Refresco la tabla para que impacten los cambios
            tblEvento.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        }
    }
    
    @FXML
    private void confirmarEvento() // Si el Evento esta planificado se debe confirmar. 
    {
        try {
            // Obtenemos el evento seleccionado de la tabla
            Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();
            // Validamos si es null
            if(eventoSeleccionado == null)
            {
                throw new IllegalArgumentException("Se debe seleccionar un Evento para poder Confirmarlo");
            }
            // Validamos si esta en estado Planificado
            if(!eventoSeleccionado.getEstado().equals(EstadoEvento.PLANIFICADO))
            {
                throw new IllegalArgumentException("El evento tiene que estar en estado PLANIFICADO");
            }
            // Confirmación de la acción
            if (!Alerta.confirmarAccion("¿Confirmar el Evento: '" + eventoSeleccionado.getNombre() + "'?")) {
                return;
            }

            // Cambiamos el estado del evento planificado a "CONFIRMADO"
            eventoServicio.confirmarEvento(eventoSeleccionado);                        
            Alerta.mostrarExito("Evento '" + eventoSeleccionado.getNombre() + "' confirmado exitosamente!");                        
            eventoServicio.actualizarEstadoEventos();
            actualizarTablaEventos();
            // Actualizo los estados de los eventos, util si un evento creado en el mismo dia cambia de estado al pasarle el estado a confirmado
            // Ejemplo un Evento creado con la fecha de hoy planificado al confirmalo pasa automaticaMENTE a en ejecucion
            
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: Asegurese de seleccionar un Evento valido: " + e.getMessage());
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        }
        
    }
    
    @FXML
    private void cancelarEvento() // Se deben poder cancelar todos los eventos menos los Finalizados
    {        
        try {
            // Obtenemos el evento seleccionado de la tabla
            Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();
            // Validamos si es null
            if(eventoSeleccionado == null)
            {
                throw new IllegalArgumentException("Se debe seleccionar un Evento para poder Cancelarlo");
            }
            // Validamos si esta en estado Finalizado
            if(eventoSeleccionado.getEstado().equals(EstadoEvento.FINALIZADO))
            {
                throw new IllegalArgumentException("No se puede cancelar un Evento Finalizado");
            }
            // Validamos si ya esta cancelado
            if(eventoSeleccionado.getEstado().equals(EstadoEvento.CANCELADO))
            {
                throw new IllegalArgumentException("Este evento ya esta cancelado");
            }
            // Confirmación de la acción
            if (!Alerta.confirmarAccion("¿Cancelar Evento: '" + eventoSeleccionado.getNombre() + "'?")) {
                return;
            }
            // Cambiamos el estado del evento a "CANCELADO"            
            eventoServicio.cancelarEvento(eventoSeleccionado);            
            Alerta.mostrarExito("Evento '" + eventoSeleccionado.getNombre() + "' Cancelado exitosamente!");            
            actualizarTablaEventos();            
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: Asegurese de seleccionar un Evento valido: " + e.getMessage());
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
        }
    }
    
    
    // Metodos Especificos
    private void actualizarTablaEventos()
    {
        try {
            // Si hay un filtro aplicado, mantenerlo
            EstadoEvento estadoSeleccionado = cmbEstado.getSelectionModel().getSelectedItem();                        
            // Si hay filtro, aplicar el filtro actual
            List<Evento> listaPorEstado = obtenerListaPorEstado(estadoSeleccionado);
            listaEventos.setAll(listaPorEstado);
            tblEvento.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar la tabla: " + e.getMessage());
        }        
    }
        
    
    private List<Evento> obtenerListaPorEstado(EstadoEvento estadoSeleccionado) {
        try {                        
            // Obtenemos todos los eventos
            List<Evento> todosLosEventos = eventoServicio.buscarTodos();

            // Si no selecciona nada en la combo, retorna todas las instancias
            if (estadoSeleccionado == null) {
                return todosLosEventos;
            }

            // Filtramos usando Streams 
            return todosLosEventos.stream()
                    .filter(evento -> evento.getEstado().equals(estadoSeleccionado))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un Error inesperado: " + e.getMessage());
            return new ArrayList<>();
        }
    }        
}
