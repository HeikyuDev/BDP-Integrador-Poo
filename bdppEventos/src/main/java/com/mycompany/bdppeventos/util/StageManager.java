package com.mycompany.bdppeventos.util;


import com.mycompany.bdppeventos.App;
import com.mycompany.bdppeventos.view.Vista;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.stage.StageStyle;



public class StageManager {
    
    // Clase dirigida a controlar las Ventanas, Paneles, Modales....
    
    

    // Stage principal de la aplicación
    private static Stage stagePrincipal;

    // Mapa para gestionar los modales abiertos, asociando títulos con Stages
    private static Map<String, Stage> modalStages = new HashMap<>();

    // Cambia la escena principal a la especificada por la vista proporcionada
    public static void cambiarEscena(Vista vista) {
        Parent root = cargarVista(vista.getRutaFxml()); // Carga la vista FXML
        if (root != null) {
            mostrar(root, vista.getTitulo()); // Muestra la vista en el Stage principal
        } else {
            Alerta.mostrarError("No se pudo cargar la vista:"  + vista.getRutaFxml());
        }
    }

    // Muestra la escena en el Stage principal
    private static void mostrar(final Parent root, String titulo) {
        Scene escena = prepararEscena(root); // Prepara o reutiliza la escena

        if (stagePrincipal != null) {
            // Configura y muestra la escena en el Stage principal
            stagePrincipal.hide();
            stagePrincipal.setTitle(titulo);
            stagePrincipal.setScene(escena);
            stagePrincipal.setMaximized(true);
            stagePrincipal.setResizable(true);       
            stagePrincipal.initStyle(StageStyle.UNDECORATED); 
            stagePrincipal.getIcons().add(new Image(App.class.getResource("/images/Logo.png").toExternalForm()));            
            try {
                stagePrincipal.show();
            } catch (Exception e) {
                Alerta.mostrarError("No se puede mostrar la escena del título: " + titulo);
            }
        } else {
            Alerta.mostrarError("El Stage principal no está inicializado.");
        }
    }

    // Prepara la escena actual o crea una nueva si no existe
    private static Scene prepararEscena(Parent root) {
        Scene escena = stagePrincipal.getScene(); // Obtiene la escena actual del Stage principal
        if (escena == null) {
            escena = new Scene(root); // Crea una nueva escena si no existe
        } else {
            escena.setRoot(root); // Establece el nuevo root en la escena existente
        }
        return escena;
    }

    // Abre un modal con la vista proporcionada
    public static void abrirModal(Parent root, Vista vista) {
        Stage modalStage = crearModalStage(vista); // Crea un nuevo Stage para el modal
        mostrarModal(modalStage, root, vista); // Muestra el modal
    }

    // Crea un nuevo Stage para el modal
    private static Stage crearModalStage(Vista vista) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL); // Configura el modal como bloqueante
        modalStages.put(vista.getTitulo(), modalStage); // Almacena el modal en el mapa con su título
        return modalStage;
    }

    // Cierra un modal asociado al título proporcionado
    public static void cerrarModal(Vista vista) {
        Stage modalStage = modalStages.get(vista.getTitulo()); // Obtiene el modal del mapa
        if (modalStage != null) {
            modalStage.close(); // Cierra el modal si existe
            modalStages.remove(vista.getTitulo()); // Elimina el modal del mapa
        } else {
            Alerta.mostrarError("No se encontró un modal con el título: " + vista.getTitulo());
        }
    }

    // Muestra el modal con la vista proporcionada
    private static void mostrarModal(Stage modalStage, Parent root, Vista vista) {
        Scene escena = new Scene(root); // Crea una nueva escena para el modal
        modalStage.setTitle(vista.getTitulo()); // Establece el título del modal
        modalStage.setScene(escena);
        modalStage.sizeToScene();
        modalStage.centerOnScreen();
        modalStage.setResizable(false);
        modalStage.getIcons().add(new Image(App.class.getResource("images/logo.png").toExternalForm()));
        modalStage.setOnCloseRequest(event -> cerrarModal(vista)); // Configura el cierre del modal
        modalStage.showAndWait(); // Muestra el modal y espera hasta que se cierre
    }

    // Carga una vista FXML y devuelve el nodo raíz
    public static Parent cargarVista(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(rutaFxml)); // Carga el archivo FXML
            Parent root = loader.load(); // Carga el nodo raíz de la vista
            Objects.requireNonNull(root, "El nodo FXML raíz no debe ser nulo"); // Verifica que el nodo no sea nulo
            return root;
        } catch (IOException e) {
            Alerta.mostrarError("Error al cargar la vista\n" + e.getMessage());
            return null;
        }
    }

    // Carga una vista FXML para un modal y devuelve el controlador y el nodo raíz
    public static <T> Pair<T, Parent> cargarVistaConControlador(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath)); // Carga el archivo FXML
            Parent nodo = loader.load(); // Carga el nodo raíz de la vista
            T controlador = loader.getController(); // Obtiene el controlador asociado
            return new Pair<>(controlador, nodo); // Devuelve el controlador y el nodo raíz como un par
        } catch (IOException e) {
            Alerta.mostrarError("Error al cargar el modal FXML: " + fxmlPath);
            return null;
        }
    }

    // Establece el Stage principal de la aplicación
    public static void setStagePrincipal(Stage stage) {
        stagePrincipal = stage;
    }

    // Coloca un título al Stage principal
    public static void setTitulo(String titulo) {
        stagePrincipal.setTitle(titulo);
    }
    
    
}
