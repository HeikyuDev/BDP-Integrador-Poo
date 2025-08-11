package com.mycompany.bdppeventos.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
        // Validación del ComboBox
        if (combo == null) {
            throw new IllegalArgumentException("El ComboBox no puede ser nulo");
        }
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
        // Agrego un Elemento null
        combo.getItems().add(null);
        combo.getItems().addAll(lista);

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
    public static <T> SimpleStringProperty formatBoolean(boolean valor) {
        if (valor == true) {
            return new SimpleStringProperty("SI");
        } else {
            return new SimpleStringProperty("NO");
        }
    }

    // Configuracion para visualizar el tipo de evento
    // Metodo utilizado para formatear el tipo de evento
    public static SimpleStringProperty obtenerTipoEvento(Evento evento) {
        if (evento instanceof Exposicion) {
            return new SimpleStringProperty(TipoEvento.EXPOSICION.getDescripcion());
        } else if (evento instanceof Taller) {
            return new SimpleStringProperty(TipoEvento.TALLER.getDescripcion());
        } else if (evento instanceof Concierto) {
            return new SimpleStringProperty(TipoEvento.CONCIERTO.getDescripcion());
        } else if (evento instanceof CicloDeCine) {
            return new SimpleStringProperty(TipoEvento.CICLO_DE_CINE.getDescripcion());
        } else if (evento instanceof Feria) {
            return new SimpleStringProperty(TipoEvento.FERIA.getDescripcion());
        } else {
            return new SimpleStringProperty("-");
        }
    }

    public static SimpleStringProperty formatCupoMaximoEvent(Evento evento) {
        if (evento.isTieneCupo()) {
            return new SimpleStringProperty(String.valueOf(
                    evento.getPersonasPorRol(TipoRol.PARTICIPANTE).size() + " / " + evento.getCapacidadMaxima()));
        } else {
            return new SimpleStringProperty("Ilimitado");
        }
    }

    // CONFIGURACIONES DE COLUMNAS CELLFACTORY

    // Método estático que recibe la columna que mostrará el EstadoEvento
    public static void configurarColumnaEstado(TableColumn<Evento, EstadoEvento> columna) {

        // Se asigna una "fábrica de celdas" personalizada para la columna.
        // Esto permite controlar cómo se va a pintar cada celda de esa columna.
        columna.setCellFactory(col -> new TableCell<Evento, EstadoEvento>() {

            // Label que se usará para mostrar el estado dentro de la celda
            private final Label label = new Label();

            @Override
            protected void updateItem(EstadoEvento estado, boolean empty) {
                // Llama al comportamiento por defecto
                super.updateItem(estado, empty);

                // Evita que la celda muestre texto por defecto
                setText(null);
                // Quita cualquier nodo gráfico previo
                setGraphic(null);

                // Si la celda no está vacía y el estado no es null
                if (!empty && estado != null) {
                    // Pone el texto del label con el nombre del enum (PLANIFICADO, CONFIRMADO,
                    // etc.)
                    label.setText(estado.getDescripcion());

                    // Asigna primero la clase base común a todos los estados
                    label.getStyleClass().setAll("label-Estado");

                    // Según el estado, se agrega una clase CSS específica
                    switch (estado) {
                        case CONFIRMADO:
                            label.getStyleClass().add("evento-confirmado");
                            break;
                        case EN_EJECUCION:
                            label.getStyleClass().add("evento-en-ejecucion");
                            break;
                        case FINALIZADO:
                            label.getStyleClass().add("evento-finalizado");
                            break;
                        case PLANIFICADO:
                            label.getStyleClass().add("evento-planificado");
                            break;
                        case CANCELADO:
                            label.getStyleClass().add("evento-cancelado");
                            break;
                    }

                    // Se coloca el label dentro de la celda como contenido gráfico
                    setGraphic(label);
                }
            }
        });
    }

    // Método estático que recibe la columna que mostrará LocalDate
    public static void configurarColumnaFecha(TableColumn<Evento, LocalDate> columna) {
        // Se asigna una "fábrica de celdas" personalizada para la columna.
        // Esto permite controlar cómo se va a mostrar cada fecha de esa columna.
        columna.setCellFactory(col -> new TableCell<Evento, LocalDate>() {
            // Formatter para dar formato a las fechas
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate fecha, boolean empty) {
                // Llama al comportamiento por defecto
                super.updateItem(fecha, empty);

                // Si la celda no está vacía y la fecha no es null
                if (!empty && fecha != null) {
                    // Formatea la fecha y la muestra como texto
                    setText(fecha.format(formatter));
                } else {
                    // Si está vacía, no muestra nada
                    setText(null);
                }
            }
        });
    }

}
