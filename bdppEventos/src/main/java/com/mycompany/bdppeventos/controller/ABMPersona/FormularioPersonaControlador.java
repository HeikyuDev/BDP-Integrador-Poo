package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

public class FormularioPersonaControlador {

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnNuevo;

    @FXML
    private CheckComboBox<TipoRol> chkCmbRol;

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

    private final ObservableList<TipoRol> roles = FXCollections.observableArrayList();

    private PersonaServicio personaServicio;

    @FXML
    public void initialize() {
        System.out.println("✅ FormularioPersonaControlador inicializado correctamente");

        // Inicializar el servicio de persona
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());

        // ✅ CARGAR SOLO ROLES REALES (sin SIN_ROL)
        roles.clear();
        for (TipoRol rol : TipoRol.values()) {
            if (rol != TipoRol.SIN_ROL) { // Excluir SIN_ROL del combo
                roles.add(rol);
            }
        }

        chkCmbRol.getItems().addAll(roles);
        // Configurar el CheckComboBox
        configurarCheckComboBox();

        personaInicial = null;
        nuevasPersonas.clear();

    }

    private void configurarCheckComboBox() {
        // Configurar el título del CheckComboBox
        chkCmbRol.setTitle("Seleccione roles");
        // ✅ NO seleccionar nada por defecto (SIN_ROL se asigna automáticamente si no hay selección)

        // Listener para debug
        chkCmbRol.getCheckModel().getCheckedItems()
                .addListener((javafx.collections.ListChangeListener<TipoRol>) change -> {
                    System.out.println("🔍 DEBUG: Roles seleccionados: " + chkCmbRol.getCheckModel().getCheckedItems());
                });
    }

    @FXML
    void guardar() {
        try {
            String dni = txtDni.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String telefono = txtTelefono.getText().trim().isEmpty() ? null : txtTelefono.getText().trim();
            String correoElectronico = txtCorreo.getText().trim().isEmpty() ? null : txtCorreo.getText().trim();
            // Obtener roles seleccionados del CheckComboBox
            List<TipoRol> rolesSeleccionados = new ArrayList<>(chkCmbRol.getCheckModel().getCheckedItems());

            // ✅ SI NO HAY ROLES SELECCIONADOS, ASIGNAR SIN_ROL AUTOMÁTICAMENTE
            if (rolesSeleccionados.isEmpty()) {
                rolesSeleccionados.add(TipoRol.SIN_ROL);
                System.out.println("🔍 DEBUG: No se seleccionaron roles, asignando SIN_ROL por defecto");
            }

            if (personaInicial == null) {
                // Crear nueva persona
                Persona nuevaPersona = personaServicio.validarEInsertar(dni, nombre, apellido, telefono,
                        correoElectronico);
                nuevaPersona.setUnaListaRoles(rolesSeleccionados);
                nuevasPersonas.add(nuevaPersona);

                Alerta.mostrarExito("Persona creada exitosamente");
                System.out.println("🔍 DEBUG: Nueva persona creada: " + nuevaPersona);

                // Limpiar formulario después de crear
                nuevo(null);

            } else {
                // Modificar persona existente
                personaServicio.validarYModificar(personaInicial, nombre, apellido, telefono, correoElectronico);

                // Actualizar roles de la persona
                personaInicial.setUnaListaRoles(rolesSeleccionados);
                System.out.println("🔍 DEBUG: Persona modificada: " + personaInicial);

                nuevasPersonas.add(personaInicial);

                Alerta.mostrarExito("Persona modificada exitosamente");
                StageManager.cerrarModal(Vista.FormularioPersona); // Cerrar el modal después de guardar
            }

        } catch (Exception e) {
            Alerta.mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void nuevo(ActionEvent event) {
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        chkCmbRol.getCheckModel().clearChecks(); // Limpiar selección de roles
    }

    // metodo para inicializar el controlador
    public void setPersonaInicial(Persona personaInicial) {
        this.personaInicial = personaInicial;
        if (personaInicial != null) {
            autocompletarCampos();
            txtDni.setDisable(true); // Deshabilitar campo DNI si es edición
            btnNuevo.setDisable(true); // Deshabilitar botón "Nuevo" si es edición
        }
    }

    public List<Persona> getPersonas() {
        return nuevasPersonas;

    }

    private void autocompletarCampos() {
        if (personaInicial != null) {
            txtDni.setText(personaInicial.getDni());
            txtNombre.setText(personaInicial.getNombre());
            txtApellido.setText(personaInicial.getApellido());
            txtTelefono.setText(personaInicial.getTelefono() != null ? personaInicial.getTelefono() : "");
            txtCorreo.setText(
                    personaInicial.getCorreoElectronico() != null ? personaInicial.getCorreoElectronico() : "");

            // ✅ CONFIGURAR ROLES EN EL CHECKCOMBOBOX (excluyendo SIN_ROL)
            chkCmbRol.getCheckModel().clearChecks();
            List<TipoRol> rolesPersona = personaInicial.getUnaListaRoles();
            if (rolesPersona != null && !rolesPersona.isEmpty()) {
                for (TipoRol rol : rolesPersona) {
                    // Solo marcar roles que NO sean SIN_ROL
                    if (rol != TipoRol.SIN_ROL) {
                        chkCmbRol.getCheckModel().check(rol);
                    }
                }
                // Si la persona solo tiene SIN_ROL, no marcar nada (queda vacío = SIN_ROL
                // implícito)
            }
        }
    }

}
