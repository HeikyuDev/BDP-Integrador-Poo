
/**
 * Panel controlador para la gestión de ciclos de cine dentro del ABM de eventos.
 * Permite inicializar la vista y manejar la acción de agregar un nuevo ciclo de cine.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.util.DialogUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PanelCicloCineController implements Initializable {

    /**
     * Inicializa el panel de ciclo de cine al cargar la vista.
     * Aquí se debe implementar la lógica para mostrar todos los ciclos cargados en la base de datos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // IMPLEMENTAR: cargar y mostrar todos los ciclos de cine desde la base de datos
    }

    
    
    
    @FXML
    private void agregarCiclo() {
        try {
        // 1) Cargo el FXML de tu formulario de Ciclo de Cine
        Parent content = FXMLLoader.load(getClass().getResource("/fxml/ABMProyeccion/FormularioProyeccion.fxml"));
        
        // 2) Muestro el diálogo centrado y modal
        DialogUtils.showCenteredDialog(content);
    } catch (IOException ex) {
        ex.printStackTrace();
        // Aquí podrías mostrar un Alert de error si prefieres
    }
        
    }        
    
}
