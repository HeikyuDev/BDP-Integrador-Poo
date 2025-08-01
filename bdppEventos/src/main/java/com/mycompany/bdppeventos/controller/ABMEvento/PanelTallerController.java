
/**
 * Controlador del panel de taller para el ABM de eventos.
 * Gestiona la selección del tipo de modalidad (presencial o virtual) y la configuración
 * de los controles relacionados con el cupo del taller.
 */
package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PanelTallerController implements Initializable {

    // Controles

    
    @FXML
    private RadioButton rbtnPresencial;

   
    @FXML
    private RadioButton rbtnVirtual;
    
   
    @FXML
    private ComboBox<Persona> cmbInstructor;
    
    // Servicios
    private PersonaServicio personaServicio;
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        // Configuramos los Radios Buttons
        configurarToggleGroup();       
        // Actualziamos La Combo
        actualizarCombo();
    }
    

    /**
     * Grupo de botones para asegurar que solo se seleccione una modalidad a la vez.
     */
    private final ToggleGroup rBtnGroup = new ToggleGroup();

    /**
     * Configura el ToggleGroup para los RadioButton de modalidad.
     * Si se deselecciona la opción actual, vuelve a seleccionarla para evitar que
     * ambas queden desmarcadas.
     */
    private void configurarToggleGroup() {
        rbtnPresencial.setToggleGroup(rBtnGroup);
        rbtnVirtual.setToggleGroup(rBtnGroup);
        rbtnPresencial.setSelected(true);
        rBtnGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });
    }

    // Metodos Específicos

    public boolean getEsPrecencial() {
        return rbtnPresencial.isSelected();                 
    }

    Persona getInstructor() {
        if (cmbInstructor == null || cmbInstructor.getSelectionModel() == null) {
            throw new IllegalStateException("ComboBox no inicializado correctamente");
        }

        Persona unInstructor = cmbInstructor.getSelectionModel().getSelectedItem();
        if (unInstructor == null) {
            throw new IllegalArgumentException("Debe seleccionar un curador");
        }
        return unInstructor;
    }

    void cargarDatos(Taller taller) {
        // Determinamos si es Virtual o no
        if (taller.isEsPresencial())
        {
            rbtnPresencial.setSelected(true);
            rbtnVirtual.setSelected(false);
        }
        else
        {
            rbtnPresencial.setSelected(false);
            rbtnVirtual.setSelected(true);
        }
        // Metodo que Obtiene la persona que ejerce el rol de Instructor en el Evento
        cmbInstructor.setValue(taller.getInstructor());        
    }
            
    private ObservableList<Persona> obtenerInstructores()
    {
        List<Persona> lista = personaServicio.buscarInstructores();
        // 2. Verificamos si la lista es nula
        if (lista != null) {
            // 3. Si no es nula, la convertimos a ObservableList
            return FXCollections.observableArrayList(lista);
        } else {
            // 4. Si es nula, devolvemos una ObservableList vacía
            return FXCollections.observableArrayList();
        }
    }       

    private void actualizarCombo() {
        try {
            // Borramos los elementos del CheckCombo
            cmbInstructor.getItems().clear();
            // Cargamos el Combo con todas las proyecciones Activas
            cmbInstructor.getItems().setAll(obtenerInstructores());
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar proyecciones:" + e.getMessage());
        }
    }
    
    protected void limpiarCampos()
    {
        cmbInstructor.getSelectionModel().clearSelection();
    }
}
