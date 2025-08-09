package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import org.controlsfx.control.CheckComboBox;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;

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
    private Text lblTitulo;

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
        configurarCheckComboBox();
        personaInicial = null;
        nuevasPersonas.clear();
  
    }

    private void configurarCheckComboBox() {
        chkCmbRol.setTitle("Seleccione roles");
        // ✅ NO seleccionar nada por defecto (SIN_ROL se asigna automáticamente si no hay selección)
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
                        correoElectronico, rolesSeleccionados);
                nuevasPersonas.add(nuevaPersona);

                Alerta.mostrarExito("Persona creada exitosamente");
                System.out.println("🔍 DEBUG: Nueva persona creada: " + nuevaPersona);

                // Limpiar formulario después de crear
                nuevo(null);

            } else {
                // Modificar persona existente
                personaServicio.validarYModificar(personaInicial, nombre, apellido, telefono, correoElectronico, rolesSeleccionados);

                // ✅ Ya no es necesario setear roles aquí porque validarYModificar ya los maneja
                System.out.println("🔍 DEBUG: Persona modificada: " + personaInicial);

                nuevasPersonas.add(personaInicial);

                Alerta.mostrarExito("Persona modificada exitosamente");
                
                // ✅ CERRAR MODAL AUTOMÁTICAMENTE DESPUÉS DE MODIFICAR (mejora UX)
                cerrarModal();
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

    // utilizado para obtener la lista de personas creadas o modificadas
    public List<Persona> getPersonas() {
        return nuevasPersonas;
    }

    public void cargarPersona(Persona persona) {
        this.personaInicial = persona;
        
        if (persona != null) {
            // Cargamos los datos en los campos del formulario
            txtNombre.setText(persona.getNombre());
            txtApellido.setText(persona.getApellido());
            txtDni.setText(String.valueOf(persona.getDni()));
            txtCorreo.setText(persona.getCorreoElectronico() != null ? persona.getCorreoElectronico() : "");
            txtTelefono.setText(persona.getTelefono() != null ? persona.getTelefono() : "");
            
            // Cargar roles en el CheckComboBox
            chkCmbRol.getCheckModel().clearChecks();
            List<TipoRol> rolesPersona = persona.getUnaListaRoles();
            if (rolesPersona != null && !rolesPersona.isEmpty()) {
                for (TipoRol rol : rolesPersona) {
                    // Solo marcar roles que NO sean SIN_ROL
                    if (rol != TipoRol.SIN_ROL) {
                        chkCmbRol.getCheckModel().check(rol);
                    }
                }
            }
            
            // Deshabilitar campo DNI y botón "Nuevo" si es edición
            txtDni.setDisable(true);
            btnNuevo.setDisable(true);
            lblTitulo.setText("Editar Persona");

        } else {
            // Limpiar formulario para nueva persona
            nuevo(null);
            txtDni.setDisable(false);
            btnNuevo.setDisable(false);
        }
    }

    /**
     * Cierra el modal automáticamente para mejorar la experiencia de usuario.
     * Se ejecuta después de modificar una persona exitosamente.
     */
    private void cerrarModal() {
        try {
            // Obtener el Stage desde cualquier control del formulario
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            
            if (stage != null) {
                stage.close();
                System.out.println("✅ Modal cerrado automáticamente después de modificar persona");
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cerrar el modal automáticamente: " + e.getMessage());
            // No es crítico si no se puede cerrar, el usuario puede cerrarlo manualmente
            // Esto puede pasar si el modal se está ejecutando en un contexto especial
        }
    }
}
