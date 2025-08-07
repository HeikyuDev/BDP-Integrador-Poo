package com.mycompany.bdppeventos.controller.PanelAdministracion;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class PanelAdministracionController implements Initializable {

    // DECLARACION DE ETIQUETAS
    @FXML
    private Label lblNumeroTotalEventos,lblNumeroEventosConfirmados,lblNumeroEventosPlanificados,lblNumeroEventosEnEjecucion,lblNumeroEventosFinalizados,
            lblNumeroEventosCancelados,lblNumeroTotalPersonas,lblNumeroOrganizadores,lblNumeroCuradores,lblNumeroInstructores,lblNumeroArtistas,lblNumeroParticipantes;
            
    // Servicios
    private EventoServicio eventoServicio;
    private PersonaServicio personaServicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los Servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        // Llamamos al metodo que actualiza las labels
        actualizarValores();
    }    
    
    // Metodo para actualizar las labels
    private void actualizarValores()
    {        
        // Actualizamos el Texto de la seccion de Eventos
        lblNumeroTotalEventos.setText(obtenerNumerosEventos(null));
        lblNumeroEventosConfirmados.setText(obtenerNumerosEventos(EstadoEvento.CONFIRMADO));
        lblNumeroEventosPlanificados.setText(obtenerNumerosEventos(EstadoEvento.PLANIFICADO));
        lblNumeroEventosEnEjecucion.setText(obtenerNumerosEventos(EstadoEvento.EN_EJECUCION));
        lblNumeroEventosFinalizados.setText(obtenerNumerosEventos(EstadoEvento.FINALIZADO));
        lblNumeroEventosCancelados.setText(obtenerNumerosEventos(EstadoEvento.CANCELADO));
        // Actualizamos el Texto de la seccion de Personas
        lblNumeroTotalPersonas.setText(obtenerNumerosPersonas(null));
        lblNumeroOrganizadores.setText(obtenerNumerosPersonas(TipoRol.ORGANIZADOR));
        lblNumeroCuradores.setText(obtenerNumerosPersonas(TipoRol.CURADOR));
        lblNumeroInstructores.setText(obtenerNumerosPersonas(TipoRol.INSTRUCTOR));
        lblNumeroArtistas.setText(obtenerNumerosPersonas(TipoRol.ARTISTA));
        lblNumeroParticipantes.setText(obtenerNumerosPersonas(TipoRol.PARTICIPANTE));
    }
                
    
    // Metodos Específicos    
    private String obtenerNumerosEventos(EstadoEvento unEstado) {
        try {
            List<Evento> listaFiltrada = new ArrayList<>();
            if (unEstado == null) {
                // Obtenemos todos los Eventos activos de la BD
                listaFiltrada = eventoServicio.buscarTodos();
                // Retornamos la cantidad de registros existentes en la bd
                return String.valueOf(listaFiltrada.size());
            } else // Si el estado pasado como parametro es distinto de null
            {                
                List<EstadoEvento> estado = Arrays.asList(unEstado);
                listaFiltrada = eventoServicio.obtenerEventosPorEstado(estado, false);
                return String.valueOf(listaFiltrada.size());
            }
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
            return "0";
        }
    }
    
    private String obtenerNumerosPersonas(TipoRol unTipoRol) {
        try {
            List<Persona> listaFiltrada = new ArrayList<>();
            if (unTipoRol == null) {
                // Obtenemos todas las personas activas de la BD
                listaFiltrada = personaServicio.buscarTodos();
                // Retornamos la cantidad de registros existentes en la bd
                return String.valueOf(listaFiltrada.size());
            } else // Si el Tipo de Rol pasado como parametro es distinto de null
            {
                listaFiltrada = personaServicio.obtenerPersonasPorRol(unTipoRol);
                return String.valueOf(listaFiltrada.size());
            }
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado: " + e.getMessage());
            return "0";
        }
    }                   
}