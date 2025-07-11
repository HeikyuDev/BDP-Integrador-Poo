package com.mycompany.bdppeventos.controller.ABMParticipante;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InscribirParticipanteController {

    // Solo las declaraciones de los elementos FXML
    @FXML
    private ComboBox<?> cbEventos;
    @FXML
    private ComboBox<?> cbPersonas;
    @FXML
    private ComboBox<?> cbRoles;
    @FXML
    private TextArea taInfoEvento;
    @FXML
    private TextArea taInfoPersona;
    @FXML
    private Label lblCupoDisponible;
    @FXML
    private Button btnInscribir;
    @FXML
    private Button btnBuscarEventos;
    @FXML
    private Button btnBuscarPersonas;
    @FXML
    private Button btnNuevaPersona;
    @FXML
    private Button btnCancelar;

    @FXML
    public void initialize() {
        // Vacío - solo para cargar la vista
    }
}
