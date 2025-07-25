package com.mycompany.bdppeventos.controller.ABMTipoDeArte;



import com.mycompany.bdppeventos.model.entities.Proyeccion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class FormularioTipoDeArteController implements Initializable {
    
    @FXML
    private TextField txtNombre;

    @FXML
    private TableView<Proyeccion> tblProyeccion;
    @FXML
    private TableColumn<Proyeccion, Integer> colId;
    
    @FXML
    private TableColumn<Proyeccion, String> colNombre;
        

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    
    // Metodos OnActioin
    
    
    
    
    @FXML
    private void altaTipoDeArte()
    {
        // TODO: No implementado
    }
    
    @FXML
    private void bajaTipoDeArte()
    {
        // TODO: No Implementado
    }
    
    @FXML
    private void modificacionTipoDeArte()
    {
        // TODO: No Implementado
    }
    
    @FXML
    private void cancelarEdicion() {        
    }

}
