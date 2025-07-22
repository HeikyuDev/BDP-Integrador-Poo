package com.mycompany.bdppeventos.controller.ABMPelicula;


import com.mycompany.bdppeventos.model.entities.Pelicula;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class FormularioPeliculaController implements Initializable {

    

    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextField txtDuracion;

    @FXML
    private TableView<Pelicula> tblPelicula;
    @FXML
    private TableColumn<Pelicula, Integer> colId;
    @FXML
    private TableColumn<Pelicula, String> colNombre;
    @FXML
    private TableColumn<Pelicula, Double> colDuracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    
    // Metodos OnActioin
    
        
    
    @FXML
    private void altaPelicula()
    {
        // TODO: No implementado
    }
    
    @FXML
    private void bajaPelicula()
    {
        // TODO: No Implementado
    }
    
    @FXML
    private void modificacionPelicula()
    {
        // TODO: No Implementado
    }

}
