package com.mycompany.bdppeventos.controller.ABMProyeccion;

import com.mycompany.bdppeventos.model.entities.Pelicula;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.CheckComboBox;

public class FormularioProyeccionController implements Initializable {

    
    @FXML
    private CheckComboBox<Pelicula> chkComboPeliculas;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
