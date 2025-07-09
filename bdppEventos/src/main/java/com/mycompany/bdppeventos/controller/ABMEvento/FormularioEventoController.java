package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FormularioEventoController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("FormularioEventoController inicializado correctamente");
    }    
    
    // ⭐ MÉTODOS QUE EL FXML ESTÁ BUSCANDO ⭐
    
    @FXML
    private void onTipoEventoChanged() {
        System.out.println("Tipo de evento cambiado");
        // TODO: Implementar lógica cuando cambie el tipo de evento
    }
    
    @FXML
    private void altaEvento() {
        System.out.println("Botón Alta presionado");
        // TODO: Implementar lógica para dar de alta el evento
    }
}