package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

public class ListaPersonasControlador {

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnModificar;

    @FXML
    private SearchableComboBox<?> cmbRol;

    @FXML
    private TableColumn<?, ?> colDni;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colNombreCompleto;

    @FXML
    private TableColumn<?, ?> colRol;

    @FXML
    private TableColumn<?, ?> colTelefono;

    @FXML
    private TableView<?> tblPersonas;

    @FXML
    private TextField txtDni;

    @FXML
    private TextField txtNombreCompleto;

    @FXML
    void agregar(ActionEvent event) {

    }

}
