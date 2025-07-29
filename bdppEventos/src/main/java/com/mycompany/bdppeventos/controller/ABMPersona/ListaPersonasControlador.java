package com.mycompany.bdppeventos.controller.ABMPersona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.SearchableComboBox;
import org.slf4j.LoggerFactory;

import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

public class ListaPersonasControlador implements Initializable{

    // Columnas de la tabla
    @FXML
    private TableColumn<Persona, String> colDni;
    @FXML
    private TableColumn<Persona, String> colEmail;
    @FXML
    private TableColumn<Persona, String> colNombreCompleto;
    @FXML
    private TableColumn<Persona, TipoRol> colRol;
    @FXML
    private TableColumn<Persona, String> colTelefono;

    @FXML
    private TableView<Persona> tblPersonas;

    // filtros
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private SearchableComboBox<TipoRol> cmbRol;

    // botones
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnModificar;

    // listas que vamos a usar para la tabla
    private final ObservableList<Persona> personasOriginales = FXCollections.observableArrayList(); // contiene todas las personas
    private final ObservableList<Persona> personasFiltradas = FXCollections.observableArrayList();  // contiene las personas filtradas por los criterios de búsqueda
    private final ObservableList<TipoRol> roles = FXCollections.observableArrayList();

    private PersonaServicio servicio;

    private Pair<FormularioPersonaControlador, Parent> formulario;
    

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        inicializarTabla();
        inicializarFiltros();
        System.out.println("✅ ListaPersonasControlador inicializado correctamente");

    }

     private void inicializarTabla() {
        try {
            servicio = new PersonaServicio(RepositorioContext.getRepositorio()); 
            
            colDni.setCellValueFactory(new PropertyValueFactory<>("dni")); 
            colNombreCompleto.setCellValueFactory(cell -> {
                Persona p = cell.getValue();
                return javafx.beans.binding.Bindings.createStringBinding(() ->
                        p.getApellido() + ", " + p.getNombre());
            });
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
            colRol.setCellValueFactory(new PropertyValueFactory<>("rol")); // Asegúrate que exista
            
            personasOriginales.clear();
            personasFiltradas.clear();

            // Cargar todas las personas al inicio
            personasOriginales.addAll(servicio.buscarTodos());
            personasFiltradas.setAll(personasOriginales);
            tblPersonas.setItems(personasFiltradas);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar la tabla");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

     private void inicializarFiltros() {
        roles.clear();
        // Asignar los roles disponibles al combo box
        roles.setAll(TipoRol.values());
        cmbRol.setItems(roles);
    }

    @FXML
    void agregar() {
        try {
            List<Persona> nuevasPersonas = abrirFormulario(null);
            System.out.println("🔍 DEBUG 1: Iniciando abrirFormulario");
            
            if (nuevasPersonas != null && !nuevasPersonas.isEmpty()) {
                for (Persona nueva : nuevasPersonas) {
                    if (!personasOriginales.contains(nueva)) {
                        personasOriginales.add(nueva);
                        personasFiltradas.add(nueva);
                    }
                }
                tblPersonas.refresh();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(ListaPersonasControlador.class).error("Error al agregar persona", e);
        }
    }

    @FXML
    void eliminar() {
        Persona personaSeleccionada = tblPersonas.getSelectionModel().getSelectedItem();

        if (personaSeleccionada != null) {
            try {
                servicio.validarYBorrar(personaSeleccionada);
                personasOriginales.remove(personaSeleccionada);
                personasFiltradas.remove(personaSeleccionada);
                tblPersonas.refresh();
            } catch (Exception e) {
                LoggerFactory.getLogger(ListaPersonasControlador.class).error("Error al eliminar persona", e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo eliminar la persona");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay persona seleccionada");
            alert.setContentText("Por favor, seleccione una persona para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    void filtrar() {
        personasFiltradas.clear();
        personasOriginales.stream()
            .filter(p -> (txtDni.getText().isEmpty() || p.getDni().contains(txtDni.getText())) &&
                         (txtNombreCompleto.getText().isEmpty() || (p.getNombre() + " " + p.getApellido()).toLowerCase().contains(txtNombreCompleto.getText().toLowerCase())) &&
                         (cmbRol.getValue() == null || p.getUnaListaRoles().contains(cmbRol.getValue())))
            .forEach(personasFiltradas::add);
            tblPersonas.refresh();
    }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        txtDni.clear();
        txtNombreCompleto.clear();
        cmbRol.setValue(null);
        filtrar();
    }

    @FXML
    void modificar(ActionEvent event) {
        Persona personaSeleccionada = tblPersonas.getSelectionModel().getSelectedItem();

        if (personaSeleccionada != null) {
            try {
                List<Persona> personasModificadas = abrirFormulario(personaSeleccionada);
                if (personasModificadas != null && !personasModificadas.isEmpty()) {
                    for (Persona modificada : personasModificadas) {
                        int index = personasOriginales.indexOf(personaSeleccionada);
                        if (index >= 0) {
                            personasOriginales.set(index, modificada);
                            personasFiltradas.set(index, modificada);
                        }
                    }
                    tblPersonas.refresh();
                }
            } catch (IOException e) {
                LoggerFactory.getLogger(ListaPersonasControlador.class).error("Error al abrir el formulario de modificación", e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay persona seleccionada");
            alert.setContentText("Por favor, seleccione una persona para modificar.");
            alert.showAndWait();
        }
    }

    private List<Persona> abrirFormulario(Persona personaInicial) throws IOException {
        formulario = StageManager.cargarVistaConControlador(Vista.FormularioPersona.getRutaFxml());
        System.out.println("🔍 DEBUG 2: Formulario cargado correctamente");

        FormularioPersonaControlador controladorFormulario = formulario.getKey();
        System.out.println("🔍 DEBUG 3: Controlador del formulario obtenido");
        //Parent vistaFormulario = formulario.getValue();

        if (personaInicial != null) {
            controladorFormulario.setPersonaInicial(personaInicial);
            System.out.println("🔍 DEBUG 4: Persona inicial establecida en el formulario");
        }

        System.out.println("🔍 DEBUG 5: Abriendo modal del formulario");
        StageManager.abrirModal(Vista.FormularioPersona);
        System.out.println("🔍 DEBUG 6: Modal del formulario abierto");
        System.out.println("🔍 DEBUG 7: Esperando a que se cierre el modal");
        return controladorFormulario.getPersonas();
    }


}
