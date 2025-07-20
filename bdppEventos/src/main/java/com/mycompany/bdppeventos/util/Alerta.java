package com.mycompany.bdppeventos.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class Alerta {

    // Muestra una alerta informativa (éxito)
    public static void mostrarExito(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION); // Crea una alerta de tipo información
        alerta.setTitle("Éxito"); // Título de la ventana
        alerta.setHeaderText(null); // Sin encabezado
        alerta.setContentText(mensaje); // Mensaje principal
        alerta.showAndWait(); // Espera a que el usuario la cierre
    }

    // Muestra una alerta de error
    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR); // Crea una alerta de tipo error
        alerta.setTitle("Error"); // Título de la ventana
        alerta.setHeaderText("Se produjo un error"); // Encabezado
        alerta.setContentText(mensaje); // Mensaje principal
        alerta.showAndWait(); // Espera a que el usuario la cierre
    }

    // Muestra una alerta de confirmación y devuelve true si se presiona OK
    public static boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(AlertType.CONFIRMATION); // Crea una alerta de confirmación
        alerta.setTitle("Confirmación"); // Título
        alerta.setHeaderText("¿Estás seguro?"); // Encabezado
        alerta.setContentText(mensaje); // Mensaje principal
        Optional<ButtonType> resultado = alerta.showAndWait(); // Espera respuesta del usuario
        return resultado.isPresent() && resultado.get() == ButtonType.OK; // Devuelve true si eligió OK
    }

    // Muestra una alerta de error o éxito, dependiendo del booleano
    public static void mostrarMensaje(boolean error, String titulo, String contenido) {
        Alert alert;

        if (error) {
            alert = new Alert(AlertType.ERROR); // Si es error, tipo ERROR
        } else {
            alert = new Alert(AlertType.INFORMATION); // Si no, tipo INFORMACIÓN
        }

        alert.setHeaderText(null); // Sin encabezado
        alert.setTitle(titulo); // Título de la ventana

        Label contenidoAjustado = new Label(contenido); // Etiqueta con contenido
        contenidoAjustado.setWrapText(true); // Para que el texto se acomode
        alert.getDialogPane().setContent(contenidoAjustado); // Mostrar la etiqueta

        alert.showAndWait(); // Espera a que el usuario cierre
    }
}
