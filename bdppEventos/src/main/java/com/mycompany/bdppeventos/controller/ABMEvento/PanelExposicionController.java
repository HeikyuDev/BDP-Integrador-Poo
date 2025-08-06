package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.TipoDeArte;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.model.interfaces.PanelEvento;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.services.TipoDeArte.TipoDeArteServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class PanelExposicionController implements Initializable, PanelEvento<Exposicion> {

    @FXML
    private ComboBox<TipoDeArte> cmbTipoArte;
    
    @FXML
    private ComboBox<Persona> cmbCurador;
                        
    //Servicios
    private TipoDeArteServicio tipoDeArteServicio;
    private PersonaServicio personaServicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializamos el tipoDeArteServicio
        tipoDeArteServicio = new TipoDeArteServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());                
        // Actualizamos la Combo
        actualizarCombos();
    }
   
    @FXML
    private void agregarTipoArte() {        
        // Abrimos el Modal que nos permite hacer el ABM de Tipos de Arte
        StageManager.abrirModal(Vista.FormularioTipoDeArte);
        // Actualizamos la ComboBox
        actualizarComboTipoArte();
    }

    // Metodos específicos
                   

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
    
    // METODOS DE LA INTERFAZ PanelEvento<T>
    
    @Override
    public void limpiarCampos()
    {
        cmbTipoArte.getSelectionModel().clearSelection();
        cmbCurador.getSelectionModel().clearSelection();
    }
    
    @Override
    public void cargarDatos(Exposicion exposicion) {
        // Validamos si la exposision es nula
        if (exposicion == null) {
            throw new NullPointerException("La exposición no puede ser nula");
        }
        // Obtengo el Tipo de Arte de la exposicion
        cmbTipoArte.setValue(exposicion.getUnTipoArte());
        // Obtengo el unico curador presente en la Exposicion
        cmbCurador.setValue(exposicion.getCurador());
    }
    
    @Override
    public void validar() {
    StringBuilder errores = new StringBuilder();

    if (cmbTipoArte.getSelectionModel().getSelectedItem() == null) {
        errores.append("• Debe seleccionar un tipo de arte.\n");
    }

    if (cmbCurador.getSelectionModel().getSelectedItem() == null) {
        errores.append("• Debe seleccionar un curador.\n");
    }

    if (errores.length() > 0) {
        throw new IllegalArgumentException(errores.toString());
    }
}
}
