package com.mycompany.bdppeventos.controller.ABMProyeccion;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.util.Alerta;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

public class FormularioProyeccionController implements Initializable {

    @FXML
    private CheckComboBox<Pelicula> chkComboPeliculas;

    @FXML
    private TextField txtNombre;

    @FXML
    private TableView<Proyeccion> tblProyeccion;
    @FXML
    private TableColumn<Proyeccion, Integer> colId;
    @FXML
    private TableColumn<Proyeccion, String> colNombre;
    @FXML
    private TableColumn<Proyeccion, String> colPeliculas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    
    // Metodos OnActioin
    
    
    @FXML
    private void agregarPelicula()
    {
        // TODO: No implementado
    }
    
    @FXML
    private void altaProyeccion()
    {
        // TODO: No implementado
    }
    
    @FXML
    private void bajaProyeccion()
    {
        // TODO: No Implementado
    }
    
    @FXML
    private void modificacionProyeccion()
    {
        // TODO: No Implementado
    }

}
