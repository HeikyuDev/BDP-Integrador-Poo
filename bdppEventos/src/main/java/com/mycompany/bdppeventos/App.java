package com.mycompany.bdppeventos;

import com.mycompany.bdppeventos.repository.Repositorio;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    // Metodo que se ejecuta automaticamente al iniciar la aplicacion antes de start
    @Override
    public void init() {
        try {
            // Crea una instancia de EntityManagerFactory utilizando una unidad de
            // persistencia definida
            var emf = Persistence.createEntityManagerFactory("EventosPU");
           
            System.out.println("📦 Creando repositorio...");
            RepositorioContext.setRepositorio(new Repositorio(emf));

            if (RepositorioContext.getRepositorio() == null) {
                throw new RuntimeException("No se pudo crear el Repositorio - Repositorio es null");
            } else {
                System.out.println("✅ Repositorio creado exitosamente");
            }

        } catch (Exception e) {
            System.out.println("Ocurrio un Error intentando inicializar el Entity Manager Factory" + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        // Establece el stage principal en StageManager
        StageManager.setStagePrincipal(stage);

        // 1) Actualiza estados justo al arrancar
        EventoServicio eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        eventoServicio.actualizarEstadoEventos();
        
        // Cambia la escena a la vista de Pantalla Principal
        StageManager.cambiarEscena(Vista.PantallaPrincipal);
    }

    public static void main(String[] args) {
        System.out.println("Bienvenido a BDPPEventos");
        launch(args);
    }

}
