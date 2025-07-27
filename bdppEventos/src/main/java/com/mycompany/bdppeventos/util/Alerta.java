package com.mycompany.bdppeventos.util;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class Alerta {

    // Muestra una alerta informativa (éxito)
    public static void mostrarExito(String mensaje) {
        crearAlerta(AlertType.INFORMATION, "Éxito", null, mensaje).showAndWait();
    }

    // Muestra una alerta de error
    public static void mostrarError(String mensaje) {
        crearAlerta(AlertType.ERROR, "Error", "Se produjo un error", mensaje).showAndWait();
    }

    // Muestra una alerta de confirmación y devuelve true si se presiona OK
    public static boolean confirmarAccion(String mensaje) {
        Alert alerta = crearAlerta(AlertType.CONFIRMATION, "Confirmación", "¿Estás seguro?", mensaje);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    // Muestra una alerta de error o éxito, dependiendo del booleano
    public static void mostrarMensaje(boolean error, String titulo, String contenido) {
        AlertType tipo = error ? AlertType.ERROR : AlertType.INFORMATION;
        crearAlerta(tipo, titulo, null, contenido).showAndWait();
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
        dialogPane.setPrefWidth(500); // Ancho preferido para mostrar mejor el texto
        
        return alerta;
    }
}