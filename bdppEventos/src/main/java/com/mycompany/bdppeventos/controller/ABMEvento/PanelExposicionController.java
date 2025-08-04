package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.services.TipoDeArte.TipoDeArteServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PanelExposicionController implements Initializable {

    @FXML
    private ComboBox<TipoDeArte> cmbTipoArte;
    
    @FXML
    private ComboBox<Persona> cmbCurador;
    

    /**
     * Botón para agregar un nuevo tipo de arte a la exposición.
     */
    @FXML
    private Button btnAgregarTipoArte;
    
    
    
    
    //Servicios
    private TipoDeArteServicio tipoDeArteServicio;
    private PersonaServicio personaServicio;

    /**
     * Inicializa el panel de exposición al cargar la vista.
     * Aquí se puede agregar lógica de inicialización si es necesario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializamos el tipoDeArteServicio
        tipoDeArteServicio = new TipoDeArteServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        //Vinculamos la Observable List con la combo        
        // Actualizamos la Combo
        actualizarCombos();
    }

    // Metodos de Controles

    /**
     * Acción asociada al botón para agregar un tipo de arte.
     * Actualmente solo imprime un mensaje por consola.
     */
    @FXML
    private void agregarTipoArte() {
        // Limpiamos los campos
        limpiarCampos();
        // Abrimos el Modal que nos permite hacer el ABM de Tipos de Arte
        StageManager.abrirModal(Vista.FormularioTipoDeArte);
        // Actualizamos la ComboBox
        actualizarCombos();
    }

    // Metodos específicos
    
    protected void limpiarCampos()
    {
        cmbTipoArte.getSelectionModel().clearSelection();
        cmbCurador.getSelectionModel().clearSelection();
    }
           

    private void actualizarCombos() {
        try {
            // Actualizar tipos de arte
            actualizarComboTipoArte();
            // Actualizar curadores
            actualizarComboCuradores();
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar combos: " + e.getMessage());
        }
    }

    public TipoDeArte getTipoArteSeleccionado() {
        TipoDeArte tipo = cmbTipoArte.getSelectionModel().getSelectedItem();
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de arte");
        }
        return tipo;
    }
     
    public Persona getCurador() {
        if (cmbCurador == null || cmbCurador.getSelectionModel() == null) {
            throw new IllegalStateException("ComboBox no inicializado correctamente");
        }
        
        Persona unCurador = cmbCurador.getSelectionModel().getSelectedItem();
        if (unCurador == null) {
            throw new IllegalArgumentException("Debe seleccionar un curador");
        }
        return unCurador;
    }

    public void cargarDatos(Exposicion exposicion) {
        // Obtengo el Tipo de Arte de la exposicion
        cmbTipoArte.setValue(exposicion.getUnTipoArte());
        // Obtengo el unico curador presente en la Exposicion
        cmbCurador.setValue(exposicion.getCurador());
    }

    private void actualizarComboCuradores() {
        try {            
            List<Persona> curadores = personaServicio.obtenerPersonasPorRol(TipoRol.CURADOR);
            cmbCurador.getItems().setAll(curadores);

        } catch (Exception e) {
            System.err.println("Error al actualizar curadores: " + e.getMessage());
            Alerta.mostrarError("No se pudieron cargar los curadores: " + e.getMessage());
        }
    }
    
    private void actualizarComboTipoArte()
    {
        try {
            List<TipoDeArte> tiposDeArte = tipoDeArteServicio.buscarTodos();
            cmbTipoArte.getItems().setAll(tiposDeArte);
        } catch (Exception e) {
            System.err.println("Error al actualizar los Tipo de Arte: " + e.getMessage());
            Alerta.mostrarError("No se pudieron cargar los Tipos de Arte: " + e.getMessage());
        }
    }
    

}
