package com.mycompany.bdppeventos.controller.InscribirParticipantes;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import static com.mycompany.bdppeventos.model.enums.TipoEvento.CICLO_DE_CINE;
import static com.mycompany.bdppeventos.model.enums.TipoEvento.CONCIERTO;
import static com.mycompany.bdppeventos.model.enums.TipoEvento.EXPOSICION;
import static com.mycompany.bdppeventos.model.enums.TipoEvento.FERIA;
import static com.mycompany.bdppeventos.model.enums.TipoEvento.TALLER;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.services.Participacion.ParticipacionServicio;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class InscribirParticipantesController implements Initializable {

    // Controles
    @FXML
    private ComboBox<TipoEvento> cmbTipoEvento;
    
    @FXML
    private TextField txtDNIParticipante, txtNombre, txtApellido, txtTelefono, txtCorreo;
    
    @FXML
    private VBox vboxDatos;
    
    @FXML
    private Button btnInscribir;

    // Tablas
    @FXML
    private TableView<Evento> tblEvento;

    @FXML
    private TableColumn<Evento, String> colNombre;

    @FXML
    private TableColumn<Evento, String> colUbicacion;

    @FXML
    private TableColumn<Evento, String> colFechaInicio;
    
    @FXML 
    private TableColumn<Evento, String> colCupo;
    
    // Listas Observables Asociadas a las table view
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    
    // Servicios
    private EventoServicio eventoServicio;
    private PersonaServicio personaServicio;
    private ParticipacionServicio participacionServicio;
    
    // FLAGS
    private boolean existeRegistro;
    
    // CONSTANTE de Filtro
    private final List<EstadoEvento> ConfirmadoYEnEjecucion= Arrays.asList(EstadoEvento.CONFIRMADO, EstadoEvento.EN_EJECUCION);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los Servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        participacionServicio = new ParticipacionServicio(RepositorioContext.getRepositorio());

        // Configuramos la IGU
        configurarTabla();
        configurarColumnas();
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);

        // Asociamos las listas Observables a Las tablas
        tblEvento.setItems(listaEventos);        

        // Cargamos todos los eventos al inicializar
        cargarTodosLosEventos();
    }    
    
    private void cargarTodosLosEventos() {
        try {
            List<Evento> todosLosEventos = eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true);
            listaEventos.setAll(todosLosEventos);            
        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar los eventos: " + e.getMessage());
        }
    }

    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblEvento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        

        // Configurar texto para tablas vacías
        tblEvento.setPlaceholder(new Label("No hay eventos para mostrar"));        

        // Simula proporciones para tblEvento 
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40%
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%
        colCupo.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30%        
    }
    
    private void actualizarTabla() {
        try {
            // Si hay un filtro aplicado, mantenerlo
            TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
            
            if (tipoSeleccionado == null) {
                // Si no hay filtro, cargar todos los eventos
                cargarTodosLosEventos();
            } else {
                // Si hay filtro, aplicar el filtro actual
                List<Evento> listaPorTipo = obtenerListaPorTipo(tipoSeleccionado);
                listaEventos.setAll(listaPorTipo);
            }
            
            tblEvento.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Error al actualizar la tabla: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        // Configuraciones de las columnas de la tabla Eventos
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colFechaInicio
                .setCellValueFactory(cellData -> ConfiguracionIgu.formatFecha(cellData.getValue().getFechaInicio()));
        colCupo.setCellValueFactory(cellData -> ConfiguracionIgu.formatCupoMaximoEvent(cellData.getValue()));
    }
    
    @FXML
    private void filtrarEvento() {
        try {
            // Obtenemos el Tipo de Evento Seleccionado
            TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
            List<Evento> listaPorTipo = obtenerListaPorTipo(tipoSeleccionado);

            listaEventos.setAll(listaPorTipo);
            tblEvento.refresh();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un Error Inesperado: " + e.getMessage());
        }
    }
    
    private List<Evento> obtenerListaPorTipo(TipoEvento unTipoEvento) {
        // Si es null, devolver todos los eventos
        if (unTipoEvento == null) {
            return eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true);
        }

        List<Evento> listaEventoFiltrada = new ArrayList<>();
        switch (unTipoEvento) {
            case EXPOSICION -> {
                for (Evento unEvento : eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true)) {
                    if (unEvento instanceof Exposicion) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case TALLER -> {
                for (Evento unEvento : eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true)) {
                    if (unEvento instanceof Taller) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CONCIERTO -> {
                for (Evento unEvento : eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true)) {
                    if (unEvento instanceof Concierto) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CICLO_DE_CINE -> {
                for (Evento unEvento : eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true)) {
                    if (unEvento instanceof CicloDeCine) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case FERIA -> {
                for (Evento unEvento : eventoServicio.obtenerEventosPorEstado(ConfirmadoYEnEjecucion, true)) {
                    if (unEvento instanceof Feria) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
        }
        return listaEventoFiltrada;
    }

    private void estadoInicial() {
        // Deshabilito el layout de ingreso de datos
        vboxDatos.setDisable(true);
        // Deshabilito el btn de Inscripción
        btnInscribir.setDisable(true);
    }
    
    private void limpiarCampos() {
        txtDNIParticipante.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        
        estadoInicial();
    }
    
    @FXML
    private void verificarExistencia() {
        try {            
            // Obtenemos el DNI del Participante ingresado en el TextField DNI
            String dniIngresado = txtDNIParticipante.getText().trim();

            // Validamos si es válido el DNI ingresado
            if (dniIngresado == null || dniIngresado.isEmpty()) {
                throw new IllegalArgumentException("Error: Por favor ingrese un DNI VÁLIDO");
            }

            // Validar que contenga solo números
            if (!dniIngresado.matches("\\d+")) {
                throw new IllegalArgumentException("Error: El DNI debe contener solo números");
            }

            // Utilizando el PersonaServicio validamos si ya existe una entidad con dicho DNI
            if (personaServicio.buscarPorId(dniIngresado) == null) {
                // Si el DNI ingresado no corresponde a una persona registrada, habilita el campo de ingreso de datos
                vboxDatos.setDisable(false);
                Alerta.mostrarExito("El DNI ingresado no corresponde a ninguna persona registrada");
                existeRegistro = false;
            } else {
                // Si el DNI ingresado corresponde a una persona registrada            
                Alerta.mostrarExito("El DNI ingresado corresponde a una persona registrada");
                existeRegistro = true;                                    
            }
            // Habilitamos el botón de Inscripción 
            btnInscribir.setDisable(false);
            
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un Error inesperado: " + e.getMessage());
            estadoInicial();
        }
    }
    
    @FXML
    private void inscribirParticipante() {
        try {
            // Obtenemos el Evento seleccionado
            Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();

            // Validamos que haya un evento seleccionado
            if (eventoSeleccionado == null) {
                Alerta.mostrarError("Debe seleccionar un evento para inscribir al participante");
                return; // Sale inmediatamente del método
            }
            // Validamos que el evento tenga cupo dispible            
            validarCupoDisponible(eventoSeleccionado);
            
            // Confirmación de la acción
            if (!Alerta.confirmarAccion(
                    "¿Inscribir el Participante con el DNI: '" + txtDNIParticipante.getText().trim() + "'?")) {
                // Si el usuario presiona cancelar, sale inmediatamente del método 
                return;
            }
            
            // 1. Si la persona existe
            if (existeRegistro == true) {
                // Obtenemos la Persona Existente.
                Persona unaPersona = personaServicio.buscarPorId(txtDNIParticipante.getText().trim());                                
                
                // 1.0 Validamos: Si la persona ya está inscripta. 
                if (participacionServicio.existeParticipacion(eventoSeleccionado, unaPersona, TipoRol.PARTICIPANTE)) {
                    throw new Exception("La persona: " + unaPersona.getInformacionPersonal() + "\nYa se encuentra inscripta en el evento");
                }
                // 1.1 Validamos: Si la persona tiene el Rol de PARTICIPANTE
                else if (unaPersona.getUnaListaRoles().contains(TipoRol.PARTICIPANTE)) {
                    // Inscribir al participante
                    eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);    
                    manejarInscripcionExitosa();
                } else {
                    // 1.2 La persona existe pero no tiene el Rol de Participante
                    personaServicio.asignarRol(unaPersona, TipoRol.PARTICIPANTE);
                    // Inscribir al participante
                    eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);  
                    manejarInscripcionExitosa();
                }
            } else {
                // 2. La persona no existe, se debe crear un nuevo registro
                
                // 2.1 Obtenemos los datos de los respectivos Controles y Validamos
                String dni = txtDNIParticipante.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String correo = txtCorreo.getText().trim();
                
                // Validar campos obligatorios ANTES de proceder
                validarCamposObligatorios(dni, nombre, apellido);
                
                List<TipoRol> listaRol = new ArrayList<>();
                listaRol.add(TipoRol.PARTICIPANTE);
                
                // 2.2 Hacemos alta de la persona
                Persona unaPersona = personaServicio.validarEInsertar(dni, nombre, apellido, telefono, correo, listaRol);
                eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);
                manejarInscripcionExitosa();
            }

        } catch (IllegalArgumentException e) {
            // Error de validación de campos: mostrar error pero NO limpiar campos
            Alerta.mostrarError("Error: Asegúrese de ingresar datos válidos\nEvite campos obligatorios vacíos\n" + e.getMessage());            
        } catch (IllegalStateException e) {
            // Error de cupo o estado del evento: mostrar error pero NO limpiar campos
            Alerta.mostrarError("Error de Cupo:\n" + e.getMessage());            
        } catch (Exception e) {
            // Error inesperado del sistema: mostrar error y limpiar campos
            Alerta.mostrarError("Error: Ocurrió un error inesperado\n" + e.getMessage());
            limpiarCampos(); // Solo limpiar en errores inesperados del sistema
        }
    }

    /**
     * Maneja las acciones necesarias después de una inscripción exitosa
     */
    private void manejarInscripcionExitosa() {
        Alerta.mostrarExito("Inscripción Realizada Correctamente");
        limpiarCampos();
        actualizarTabla(); // Refrescar la tabla para mostrar cambios en cupos
    }
  
    private void validarCamposObligatorios(String DNI, String nombre, String apellido) {
        StringBuilder sb = new StringBuilder();

        // VALIDAMOS CAMPOS OBLIGATORIOS
        if (DNI == null || DNI.trim().isEmpty()) {
            sb.append("Error: DNI es un Campo OBLIGATORIO\n");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            sb.append("Error: Nombre es un Campo OBLIGATORIO\n");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            sb.append("Error: Apellido es un Campo OBLIGATORIO\n");
        }

        // Si hay errores, mostrar alerta y lanzar excepción
        if (sb.length() > 0) {
            try {
                Alerta.mostrarError(sb.toString());
            } catch (Exception e) {
                // Si falla la alerta, al menos loggear el error
                System.err.println("Error mostrando alerta: " + e.getMessage());
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    // Validación más detallada (RECOMENDADA)
    private void validarCupoDisponible(Evento evento) {
        int participantesActuales = participacionServicio.contarPersonasPorRol(evento, TipoRol.PARTICIPANTE);
        int capacidadMaxima;
        
        // Validamos si tiene cupo el evento
        if(evento.isTieneCupo())
        {
            // Si el evento tiene cupo obtengo la capacidad maxima registrada del evento
            capacidadMaxima = evento.getCapacidadMaxima();
        }
        else
        {
            // Sino le asigno el maximo valor del tipo de dato int
            capacidadMaxima = Integer.MAX_VALUE;
        }            
            

        if (participantesActuales >= capacidadMaxima) {
            throw new IllegalStateException(
                    String.format("No se puede inscribir al participante. "
                            + "El evento '%s' ya alcanzó su capacidad máxima (%d/%d participantes).",
                            evento.getNombre(), participantesActuales, capacidadMaxima)
            );
        }
    }
}