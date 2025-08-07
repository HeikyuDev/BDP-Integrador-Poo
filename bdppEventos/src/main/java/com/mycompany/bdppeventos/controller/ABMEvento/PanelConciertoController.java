package com.mycompany.bdppeventos.controller.ABMEvento;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.CheckComboBox;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.model.interfaces.PanelEvento;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class PanelConciertoController  implements Initializable, PanelEvento<Concierto> {

    @FXML
    private CheckBox chkEsPago;

    @FXML
    private TextField txtEsPago;

    @FXML
    private CheckComboBox<Persona> chkComboArtistas;

    // Servicios
    private PersonaServicio personaServicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Inicializamos la Persona Servicio
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        // Actualizamos la combo de Artistas
        actualizarCombo();
    }

    @FXML
    private void configurarEsPago() {
        // Configuracion que permite escritura en Textfield si se selecciona el checkBox
        ConfiguracionIgu.configuracionCheckTextfield(chkEsPago, txtEsPago);
    }

    // Metodos específicos

    public boolean getEsPago() {
        return chkEsPago.isSelected();
    }

    public double getMonto() {
        if (chkEsPago.isSelected()) {
            String texto = txtEsPago.getText().trim();
            if (texto.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un monto si el evento es pago.");
            }
            try {
                double monto = Double.parseDouble(texto);
                if (monto <= 0) {
                    throw new IllegalArgumentException("El monto debe ser mayor que cero.");
                }
                return monto;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El monto ingresado no es válido. Debe ser un número.");
            }
        } else {
            throw new IllegalArgumentException("No se puede especificar el monto del evento si el mismo es gratuito.");
        }
    }

    public List<Persona> getArtistas() {
        List<Persona> artistas; 
        if (chkComboArtistas == null || chkComboArtistas.getCheckModel() == null) {
            throw new IllegalStateException("CheckComboBox no inicializado correctamente");
        }

        ObservableList<Persona> listaObservableArtistas = chkComboArtistas.getCheckModel().getCheckedItems();

        if (listaObservableArtistas == null || listaObservableArtistas.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un artista");
        }
        artistas = new ArrayList<>(listaObservableArtistas);
        return  artistas;
    }
       
    private ObservableList<Persona> obtenerArtistas() {
        List<Persona> lista = personaServicio.obtenerPersonasPorRol(TipoRol.ARTISTA);
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
            chkComboArtistas.getItems().clear();
            // Cargamos el Combo con todas las proyecciones Activas
            chkComboArtistas.getItems().setAll(obtenerArtistas());
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar proyecciones:" + e.getMessage());
        }
    }

    // METODOS DE LA INTERFAZ PanelEvento<>
    
    @Override
    public void limpiarCampos() {
        chkComboArtistas.getCheckModel().clearChecks();
        txtEsPago.clear();
        chkEsPago.setSelected(false);
    }
    
    @Override
    public void validar() {
        StringBuilder errores = new StringBuilder();

        // Validar artistas
        List<Persona> artistas = chkComboArtistas.getCheckModel().getCheckedItems();
        if (artistas == null || artistas.isEmpty()) {
            errores.append("• Debe seleccionar al menos un artista.\n");
        }

        // Validar monto si es pago
        if (chkEsPago.isSelected()) {
            String textoMonto = txtEsPago.getText().trim();
            if (textoMonto.isEmpty()) {
                errores.append("• Debe ingresar un monto si el evento es pago.\n");
            } else {
                try {
                    double monto = Double.parseDouble(textoMonto);
                    if (monto <= 0) {
                        errores.append("• El monto debe ser mayor que cero.\n");
                    }
                } catch (NumberFormatException e) {
                    errores.append("• El monto ingresado no es válido. Debe ser un número.\n");
                }
            }
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    
    @Override
    public void cargarDatos(Concierto concierto) {
        chkEsPago.setSelected(concierto.isEsPago());

        if (concierto.isEsPago()) {
            txtEsPago.setDisable(false);
            txtEsPago.setText(String.valueOf(concierto.getMonto()));
        } else {
            txtEsPago.setText("");
        }

        // Rellenamos la lista de Artistas. Colocando los seleccionados
        List<Persona> artistas = concierto.getArtistas();
        chkComboArtistas.getCheckModel().clearChecks();
        for (Persona artista : artistas) {
            chkComboArtistas.getCheckModel().check(artista);
        }

    }

}
