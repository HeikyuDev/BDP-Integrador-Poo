package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

import org.controlsfx.control.SearchableComboBox;

import com.mycompany.bdppeventos.model.entities.Persona;

public class FormularioPersonaControlador {

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnNuevo;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private SearchableComboBox<?> cmbRol;

    @FXML
    private Label lblRol;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDni;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    private Persona personaInicial;
    private ObservableList<Persona> nuevasPersonas = FXCollections.observableArrayList();

    @FXML
    void guardar(ActionEvent event) {

    }

    @FXML
    void limpiar(ActionEvent event) {

    }

    @FXML
    void nuevo(ActionEvent event) {

    }

    public void setPersonaInicial(Persona personaInicial) {
        this.personaInicial = personaInicial;
        if (personaInicial != null) {
            txtDni.setText(personaInicial.getDni());
            txtNombre.setText(personaInicial.getNombre());
            txtApellido.setText(personaInicial.getApellido());
            txtTelefono.setText(personaInicial.getTelefono());
            txtCorreo.setText(personaInicial.getCorreoElectronico());
            /* cmbRol.setSelectedItem(personaInicial.getRol());
            chkActivo.setSelected(personaInicial.isActivo());
            */
        }
    }

    public List<Persona> getPersonas() {
        return nuevasPersonas;
        
    }

}
