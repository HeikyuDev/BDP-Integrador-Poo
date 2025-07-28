package com.mycompany.bdppeventos.controller.VerParticipantes;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class VerParticipantesController implements Initializable {

    // Controles
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;
    
    // Tablas
    
    @FXML
    private TableView<Evento> tblEvento;
    
    @FXML
    private TableColumn< Evento, String> colNombre;
    
    @FXML
    private TableColumn< Evento, String> colUbicacion;
    
    @FXML
    private TableColumn< Evento, LocalDate> colFechaInicio;
    
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
    
    @Override
public void initialize(URL url, ResourceBundle rb) {
    // Aplica política de ajuste automático
    tblEvento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tblParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Simula proporciones para tblEvento (ejemplo: 30%-40%-30)
    colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%
    colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40%
    colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%

    // Simula proporciones para tblParticipantes (ejemplo: 15%-35%-25%-25)
    colDNI.setMaxWidth(1f * Integer.MAX_VALUE * 15);
    colNombreCompleto.setMaxWidth(1f * Integer.MAX_VALUE * 35);
    colTelefono.setMaxWidth(1f * Integer.MAX_VALUE * 25);
    colCorreo.setMaxWidth(1f * Integer.MAX_VALUE * 25); // Este campo faltaba en tu controlador
}
   
    
    
    
    // Metodos de Controles
    
    @FXML
    private void filtrarEvento()
    {
        System.out.println("Evento Filtrado");
    }
    
    @FXML
    private void verParticipantes()
    {
        System.out.println("Participantes Mostrados");
    }
    
    // Metodos Especificos
    
    
    
    
}
