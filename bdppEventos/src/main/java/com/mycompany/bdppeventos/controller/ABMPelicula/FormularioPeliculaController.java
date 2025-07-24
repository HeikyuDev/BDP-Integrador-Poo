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
            btnAltaPelicula.setText("Alta de Película");
            btnModificacion.setDisable(false);
            btnBaja.setDisable(false);
            btnCancelar.setDisable(true);
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
        Pelicula peliculaSeleccionada = tblPelicula.getSelectionModel().getSelectedItem();
        if (peliculaSeleccionada == null) {
            Alerta.mostrarError("Debe seleccionar una película para modificar.");
            return;
        }
        // Configuraciones de Elementos Graficos
        btnAltaPelicula.setText("Guardar Cambios"); // Cambiamos el texto del boton a "Guardar Cambios"
        btnModificacion.setDisable(true); // Desabilitamos el btn de modificacion para que el usuario no pueda volver a
                                          // precionarlo
        btnBaja.setDisable(true);
        btnCancelar.setDisable(false);
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
        btnAltaPelicula.setText("Alta de Película");
        btnModificacion.setDisable(false);
        btnBaja.setDisable(false);
        btnCancelar.setDisable(true); // Opcional: deshabilitar botón cancelar al salir edición
    }

    // Métodos auxiliares
    private void validarCampos(String nombre, String duracionTexto) {
        if (nombre.isEmpty() || duracionTexto.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }

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

    private void actualizarTabla() {
        try {
            // Limpiar la lista antes de agregar nuevos elementos
            listaPeliculas.clear();

            // Obtener todos los eventos del servicio
            List<Pelicula> peliculas = peliculaServicio.buscarTodos();

            // Verificar que la lista no sea null
            if (peliculas != null) {
                listaPeliculas.addAll(peliculas);
            } else {
                Alerta.mostrarError("No se pudieron cargar los eventos");
            }
        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
