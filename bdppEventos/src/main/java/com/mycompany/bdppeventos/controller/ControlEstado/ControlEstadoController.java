package com.mycompany.bdppeventos.controller.ControlEstado;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ControlEstadoController implements Initializable {

    // Elementos FXML    
    @FXML
    private ComboBox<EstadoEvento> cmbEstado;
    
    // Tabla
    @FXML
    private TableView<Evento> tblEvento;
    
    // Columnas
    @FXML
    private TableColumn<Evento,String> colNombre, colUbicacion, colFechaInicio, colFechaFin, colEstado;
        
    // Servicios
    private EventoServicio eventoServicio;
    
    // Lista Observable asociada a la tabla
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos el eventoServices
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        // Vinculamos la lista Obserbable con la tabla de eventos
        tblEvento.setItems(listaEventos);
        // Actualizamos la tabla para obtener todos los eventos de la BD
        actualizarTablaEventos();
    }    
    
    
    // Metodos "onAction"
    
    @FXML
    private void filtrarPorEstado()
    {
        // Obtengo los datos del Combo, y determino de que tipo se trata
    }
    
    @FXML
    private void confirmarEvento()
    {
        // Si el Evento esta planificado se debe confirmar. 
    }
    
    @FXML
    private void cancelarEvento()
    {
        // Se deben poder cancelar todos los eventos menos los Finalizados
    }
    
    
    // Metodos Especificos
    private void actualizarTablaEventos()
    {
        // Metodo que va a ser utilizado cuando:
           // 1. Se Cambie el estado del Evento CONFIRMADO -> CANCELADO
           // 2. Cuando se cancele un Evento
        
    }
}
