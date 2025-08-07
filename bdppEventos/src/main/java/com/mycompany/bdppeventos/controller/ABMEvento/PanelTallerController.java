package com.mycompany.bdppeventos.controller.ABMEvento;


import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.model.interfaces.PanelEvento;
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

public class PanelTallerController implements Initializable, PanelEvento<Taller> {
        
    @FXML
    private RadioButton rbtnPresencial, rbtnVirtual;

    // ToggleGroup para que solo se pueda seleccionar una modalidad
    private final ToggleGroup rBtnGroup = new ToggleGroup();
    
    @FXML
    private ComboBox<Persona> cmbInstructor;
    
    // Servicios
    private PersonaServicio personaServicio;                
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos el servicios de Persona
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        // Configuramos los Radios Buttons
        configurarToggleGroup();       
        // Actualziamos La Combo
        actualizarCombo();
    }
    
        
    private void configurarToggleGroup() {
        // Agregamos los radio Buttons al ToggleGroup
        rbtnPresencial.setToggleGroup(rBtnGroup);
        rbtnVirtual.setToggleGroup(rBtnGroup);
        // Dejamos seteados por defecto una modalidad
        rbtnPresencial.setSelected(true);
        // previene que el grupo de radio buttons se quede sin selección
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

    public Persona getInstructor() {
        if (cmbInstructor == null || cmbInstructor.getSelectionModel() == null) {
            throw new IllegalStateException("ComboBox no inicializado correctamente");
        }

        Persona unInstructor = cmbInstructor.getSelectionModel().getSelectedItem();
        if (unInstructor == null) {
            throw new IllegalArgumentException("Debe seleccionar un Instructor");
        }
        return unInstructor;
    }

    
            
    private ObservableList<Persona> obtenerInstructores()
    {
        List<Persona> lista = personaServicio.obtenerPersonasPorRol(TipoRol.INSTRUCTOR);
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
    
    // METODOS DE LA INTERFAZ PanelEvento<T>
    
    @Override
    public void limpiarCampos()
    {
        cmbInstructor.getSelectionModel().clearSelection();
    }
    
    @Override
    public void validar() {
        StringBuilder errores = new StringBuilder();

        if (cmbInstructor.getSelectionModel().getSelectedItem() == null) {
            errores.append("• Debe seleccionar un instructor.\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }
    
    @Override
    public void cargarDatos(Taller taller) {
        // Determinamos si es Virtual o no
        if(taller == null)
        {
            throw new NullPointerException("Error: No se reconoce el taller");
        }        
        
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
    
}
