package com.mycompany.bdppeventos;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;
import javafx.application.Application;
import javafx.stage.Stage;



public class App extends Application {

    

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
        // Establece el stage principal en StageManager
        StageManager.setStagePrincipal(stage);

        // Cambia la escena a la vista de Pantalla Principal
        StageManager.cambiarEscena(Vista.PantallaPrincipal);
    }
    
   

    public static void main(String[] args) {
        launch(args);        
        System.out.println(Vista.PantallaPrincipal.getRutaFxml());
    }

}
