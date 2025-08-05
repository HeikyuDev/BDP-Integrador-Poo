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

public class ListaPersonasControlador implements Initializable {

    // Columnas de la tabla
    @FXML
    private TableColumn<Persona, String> colDni;
    @FXML
    private TableColumn<Persona, String> colEmail;
    @FXML
    private TableColumn<Persona, String> colNombreCompleto;
    @FXML
    private TableColumn<Persona, String> colRol;
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
    private final ObservableList<Persona> personasOriginales = FXCollections.observableArrayList(); // contiene todas
                                                                                                    // las personas
    private final ObservableList<Persona> personasFiltradas = FXCollections.observableArrayList(); // contiene las
                                                                                                   // personas filtradas
                                                                                                   // por los criterios
                                                                                                   // de búsqueda
    private final ObservableList<TipoRol> roles = FXCollections.observableArrayList();

    private PersonaServicio servicio;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        inicializarTabla();
        inicializarFiltros();
        System.out.println("✅ ListaPersonasControlador inicializado correctamente");

    }

    private void inicializarTabla() {
        try {
            // ✅ debug 1: VERIFICAR QUE EL REPOSITORIO EXISTE
            if (RepositorioContext.getRepositorio() == null) {
                throw new RuntimeException(
                        "El repositorio no está inicializado. Verifique que la aplicación se haya iniciado correctamente.");
            }

            System.out.println("✅ Repositorio disponible, creando PersonaServicio...");
            servicio = new PersonaServicio(RepositorioContext.getRepositorio());

            // ✅ PROBAR LA CONEXIÓN ANTES DE CONTINUAR
            RepositorioContext.getRepositorio().probarConexion();
            System.out.println("✅ Conexión a base de datos verificada");

            // Configurar las columnas de la tabla
            colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
            colNombreCompleto.setCellValueFactory(cell -> {
                Persona p = cell.getValue();
                return javafx.beans.binding.Bindings.createStringBinding(() -> p.getApellido() + ", " + p.getNombre());
            });
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));

            // ✅ CORREGIR COLUMNA DE ROLES - Persona no tiene getRol()
            colRol.setCellValueFactory(cell -> {
                Persona p = cell.getValue();
                return javafx.beans.binding.Bindings.createStringBinding(() -> {
                    List<TipoRol> roles = p.getUnaListaRoles();
                    if (roles == null || roles.isEmpty()) {
                        return "Sin rol";
                    }
                    return roles.stream()
                            .map(TipoRol::getDescripcion)
                            .collect(java.util.stream.Collectors.joining(", "));
                });
            });

            personasOriginales.clear();
            personasFiltradas.clear();

            // ✅ AQUÍ SE PROBARÁ REALMENTE LA CONEXIÓN
            // Cargar todas las personas al inicio
            System.out.println("📊 Cargando personas desde la base de datos...");
            List<Persona> todasLasPersonas = servicio.buscarTodos();
            System.out.println("✅ Se cargaron " + todasLasPersonas.size() + " personas");

            personasOriginales.addAll(servicio.buscarTodos());
            personasFiltradas.setAll(personasOriginales);
            tblPersonas.setItems(personasFiltradas);

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar tabla: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Inicialización");
            alert.setHeaderText("No se pudo cargar la tabla de personas");
            alert.setContentText("Error: " + e.getMessage() +
                    "\n\nPosibles causas:\n" +
                    "• La base de datos no está conectada\n" +
                    "• El repositorio no se inicializó correctamente\n" +
                    "• Las tablas no existen en la base de datos");
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
        System.out.println("🔍 DEBUG: Botón agregar presionado");

        try {
            // ✅ VERIFICAR REPOSITORIO ANTES DE CONTINUAR
            if (RepositorioContext.getRepositorio() == null) {
                throw new RuntimeException("El repositorio no está disponible");
            }

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
                System.out.println("✅ Personas agregadas correctamente");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al agregar persona: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo agregar la persona");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
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
        personasOriginales.stream() // .stream() convierte la lista original en un Stream para aplicar operaciones funcionales
                .filter(p -> (txtDni.getText().isEmpty() || p.getDni().contains(txtDni.getText())) &&
                        (txtNombreCompleto.getText().isEmpty() || (p.getNombre() + " " + p.getApellido()).toLowerCase()
                                .contains(txtNombreCompleto.getText().toLowerCase()))
                        &&
                        (cmbRol.getValue() == null || p.getUnaListaRoles().contains(cmbRol.getValue())))
                .forEach(personasFiltradas::add); // cada persona que cumple los filtros se agrega a la lista filtrada
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
        System.out.println("🔍 DEBUG: Modificar persona seleccionada: " + personaSeleccionada);

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
                LoggerFactory.getLogger(ListaPersonasControlador.class)
                        .error("Error al abrir el formulario de modificación", e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay persona seleccionada");
            alert.setContentText("Por favor, seleccione una persona para modificar.");
            alert.showAndWait();
        }
    }

    private List<Persona> abrirFormulario(Persona personaSeleccionada) throws IOException {
        // Cargamos el controlador y el Parent (vista)
        Pair<FormularioPersonaControlador, Parent> formulario =
            StageManager.cargarVistaConControlador(Vista.FormularioPersona.getRutaFxml());

        // Le pasamos la persona seleccionada al formulario (modo edición)
        formulario.getKey().cargarPersona(personaSeleccionada);

        // Abrimos el modal pasando directamente el Parent ya cargado (misma instancia)
        StageManager.abrirModal(Vista.FormularioPersona, formulario.getValue());
        
        // Retornamos las personas procesadas en el formulario
        return formulario.getKey().getPersonas();
    }   

    
}

