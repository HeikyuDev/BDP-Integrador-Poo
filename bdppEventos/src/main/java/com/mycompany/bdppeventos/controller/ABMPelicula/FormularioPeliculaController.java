package com.mycompany.bdppeventos.controller.ABMPelicula;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.Evento;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioPeliculaController extends ConfiguracionIgu implements Initializable {

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
        // Configuramos las columnas
        configuracionColumnas();
        // Llamamos al metodo que obtiene todas las instancias
        actualizarTabla();
    }

    // Metodos OnActioin

    @FXML
    private void altaPelicula() {
        try {
            String titulo = txtNombre.getText().trim();
            String duracionTexto = txtDuracion.getText().trim();
            validarCampos(titulo, duracionTexto);
            double duracion = Double.parseDouble(duracionTexto);

            if (peliculaEnEdicion == null) {
                if (!Alerta.confirmarAccion(
                        "¿Guardar la película '" + titulo + "' con duración de " + duracion + " horas?")) {
                    return;
                }
                peliculaServicio.altaPelicula(titulo, duracion);
                Alerta.mostrarExito("Película registrada exitosamente");
            } else {
                if (!Alerta.confirmarAccion("¿Modificar la película '" + peliculaEnEdicion.getTitulo() + "'?")) {
                    return;
                }                
                peliculaServicio.modificarPelicula(peliculaEnEdicion, titulo, duracion);
                Alerta.mostrarExito("Película modificada exitosamente");
            }
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError(e.getMessage());
            return;
        } catch (Exception e) {
            Alerta.mostrarError("Error al guardar: " + e.getMessage());
            return;
        } finally {
            // Este bloque se ejecuta SIEMPRE (haya o no error)
            peliculaEnEdicion = null;
            limpiarCampos();
            actualizarTabla();
            configuracionFinaly(btnAltaPelicula,btnModificacion,btnBaja,btnCancelar,altaTxt);
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
        configuracionBtnModificar(btnAltaPelicula, btnModificacion, btnBaja, btnCancelar);        
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
        configuracionBtnCancelar(btnAltaPelicula, btnModificacion, btnBaja, btnCancelar, altaTxt);        
    }

    // Métodos auxiliares
    private void validarCampos(String nombre, String duracionTexto) {
        if (nombre.isEmpty() || duracionTexto.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }
    
    
    // Metodos Especificos
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtDuracion.clear();
    }

    private void configuracionColumnas() {
        // Definimos como cada columna obtiene los datos
        colId.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("idPelicula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("titulo"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<Pelicula, Double>("duracion"));
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
            listaPeliculas.addAll(FXCollections.observableArrayList(lista));                        
            // Actaulzamos la tabla
            tblPelicula.refresh();                        
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado:\n" + e.getMessage());
        }
    }

}
