package com.mycompany.bdppeventos.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public abstract class ConfiguracionIgu {

    /**
     * Clase dirigida a reutilizar metodos de las interfaces de la seccion "ABM
     * EVENTOS". Permite centralizar la lógica de configuración de controles en
     * los formularios de eventos.
     */
    // Configuraciion que permite que Cuando se precione un Checkbox se Habilie el
    // txtfield
    public void configuracionCheckTextfield(CheckBox unCheckbox, TextField unTextfield) {
        if (unCheckbox.isSelected()) {
            unTextfield.setDisable(false);
            unTextfield.setEditable(true);
        } else {
            unTextfield.setText("");
            unTextfield.setDisable(true);
        }
    }

    // Configuracion para el ScrollPane
    public static void configuracionScrollPane(ScrollPane scrollPane) {
        // Configuración básica
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Aplicar configuración después de que el ScrollPane esté en la escena
        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                ScrollBar vScrollBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
                if (vScrollBar != null) {
                    vScrollBar.setUnitIncrement(20);
                    vScrollBar.setBlockIncrement(40);
                }
            }
        });
    }

    // Configuracion que llena los comboBox, Pasandole un Enum
    public static <T extends Enum<T>> void configuracionEnumEnCombo(ComboBox<T> combo, Class<T> enumClass) {
        // Limpia Los elementos del combo antes
        combo.getItems().clear();
        // Obtiene todos los valores y los pone en el ComboBox:
        combo.getItems().add(null);
        combo.getItems().addAll(enumClass.getEnumConstants());
    }

    // Configuracion que llena los combobox, pasandole una Lista
    public static <T> void configuracionListaEnCombo(ComboBox<T> combo, List<T> lista) {
        // Validación del ComboBox
        if (combo == null) {
            throw new IllegalArgumentException("El ComboBox no puede ser nulo");
        }

        // Limpiar items existentes
        combo.getItems().clear();

        // Convertir y cargar los nuevos items
        ObservableList<T> itemsObservables;
        if (lista != null && !lista.isEmpty()) {
            itemsObservables = FXCollections.observableArrayList(lista);
        } else {
            itemsObservables = FXCollections.observableArrayList();
            // Opcional: Mostrar mensaje cuando la lista está vacía
            combo.setPromptText("No hay elementos disponibles");
        }

        combo.setItems(itemsObservables);
    }

    // Configuracion Que llena un CheckCombobox pasandole una lista
    public static <T> void configuracionListaEnCheckCombo(CheckComboBox<T> checkCombo, List<T> lista) {
        // Limpiar elementos existentes
        checkCombo.getItems().clear();

        // Crear ObservableList a partir de la lista recibida
        ObservableList<T> itemsObservables;
        if (lista != null) {
            itemsObservables = FXCollections.observableArrayList(lista);
        } else {
            itemsObservables = FXCollections.observableArrayList();
        }

        // Asignar los items al CheckComboBox
        checkCombo.getItems().setAll(itemsObservables);
    }

    // Configuracion del boton "Cancelar"
    protected static void configuracionBtnCancelar(Button btnAlta, Button btnModificacion, Button btnBaja,
            Button btnCancelar, String textoAlta) {
        btnAlta.setText(textoAlta);
        btnModificacion.setDisable(false);
        btnBaja.setDisable(false);
        btnCancelar.setDisable(true);
    }

    // Configuracion del boton "Modificar"
    protected static void configuracionBtnModificar(Button btnAlta, Button btnModificacion, Button btnBaja,
            Button btnCancelar) {
        // Configuraciones de Elementos Graficos
        btnAlta.setText("Guardar Cambios"); // Cambiamos el texto del boton a "Guardar Cambios"
        btnModificacion.setDisable(true); // Desabilitamos el btn de modificacion para que el usuario no pueda volver a
                                          // precionarlo
        btnBaja.setDisable(true);
        btnCancelar.setDisable(false);
    }

    protected static void configuracionFinaly(Button btnAlta, Button btnModificacion, Button btnBaja,
            Button btnCancelar, String txtBtnAlta) {
        btnAlta.setText(txtBtnAlta);
        btnModificacion.setDisable(false);
        btnBaja.setDisable(false);
        btnCancelar.setDisable(true);
    }

    // Configuracion de Columnas "SetValueFactory"
    // Configuracion para listas

    public static <T> SimpleStringProperty formatLista(
            List<T> lista, // <T> Tipo de Elemento de la lista
            Function<T, String> mapper, // mapper Funcion Para convertir cada elemento a String
            String mensajeVacio) // Mensaje para mostrar cuando la lista este vacia
    {

        if (lista == null || lista.isEmpty()) {
            return new SimpleStringProperty(mensajeVacio);
        }

        String resultado = lista.stream()
                .map(mapper)
                .filter(s -> s != null && !s.isEmpty()) // Filtra strings vacíos
                .collect(Collectors.joining(" - ")); // Delimitador fijo

        return new SimpleStringProperty(resultado);
    }

    // Configuracion para Valores booleanos
    public static <T> SimpleStringProperty formatBoolean(boolean valor, String mensajeVerdadero, String mensajeFalso) {
        if (valor == true) {
            return new SimpleStringProperty(mensajeVerdadero);
        } else {
            return new SimpleStringProperty(mensajeFalso);
        }
    }

    public static SimpleStringProperty formatFecha(LocalDate fecha) {        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new SimpleStringProperty(fecha.format(formatter));
    }
    

}
