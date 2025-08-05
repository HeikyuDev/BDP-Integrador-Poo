package com.mycompany.bdppeventos.controller.ABMProyeccion;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.model.entities.Proyeccion;
import com.mycompany.bdppeventos.services.Pelicula.PeliculaServicio;
import com.mycompany.bdppeventos.services.proyeccion.ProyeccionServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import java.net.URL;
import java.util.ArrayList;
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
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckModel;

public class FormularioProyeccionController implements Initializable {

    @FXML
    private CheckComboBox<Pelicula> chkComboPeliculas;

    @FXML
    private TextField txtNombre;

    // Elementos para la seccion de "Datos Cargados"
    @FXML
    private TableView<Proyeccion> tblProyeccion;
    @FXML
    private TableColumn<Proyeccion, Integer> colId;
    @FXML
    private TableColumn<Proyeccion, String> colNombre;
    @FXML
    private TableColumn<Proyeccion, String> colPeliculas;

    // Lista Observable para la Tabla
    private ObservableList<Proyeccion> listaProyecciones = FXCollections.observableArrayList();

    // Lista Observable de "Peliculas" Para la Combo
    private ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    // Botones
    @FXML
    private Button btnAltaProyeccion, btnBaja, btnModificacion, btnCancelar, btnAgregarPelicula;

    // Variable de modificacion
    private Proyeccion ProyeccionEnEdicion = null;

    // Constante que tiene el valor del Texto del boton alta
    private final String altaTxt = "Alta de Proyeccion";
    // Servicios
    private ProyeccionServicio proyeccionServicio;
    private PeliculaServicio peliculaServicio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los services de "Pelicula" y "Proyeccion"
        proyeccionServicio = new ProyeccionServicio(RepositorioContext.getRepositorio());
        peliculaServicio = new PeliculaServicio(RepositorioContext.getRepositorio());
        // Vinculamos las Observable list con las tablas/Combos
        tblProyeccion.setItems(listaProyecciones);
        chkComboPeliculas.getItems().addAll(listaPeliculas);
        // Metodo Que configure la tabla y las columnas
        configurarTablaYColumnas();
        // Metodos que configuren el Combo y las Tables "Actualizar Combo" - "Actualizar Tabla"
        actualizarCheckCombo();
        actualizarTabla();

    }

    // Metodos de los Elementos Graficos
    @FXML
    private void agregarPelicula() {
        // Vaciamos los campos 
        limpiarCampos();
        // Abrimos nuevo modal que nos permiten dar de alta / Modificar / O dar de baja una pelicula        
        StageManager.abrirModal(Vista.FormularioPelicula);
        // Actualizamos el combo para registrar todas las posibles peliculas
        actualizarCheckCombo();
        actualizarTabla();
    }

    @FXML
    private void altaProyeccion() {
        try {
            // Obtengo el nombre de la proyeccion de la interfaz
            String nombre = txtNombre.getText().trim();
            // Obtengo la lista Observable de peliculas seleciconadas por el usuario por el CheckComboBox
            ObservableList<Pelicula> listaObservableSeleccionados = FXCollections.observableArrayList();
            listaObservableSeleccionados = chkComboPeliculas.getCheckModel().getCheckedItems(); // Me devuelve solo las instancias que seleccione del CheckCombo
            // Creo una ArrayList para que el tipo de Lista coincida con el del modelo "Proyeccion declara la lista de pelicula como List<Pelicula>"
            List<Pelicula> listaSeleccionados = new ArrayList<>(listaObservableSeleccionados);

            // Validamos los campos
            validarCampos(nombre, listaSeleccionados);  // Si falla retorna una excepcion          

            if (ProyeccionEnEdicion == null) {
                // Si la Proyeccion en Edicion es NULL significa que es Alta
                if (!Alerta.confirmarAccion(
                        "¿Guardar la proyeccion'" + nombre + "'")) {
                    //Si el usuario preciona la opcion cancelar "Sale inmediatamente del metodo" 
                    return;
                }
                // LLamamos al metodo de Proyeccion Servicio "altaProyeccion" Para persistir la informacion
                proyeccionServicio.altaProyeccion(nombre, listaSeleccionados);
                Alerta.mostrarExito("Proyeccion registrada exitosamente");
                // Limpio los campos
                limpiarCampos();
                // Actualizamos la tabla
                actualizarTabla();
            } else {
                // Si la Proyeccion en Edicion es distinto de null significa que es Modificacion
                if (!Alerta.confirmarAccion("¿Modificar la Proyeccion '" + ProyeccionEnEdicion.getNombre() + "'?")) {
                    return;
                }
                proyeccionServicio.modificarProyeccion(ProyeccionEnEdicion, nombre, listaSeleccionados);
                Alerta.mostrarExito("Proyeccion modificada exitosamente");
                // Asigno a la proyeccion en edicion el valor de null indicando que se va a realizar unn alta posteriormente
                ProyeccionEnEdicion = null;
                // Limpio los campos
                limpiarCampos();
                // Actualizo la tabla para que impacten los cambios
                actualizarTabla();
                // Configuros los iconos como estaban por defecto
                ConfiguracionIgu.configuracionBase(btnAltaProyeccion, btnModificacion, btnBaja, btnCancelar, altaTxt);
                btnAgregarPelicula.setDisable(false);
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError(e.getMessage());
        } catch (Exception e) {
            Alerta.mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void bajaProyeccion() {
        Proyeccion proyeccionSeleccionada = tblProyeccion.getSelectionModel().getSelectedItem();
        // Obtengo la "Proyeccion selecciondada de la TableView"
        if (proyeccionSeleccionada == null) {
            // Si no selecciono nada muestra una alerta y sale del metodo
            Alerta.mostrarError("Debe seleccionar una proyeccion para eliminar.");
            return;
        }

        boolean confirmar = Alerta.confirmarAccion(
                "¿Está seguro que desea eliminar la Proyeccion '" + proyeccionSeleccionada.getNombre() + "'?");
        if (!confirmar) {
            return; // usuario canceló
        }

        try {
            // Pasamos el Id de la Proyeccion a "Proyeccion Servicio" el cual tiene la responsabilidad de eliminar la instancia
            proyeccionServicio.bajaProyeccion(proyeccionSeleccionada.getIdProyeccion());
            Alerta.mostrarExito("proyeccion eliminada correctamente.");
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            Alerta.mostrarError("Error al eliminar la proyeccion: " + e.getMessage());
        }
    }

    @FXML
    private void modificacionProyeccion() {
        //  Obtenemos y validamos La proyeccion Seleccionada
        Proyeccion proyeccionSeleccionada = tblProyeccion.getSelectionModel().getSelectedItem();
        if (proyeccionSeleccionada == null) {
            Alerta.mostrarError("Debe seleccionar una proyección para modificar.");
            return;
        }

        //  Configuramos la interfaz
        ConfiguracionIgu.configuracionBtnModificar(btnAltaProyeccion, btnModificacion, btnBaja, btnCancelar);
        txtNombre.setText(proyeccionSeleccionada.getNombre());

        // Obtenemos las peliculas seleccionadas
        CheckModel<Pelicula> checkModel = chkComboPeliculas.getCheckModel();
        checkModel.clearChecks(); // Limpiar selecciones previas

        if (proyeccionSeleccionada.getUnaListaPelicula() != null) {
            for (Pelicula pelicula : proyeccionSeleccionada.getUnaListaPelicula()) {
                // Verificar que la película exista en la lista del ComboBox
                if (chkComboPeliculas.getItems().contains(pelicula)) {
                    checkModel.check(pelicula);
                }
            }
        }

        // 4. Guardar referencia para edición
        ProyeccionEnEdicion = proyeccionSeleccionada;
    }

    @FXML
    private void cancelarEdicion() {
        ProyeccionEnEdicion = null;
        limpiarCampos();
        ConfiguracionIgu.configuracionBtnCancelar(btnAltaProyeccion, btnModificacion, btnBaja, btnCancelar, altaTxt);
    }

    //===Metodos Especificos ===// 
    // Metodo que limpia los campos
    private void limpiarCampos() {
        txtNombre.clear();
        // Limpiar CheckComboBox (desmarcar todos los ítems seleccionados)
        chkComboPeliculas.getCheckModel().clearChecks();
    }

    private void configurarTablaYColumnas() {
        configurarTabla();
        configuracionColumnas();
    }

    // Metodo que ajusta las dimensiones de la tabla y los comportamientos
    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblProyeccion.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurar texto para tablas vacías
        tblProyeccion.setPlaceholder(new Label("No hay Proyecciones para mostrar"));

        // Simula proporciones para tblEvento 
        colId.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20%
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 40%                
        colPeliculas.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 30%        
    }

    // Metodo que configura Cada columna COMO Y QUE va a mostrar
    private void configuracionColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<Proyeccion, Integer>("idProyeccion")); // 
        colNombre.setCellValueFactory(new PropertyValueFactory<Proyeccion, String>("nombre"));
        colPeliculas.setCellValueFactory(cellData -> ConfiguracionIgu.formatLista(cellData.getValue().getUnaListaPelicula(), Pelicula::getTitulo, "Sin Peliculas"));
        //Le paso la lista de pelicula asociada a al objeto de cada fila, Le paso COMO quiero que muestre esa pelicula como String(De cada pelicula quiero que muestres el titulo)
        //,Y por ultimo le paso El mensaje que va a mostrar cuando un objeto no tenga asociado una lista o su listaes vacia
    }

    private void validarCampos(String nombre, List<Pelicula> peliculas) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío o ser nulo");
        }

        if (peliculas == null || peliculas.isEmpty()) {
            throw new IllegalArgumentException("La lista de películas no puede ser nula o vacía");
        }

        // Validación adicional para asegurarte que hay selecciones reales
        CheckModel<Pelicula> checkModel = chkComboPeliculas.getCheckModel();
        if (checkModel.getCheckedItems().isEmpty()) {
            throw new IllegalArgumentException("No hay películas seleccionadas");
        }
    }

    // Metodo Que actualiza al CheckComboBox
    private void actualizarCheckCombo() {
        try {
            listaPeliculas.clear();
            listaPeliculas.addAll(FXCollections.observableArrayList(peliculaServicio.buscarTodos()));
            ConfiguracionIgu.configuracionListaEnCheckCombo(chkComboPeliculas, listaPeliculas);
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado:\n" + e.getMessage());
        }
    }

    // Metodo Que actualiza la Tabla de la seccion "Datos Cargados"
    private void actualizarTabla() {
        try {
            // Obtener las proyecciones del servicio
            List<Proyeccion> proyecciones = proyeccionServicio.buscarTodos();
            // Limpio los elementos anteriorses de la lista Observable            
            if (proyecciones == null || proyecciones.isEmpty()) {
                // Si la proyeccion son vacias o nulas devuelvo uan lista vacia
                listaProyecciones.setAll(FXCollections.observableArrayList());
            } else {
                // Sino
                // Agregar todas las proyecciones a la lista observable
                listaProyecciones.setAll(FXCollections.observableArrayList(proyecciones));
            }
            // Forzar la actualización de la tabla
            tblProyeccion.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un error al actualizar la tabla:\n" + e.getMessage());
        }
    }

}
