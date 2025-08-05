package com.mycompany.bdppeventos.controller.ABMTipoDeArte;

import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.services.TipoDeArte.TipoDeArteServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioTipoDeArteController implements Initializable {

    @FXML
    private TextField txtNombre;

    @FXML
    private TableView<TipoDeArte> tblTipoDeArte;
    @FXML
    private TableColumn<TipoDeArte, Integer> colId;

    @FXML
    private TableColumn<TipoDeArte, String> colNombre;

    @FXML
    private Button btnAltaTipoDeArte, btnBaja, btnModificacion, btnCancelar;

    // Servicios
    private TipoDeArteServicio tipoDeArteServicio;

    // Lista Observable para la tabla de Tipos de Arte
    private ObservableList<TipoDeArte> listaTiposDeArte = FXCollections.observableArrayList();

    // Variable de Edicion
    private TipoDeArte tipoDeArteEnEdicion = null;

    // Constante que tiene el valor del Texto del boton alta
    private final String altaTxt = "Alta de Tipo de arte";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos el TipoDeArte Servicios
        tipoDeArteServicio = new TipoDeArteServicio(RepositorioContext.getRepositorio());
        //Vinculamos la lista observable con la tabla de Tipos de Arte
        tblTipoDeArte.setItems(listaTiposDeArte);
        //Configuramos la tabla y las columnas
        configurarTablaYColumnas();
        // Actualizamos la tabla para mostrar Los tipos de Arte Actualizados
        actualizarTabla();
    }

    // Metodos de Controles
    @FXML
    private void altaTipoDeArte() {
        try {

            // Validamos los datos 
            String nombre = txtNombre.getText().trim();
            validarCampos(nombre);

            if (tipoDeArteEnEdicion == null) {
                if (!Alerta.confirmarAccion(
                        "¿Guardar el Tipo de Arte '" + nombre + "'?")) {
                    return;
                }
                tipoDeArteServicio.altaTipoDeArte(nombre);                
                Alerta.mostrarExito("Tipo de Arte registrado exitosamente"); 
                // Limpiamos todos los campos
                limpiarCampos();
                // Actualizamos la tabla, para que impacten la modificacion
                actualizarTabla();
            } else {
                if (!Alerta.confirmarAccion("¿Modificar el Tipo de Arte'" + tipoDeArteEnEdicion.getNombre() + "'?")) {
                    return;
                }
                tipoDeArteServicio.modificarTipoDeArte(tipoDeArteEnEdicion, nombre);                
                Alerta.mostrarExito("Tipo De Arte modificado exitosamente");
                // Asignamos a tipoDeArteEnEdicion el valor null, volviendo al estado inicial
                tipoDeArteEnEdicion = null;
                // Limpiamos todos los campos
                limpiarCampos();
                // Actualizamos la tabla, para que impacten la modificacion
                actualizarTabla();
                // Configuramos la igu para que vuelva al estado base
                ConfiguracionIgu.configuracionBase(btnAltaTipoDeArte, btnModificacion, btnBaja, btnCancelar, altaTxt);
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError(e.getMessage());            
        } catch (Exception e) {
            Alerta.mostrarError("Error al guardar: " + e.getMessage());            
        } 
    }

    @FXML
    private void bajaTipoDeArte() {
        TipoDeArte tipoDeArteSeleccionado = tblTipoDeArte.getSelectionModel().getSelectedItem();
        if (tipoDeArteSeleccionado == null) {
            Alerta.mostrarError("Debe seleccionar un Tipo de Arte  para eliminar.");
            return;
        }
        boolean confirmar = Alerta.confirmarAccion(
                "¿Está seguro que desea eliminar el Tipo de Arte '" + tipoDeArteSeleccionado.getNombre() + "'?");
        if (!confirmar) {
            return; // usuario canceló
        }

        try {
            tipoDeArteServicio.bajaTipoDeArte(tipoDeArteSeleccionado.getIdTipoArte());
            Alerta.mostrarExito("Tipo de Arte eliminado correctamente.");
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            Alerta.mostrarError("Error al eliminar el tipo de Arte: " + e.getMessage());
        }
    }

    @FXML
    private void modificacionTipoDeArte() {
        // Obtenemos el objeto seleccionado
        TipoDeArte tipoDeArteSeleccionado = tblTipoDeArte.getSelectionModel().getSelectedItem();
        // Validamos que no sea nulo
        if (tipoDeArteSeleccionado == null) {
            Alerta.mostrarError("Debe seleccionar un Tipo de Arte para modificar.");
            return; // Si es nulo sale inmediatamente del metodo e informa al usuario
        }
        ConfiguracionIgu.configuracionBtnModificar(btnAltaTipoDeArte, btnModificacion, btnBaja, btnCancelar);
        // Cargar datos en los campos
        txtNombre.setText(tipoDeArteSeleccionado.getNombre());
        // Guardar la película en edición
        tipoDeArteEnEdicion = tipoDeArteSeleccionado;
    }

    @FXML
    private void cancelarEdicion() {
        tipoDeArteEnEdicion = null;
        limpiarCampos();
        ConfiguracionIgu.configuracionBtnCancelar(btnAltaTipoDeArte, btnModificacion, btnBaja, btnCancelar, altaTxt);
    }

    // ==Metodos Especificos==
    private void limpiarCampos() {
        // Limpiamos el texto que esta en el TextField
        txtNombre.clear();
    }

    private void validarCampos(String nombre) {
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }

    private void actualizarTabla() {
        try {
            // Obtenemos todos los registros de la base de datos y la almacenamos en un a lista
            List<TipoDeArte> lista = tipoDeArteServicio.buscarTodos();
            // Limpiamos los elementos de la lista Observable
            listaTiposDeArte.clear();
            // Agregamos todos los registros de la base de datos a la tabla
            listaTiposDeArte.addAll(FXCollections.observableArrayList(lista));
            // Actualizamos la Tabla
            tblTipoDeArte.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado:\n" + e.getMessage());
        }
    }
    
    private void configurarTablaYColumnas()
    {
        configurarTabla();
        configurarColumnas();
    }

    private void configurarColumnas() {
        // Definimos como cada columna representa los datos ("Lo que muestra") DEFINIMOS EL QUÉ Y EL CÓMO
        colId.setCellValueFactory(new PropertyValueFactory<TipoDeArte, Integer>("idTipoArte"));
        colNombre.setCellValueFactory(new PropertyValueFactory<TipoDeArte, String>("nombre"));
    }

    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblTipoDeArte.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurar texto para tablas vacías
        tblTipoDeArte.setPlaceholder(new Label("No Hay Tipos de Arte para mostrar"));

        // Simula proporciones para tblEvento 
        colId.setMaxWidth(1f * Integer.MAX_VALUE * 40); 
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 60);                         
    }
}
