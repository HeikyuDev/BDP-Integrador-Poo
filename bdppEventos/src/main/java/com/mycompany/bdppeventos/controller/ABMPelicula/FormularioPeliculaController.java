package com.mycompany.bdppeventos.controller.ABMPelicula;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.Pelicula;
import com.mycompany.bdppeventos.services.Pelicula.PeliculaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;

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

public class FormularioPeliculaController  implements Initializable {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDuracion;

    @FXML
    private Button btnAltaPelicula, btnModificacion, btnBaja, btnCancelar;

    @FXML
    private TableView<Pelicula> tblPelicula;
    @FXML
    private TableColumn<Pelicula, Integer> colId;
    @FXML
    private TableColumn<Pelicula, String> colNombre;
    @FXML
    private TableColumn<Pelicula, Double> colDuracion;

    // Servicio de Pelicula
    private PeliculaServicio peliculaServicio;

    // Lista Observable de la tabla de Películas
    private ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    // Variable de modificacion
    private Pelicula peliculaEnEdicion = null;

    // Constante que tiene el valor del Texto del boton alta
    private final String altaTxt = "Alta de Película";
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instanciamos la Pelicula servicios con el repositorio
        peliculaServicio = new PeliculaServicio(RepositorioContext.getRepositorio());
        // Configuramos la ObservableList
        tblPelicula.setItems(listaPeliculas);
        // Configuramos la tabla
        configurarTablayColumna();
        // Llamamos al metodo que obtiene todas las instancias
        actualizarTabla();
    }
        
    
    // Metodos OnActioin

    @FXML
    private void altaPelicula() {
        try {
            // Obtenemos los datos de la pelicula
            String titulo = txtNombre.getText().trim();
            String duracionTexto = txtDuracion.getText().trim();
            // Validamos que no sean vacios ni nulos
            validarCampos(titulo, duracionTexto);
            // Parseamos de String a Double
            double duracion = Double.parseDouble(duracionTexto);
            
            // Si la peliculaEnEdicion es null significa que es ALTA
            if (peliculaEnEdicion == null) {
                if (!Alerta.confirmarAccion(
                        "¿Guardar la película '" + titulo + "' con duración de " + duracion + " horas?")) {
                    return; 
                }
                // Llamamos al servicio de peliculas para que cree el objeto y lo persista
                peliculaServicio.altaPelicula(titulo, duracion);
                limpiarCampos();
                Alerta.mostrarExito("Película registrada exitosamente");
            } else {
                if (!Alerta.confirmarAccion("¿Modificar la película '" + peliculaEnEdicion.getTitulo() + "'?")) {
                    return;
                }
                peliculaServicio.modificarPelicula(peliculaEnEdicion, titulo, duracion);
                limpiarCampos();
                Alerta.mostrarExito("Película modificada exitosamente");
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError(e.getMessage());            
        } catch (Exception e) {
            Alerta.mostrarError("Error al guardar: " + e.getMessage());            
        } finally {
            // Este bloque se ejecuta SIEMPRE (haya o no error)
            peliculaEnEdicion = null;
            actualizarTabla();
            ConfiguracionIgu.configuracionFinaly(btnAltaPelicula, btnModificacion, btnBaja, btnCancelar, altaTxt);
        }
    }

    @FXML
    private void bajaPelicula() {
        Pelicula peliculaSeleccionada = tblPelicula.getSelectionModel().getSelectedItem();
        if (peliculaSeleccionada == null) {
            Alerta.mostrarError("Debe seleccionar una película para eliminar.");
            return;
        }

        boolean confirmar = Alerta.confirmarAccion(
                "¿Está seguro que desea eliminar la película '" + peliculaSeleccionada.getTitulo() + "'?");
        if (!confirmar) {
            return; // usuario canceló
        }

        try {
            peliculaServicio.bajaPelicula(peliculaSeleccionada.getIdPelicula());
            Alerta.mostrarExito("Película eliminada correctamente.");
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            Alerta.mostrarError("Error al eliminar película: " + e.getMessage());
        }
    }

    @FXML
    private void modificacionPelicula() {
        // Obtenemos el objeto seleccionado
        Pelicula peliculaSeleccionada = tblPelicula.getSelectionModel().getSelectedItem();
        // Validamos que no sea nulo
        if (peliculaSeleccionada == null) {
            Alerta.mostrarError("Debe seleccionar una película para modificar.");
            return; // Si es nulo sale inmediatamente del metodo e informa al usuario
        }        
        ConfiguracionIgu.configuracionBtnModificar(btnAltaPelicula, btnModificacion, btnBaja, btnCancelar);        
        // Cargar datos en los campos
        txtNombre.setText(peliculaSeleccionada.getTitulo());
        txtDuracion.setText(String.valueOf(peliculaSeleccionada.getDuracion()));
        // Guardar la película en edición
        peliculaEnEdicion = peliculaSeleccionada;
    }

    @FXML
    private void cancelarEdicion() {
        peliculaEnEdicion = null;
        limpiarCampos();
        ConfiguracionIgu.configuracionBtnCancelar(btnAltaPelicula, btnModificacion, btnBaja, btnCancelar, altaTxt);        
    }

    // Metodos Especificos
    
    private void validarCampos(String nombre, String duracionTexto) {
        if (nombre.isEmpty() || duracionTexto.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }            
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtDuracion.clear();
    }

    private void configurarTablayColumna()
    {
        configuracionColumnas();
        configurarTabla();
    }
    
    private void configuracionColumnas() {
        // Definimos como cada columna obtiene los datos
        colId.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("idPelicula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("titulo"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<Pelicula, Double>("duracion"));
    }
    
    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblPelicula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        

        // Configurar texto para tablas vacías
        tblPelicula.setPlaceholder(new Label("No hay Peliculas para mostrar"));        

        // Simula proporciones para tblEvento 
        colId.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40%                
        colDuracion.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%        
    }

     // Metodo Que obtiene todas las peliculas. 
    

    private void actualizarTabla() {
        try {
            // Obtenemos todos los registros de la base de datos y la almacenamos en un a lista
            List<Pelicula> lista = peliculaServicio.buscarTodos();            
            // Limpiamos los elementos de la tabla
            listaPeliculas.clear();
            // Convertimos la lista a una lista Observable y le asignamos a la variable Observable
            // Que esta vinculado a la tabla
            listaPeliculas.setAll(FXCollections.observableArrayList(lista));                        
            // Actaulzamos la tabla
            tblPelicula.refresh();                        
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado:\n" + e.getMessage());
        }
    }

}
