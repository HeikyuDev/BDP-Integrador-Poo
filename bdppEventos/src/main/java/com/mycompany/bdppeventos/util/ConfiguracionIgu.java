package com.mycompany.bdppeventos.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.TipoRol;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public abstract class ConfiguracionIgu {

    // === CONFIGURACIONES PARA LOS TEXTFIELDS ===
    
    public static void configuracionCheckTextfield(CheckBox unCheckbox, TextField unTextfield) {
        if (unCheckbox.isSelected()) {
            unTextfield.setDisable(false);
            unTextfield.setEditable(true);
        } else {
            unTextfield.setText("");
            unTextfield.setDisable(true);
        }
    }
    

    // === CONFIGURACION PARA LOS COMBOBOX/CHECKCOMBO ===
    
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
    // 1) Limpia los checks anteriores
    checkCombo.getCheckModel().clearChecks();

    // 2) Limpia los elementos existentes
    checkCombo.getItems().clear();

    // 3) Pasa la lista a ObservableList y la asigna
    if (lista != null) {
        checkCombo.getItems().setAll(FXCollections.observableArrayList(lista));
    }
}

    
    // === CONFIGURACIONES DE BOTONES ===        

    // Configuracion del boton "Modificar"
    public static void configuracionBtnModificar(Button btnAlta, Button btnModificacion, Button btnBaja,
            Button btnCancelar) {
        // Configuraciones de Elementos Graficos
        btnAlta.setText("Guardar Cambios"); // Cambiamos el texto del boton a "Guardar Cambios"
        btnModificacion.setDisable(true); // Desabilitamos el btn de modificacion para que el usuario no pueda volver a
                                          // precionarlo
        btnBaja.setDisable(true);
        btnCancelar.setDisable(false);
    }

    public static void configuracionBase(Button btnAlta, Button btnModificacion, Button btnBaja,
            Button btnCancelar, String txtBtnAlta) {
        btnAlta.setText(txtBtnAlta);
        btnModificacion.setDisable(false);
        btnBaja.setDisable(false);
        btnCancelar.setDisable(true);
    }

    // === CONFIGURACIONES PARA LAS COLUMNAS ===

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
    
    
    public static SimpleStringProperty formatCupoMaximoEvent(Evento evento) {
        if (evento.isTieneCupo()) {
            return new SimpleStringProperty(String.valueOf(evento.getPersonasPorRol(TipoRol.PARTICIPANTE).size() + " / " +evento.getCapacidadMaxima()));
        } else {
            return new SimpleStringProperty("Ilimitado");
        }
    }        
        

}
