package com.mycompany.bdppeventos.controller.ABMEvento;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Taller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class VisualizacionController implements Initializable {
    
    // Labels para Exposición
    @FXML private Label tipoArteLabel;
    @FXML private Label curadorLabel;
    
    // Labels para Taller
    @FXML private Label modalidadLabel;
    @FXML private Label instructorLabel;
    
    // Labels para Concierto
    @FXML private Label tipoEntradaLabel;
    @FXML private Label montoLabel;
    @FXML private Label artistaLabel;
    
    // Labels para Ciclo de Cine
    @FXML private Label proyeccionLabel;
    @FXML private Label charlasPosterioresLabel;
    
    // Labels para Feria
    @FXML private Label cantidadStandsLabel;
    @FXML private Label tipoCoberturaLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización vacía
    }
    
    public void mostrarDatos(Evento evento) {
        if (evento instanceof Exposicion) {
            mostrarExposicion((Exposicion) evento);
        } else if (evento instanceof Taller) {
            mostrarTaller((Taller) evento);
        } else if (evento instanceof Concierto) {
            mostrarConcierto((Concierto) evento);
        } else if (evento instanceof CicloDeCine) {
            mostrarCicloDeCine((CicloDeCine) evento);
        } else if (evento instanceof Feria) {
            mostrarFeria((Feria) evento);
        }
    }
    
    private void mostrarExposicion(Exposicion exposicion) {
        if (tipoArteLabel != null && exposicion.getUnTipoArte() != null) {
            tipoArteLabel.setText(exposicion.getUnTipoArte().getNombre());
        }
        if (curadorLabel != null && exposicion.getCurador() != null) {
            curadorLabel.setText(exposicion.getCurador().getInformacionPersonal());
        }
    }
    
    private void mostrarTaller(Taller taller) {
        if (modalidadLabel != null) {
            modalidadLabel.setText(taller.getModalidadTexto());
        }
        if (instructorLabel != null) {
            instructorLabel.setText(taller.getInstructor().getInformacionPersonal());
        }
    }
    
    private void mostrarConcierto(Concierto concierto) {
        if (tipoEntradaLabel != null) {
            tipoEntradaLabel.setText(concierto.getModalidadTexto());
        }
        if (montoLabel != null) {
            montoLabel.setText("$" + concierto.getMonto());
        }
        if (artistaLabel != null) {
            artistaLabel.setText(concierto.getInformacionPersonalArtistas());
        }
    }
    
    private void mostrarCicloDeCine(CicloDeCine cicloCine) {
        if (proyeccionLabel != null) {
            proyeccionLabel.setText(cicloCine.getUnaProyeccion().getNombre());
        }
        if (charlasPosterioresLabel != null) {
            charlasPosterioresLabel.setText(cicloCine.getCharlasPosterioresTexto());
        }
    }
    
    private void mostrarFeria(Feria feria) {
        if (cantidadStandsLabel != null) {
            cantidadStandsLabel.setText(String.valueOf(feria.getCantidadStands()));
        }
        if (tipoCoberturaLabel != null) {
            tipoCoberturaLabel.setText(feria.getTipoCobertura().getDescripcion());
        }
    }
}