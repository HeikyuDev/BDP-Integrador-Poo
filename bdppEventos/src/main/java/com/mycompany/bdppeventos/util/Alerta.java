package com.mycompany.bdppeventos.util;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.mycompany.bdppeventos.App;

public class Alerta {
    
    // Muestra una alerta informativa (éxito)
    public static void mostrarExito(String mensaje) {
        Alert alerta = crearAlerta(AlertType.INFORMATION, "Éxito", null, mensaje);
        ponerIcono(alerta);
        alerta.showAndWait();
    }
    
    // Muestra una alerta de error
    public static void mostrarError(String mensaje) {
        Alert alerta = crearAlerta(AlertType.ERROR, "Error", "Se produjo un error", mensaje);
        ponerIcono(alerta);
        alerta.showAndWait();
    }
    
    // Muestra una alerta de confirmación y devuelve true si se presiona OK
    public static boolean confirmarAccion(String mensaje) {
        Alert alerta = crearAlerta(AlertType.CONFIRMATION, "Confirmación", "¿Estás seguro?", mensaje);
        ponerIcono(alerta);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
    
    // Muestra una alerta de error o éxito, dependiendo del booleano
    public static void mostrarMensaje(boolean error, String titulo, String contenido) {
        AlertType tipo = error ? AlertType.ERROR : AlertType.INFORMATION;
        Alert alerta = crearAlerta(tipo, titulo, null, contenido);
        ponerIcono(alerta);
        alerta.showAndWait();
    }
    
    // Método privado para crear alertas con configuración común
    private static Alert crearAlerta(AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        
        // Configuración para que el texto no se corte
        Label etiqueta = new Label(mensaje);
        etiqueta.setWrapText(true);
        etiqueta.setPadding(new Insets(10));
        etiqueta.setMaxWidth(Double.MAX_VALUE);
        
        DialogPane dialogPane = alerta.getDialogPane();
        dialogPane.setContent(etiqueta);
        dialogPane.setPrefWidth(800); // Ancho preferido para mostrar mejor el texto
        
        return alerta;
    }
    
    // Método simple para poner el icono
    private static void ponerIcono(Alert alerta) {
        try {
            // Cargar el mismo icono que usa tu app principal
            Image icono = new Image(App.class.getResource("/images/Logo.png").toExternalForm());
            
            // Obtener el Stage de la alerta y agregar el icono
            Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icono);
            
            // Opcional: deshabilitar maximización
            stage.setResizable(false);
            
        } catch (Exception e) {
            // Si no se puede cargar el icono, simplemente continúa sin él
            System.out.println("No se pudo cargar el icono para la alerta");
        }
    }
}