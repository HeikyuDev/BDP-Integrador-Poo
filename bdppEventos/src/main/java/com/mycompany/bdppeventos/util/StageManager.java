package com.mycompany.bdppeventos.util;

import java.io.IOException;
import java.util.HashMap;

import com.mycompany.bdppeventos.App;
import com.mycompany.bdppeventos.view.Vista;

import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

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
            Alerta.mostrarError("No se pudo cargar la vista:" + vista.getRutaFxml());
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

    // Cambia la escena en un determinado contenedor
    public static void cambiarEscenaEnContenedor(AnchorPane contenedor, Vista vista) {
        Parent root = cargarVista(vista.getRutaFxml()); // Carga la vista FXML
        if (root != null && contenedor != null) {
            mostrarEnContenedor(contenedor, root);
        } else {
            Alerta.mostrarError("No se pudo cargar la vista en el contenedor: " + vista.getRutaFxml());
        }
    }

    public static <T> T cambiarEscenaEnContenedorYObtenerControlador(AnchorPane contenedor, Vista vista) {
    Pair<T, Parent> resultado = cargarVistaConControlador(vista.getRutaFxml());
    if (resultado != null) {
        Parent root = resultado.getValue();
        T controlador = resultado.getKey();
        mostrarEnContenedor(contenedor, root);
        return controlador;
    } else {
        Alerta.mostrarError("No se pudo cargar la vista con controlador: " + vista.getRutaFxml());
        return null;
    }
}

    // Mustra el nodoRaiz pasado como parametro en el contenedor
    private static void mostrarEnContenedor(AnchorPane contenedor, Parent root) {
        if (root != null) {
            contenedor.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
        }
        else
        {
            Alerta.mostrarError("No se pudo mostrar el panel");
        }

    }

    

    // Abre un modal con la vista proporcionada
    public static void abrirModal(Vista vista) {
        Parent root = cargarVista(vista.getRutaFxml());
        Stage modalStage = crearModalStage(vista); // Crea un nuevo Stage para el modal
        mostrarModal(modalStage, root, vista); // Muestra el modal
    }

    // Crea un nuevo Stage para el modal
    private static Stage crearModalStage(Vista vista) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL); // Configura el modal como bloqueante        
        return modalStage;
    }
   

    // Muestra el modal con la vista proporcionada
    private static void mostrarModal(Stage modalStage, Parent root, Vista vista) {
        Scene escena = new Scene(root); // Crea una nueva escena para el modal
        modalStage.setTitle(vista.getTitulo()); // Establece el título del modal
        modalStage.setScene(escena);
        modalStage.sizeToScene();
        modalStage.centerOnScreen();
        modalStage.setResizable(false);
        modalStage.getIcons().add(new Image(App.class.getResource("/images/logo.png").toExternalForm()));        
        modalStage.showAndWait(); // Muestra el modal y espera hasta que se cierre
    }

    // Cierra un modal asociado al título proporcionado
    public static void cerrarModal(Vista vista) {
        Stage modalStage = modalStages.get(vista.getTitulo()); // Obtiene el modal del mapa
        if (modalStage != null) {
            modalStage.close(); // Cierra el modal si existe
            modalStages.remove(vista.getTitulo()); // Elimina el modal del mapa
        } else {
            Alerta.mostrarError("No se encontró un modal con el título: " + vista.getTitulo()); // Advierte si no se encuentra el modal
        }
    }

    // Carga una vista FXML y devuelve el nodo raíz
    public static Parent cargarVista(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(rutaFxml)); // Carga el archivo FXML
            Parent root = loader.load(); // Carga el nodo raíz de la vista
            return root;
        } catch (IOException | NullPointerException e) {
            Alerta.mostrarError("Error al cargar la vista\n" + e.getMessage());
            return null;
        }
    }

    // Carga una vista FXML  y devuelve el controlador y el nodo raíz
    public static <T> Pair<T, Parent> cargarVistaConControlador(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath)); // Carga el archivo FXML
            Parent nodo = loader.load(); // Carga el nodo raíz de la vista
            T controlador = loader.getController(); // Obtiene el controlador asociado
            return new Pair<>(controlador, nodo); // Devuelve el controlador y el nodo raíz como un par
        } catch (IOException e) {            
            Alerta.mostrarError("A ocurrido un Error inesperado\n" + e.getMessage());
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
