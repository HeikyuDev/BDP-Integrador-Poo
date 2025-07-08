package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class FormularioPersonaControlador {

    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private CheckBox chkActivo;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnBajaAlta;
    @FXML
    private Button btnLimpiar;

    // Este método se llama automáticamente después de cargar el FXML
    @FXML
    public void initialize() {
        // Por ejemplo, podrías inicializar valores aquí
    }

    @FXML
    private void handleGuardar() {
        String dni = txtDni.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        boolean activo = chkActivo.isSelected();

        System.out.println("Guardando/Modificando persona:");
        System.out.println("DNI: " + dni);
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Correo: " + correo);
        System.out.println("Activo: " + activo);

        // Aquí podrías llamar a un servicio para guardar en DB
    }

    @FXML
    private void handleBajaAlta() {
        boolean activo = chkActivo.isSelected();
        if (activo) {
            chkActivo.setSelected(false);
            btnBajaAlta.setText("Dar Alta");
            System.out.println("Persona dada de baja.");
        } else {
            chkActivo.setSelected(true);
            btnBajaAlta.setText("Dar Baja");
            System.out.println("Persona dada de alta.");
        }
    }

    @FXML
    private void handleLimpiar() {
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        chkActivo.setSelected(false);
        btnBajaAlta.setText("Dar Baja");
        System.out.println("Formulario limpiado.");
    }
}
