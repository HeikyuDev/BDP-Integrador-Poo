package com.mycompany.bdppeventos.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogUtils {

    /**
     * Muestra un diálogo modal centrado en pantalla con el contenido dado.
     */
    public static void showCenteredDialog(Parent content) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setScene(new Scene(content));
        dialog.centerOnScreen();
        dialog.showAndWait();
    }

}
