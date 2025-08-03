package com.mycompany.bdppeventos.controller.InscribirParticipantes;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
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
import javafx.scene.layout.HBox;
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
            List<Evento> todosLosEventos = eventoServicio.obtenerEventosConfirmadosInscribibles();
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
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 20%
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40%
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 20%
        colCupo.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 20%        
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
            Alerta.mostrarError("Ocurrio un Error Inesperado: " + e.getMessage());
        }
    }
    
    private List<Evento> obtenerListaPorTipo(TipoEvento unTipoEvento) {
        // Si es null, devolver todos los eventos
        if (unTipoEvento == null) {
            return eventoServicio.obtenerEventosConfirmadosInscribibles();
        }

        List<Evento> listaEventoFiltrada = new ArrayList<>();
        switch (unTipoEvento) {
            case EXPOSICION -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Exposicion) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case TALLER -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Taller) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CONCIERTO -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Concierto) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case CICLO_DE_CINE -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof CicloDeCine) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
            case FERIA -> {
                for (Evento unEvento : eventoServicio.obtenerEventosConfirmadosInscribibles()) {
                    if (unEvento instanceof Feria) {
                        listaEventoFiltrada.add(unEvento);
                    }
                }
            }
        }
        return listaEventoFiltrada;
    }

    private void estadoInicial()
    {
        // Desabulito el layout de ingreso de datos
        vboxDatos.setDisable(true);
        // Desabilito el btn de Inscripcion
        btnInscribir.setDisable(true);
    }
    
    private void limpiarCampos()
    {
        txtDNIParticipante.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        
        estadoInicial();
    }
    
    @FXML
    private void verificarExistencia()
    {
        try {            
        // Obtenemos el DNI del Participante ingresado en el TextField DNI
        String dniIngresado = txtDNIParticipante.getText().trim();
        // Validamos si es valido el DNI ingresado
        if(dniIngresado == null || dniIngresado.isEmpty())
        {
            throw new IllegalArgumentException("ErrOr: Por favor ingrese un DNI VALIDO");
        }
        
        
        // Utilizando el PersonaServicio validamos si ya existe una entidad con Dicho dni
        if(personaServicio.buscarPorId(dniIngresado) == null)
        {
            // Si el DNI ingresado en el TextField no corresponde a una persona registrada en el sistema Habilita el campo de ingreso de datos
            vboxDatos.setDisable(false);
            Alerta.mostrarExito("El DNI ingresado no Corresponde a ninguna persona Registrada!!!");
            existeRegistro = false;
        }
        else
        {
            // Si el el DNI ingresado en el TextField Corresponde a una persona registrada (Curador / Artista /,....)            
            Alerta.mostrarExito("El DNI ingresado  Corresponde a una persona Registrada!!!");
            existeRegistro = true;                                    
        }
        // Habilitamos el BTn de Inscripcion 
        btnInscribir.setDisable(false);
        
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrio un Error inesperado " + e.getMessage());
            estadoInicial();
        }
    }
    
    @FXML
    private void inscribirParticipante()
    {
        try {
            // Obtenemos el Evento seleccionado
            Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();

            // Validamos que haya un evento seleccionado
            if (eventoSeleccionado == null) {
                Alerta.mostrarError("Debe seleccionar un evento para Inscribir al participante");
                return; // Sale inmediatamente del metodo
            }
            
            // Si la Proyeccion en Edicion es NULL significa que es Alta
                if (!Alerta.confirmarAccion(
                        "¿Inscribir el Participante'" + txtDNIParticipante.getText().trim() + "'")) {
                    //Si el usuario preciona la opcion cancelar "Sale inmediatamente del metodo" 
                    return;
                }
            // 1. Si la persona Existe
            if(existeRegistro == true) // Valido si ya existe un participante con ese DNI
            {
                // Obtenemos la Persona Existente.
                Persona unaPersona = personaServicio.buscarPorId(txtDNIParticipante.getText().trim());                                
                // 1.0 Validamos: Si la persona ya esta inscripta. 
                if(participacionServicio.existeParticipacion(eventoSeleccionado, unaPersona, TipoRol.PARTICIPANTE))
                {
                    throw new Exception("la persona: " + unaPersona.getInformacionPersonal() + "\nYa se encuentra inscripta en el evento");
                }
                // 1.1 Validamos: Si la persona Tiene el Rol de PARTICIPANTE
                else if(unaPersona.getUnaListaRoles().contains(TipoRol.PARTICIPANTE))
                {
                    // Utilizamos el Evento Servicio para modificar las participaciones Asociadas al evento
                    // Agregando esta neuva participacion con el rol de Asistente/Participante
                    eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);    
                    Alerta.mostrarExito("Inscripcion Realizada Correctamente");
                }
                else 
                {
                    // 1.2 La persona existe pero no tiene el Rol de Participante
                    personaServicio.asignarRol(unaPersona, TipoRol.PARTICIPANTE);
                    // Utilizamos el Evento Servicio para modificar las participaciones Asociadas al evento
                    // Agregando esta neuva participacion con el rol de Asistente/Participante
                    eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);  
                    Alerta.mostrarExito("Inscripcion Realizada Correctamente");
                }
            }
            else // Significa que no existe una persona con ese DNI "Por lo tanto se"
                // habilita los campos para el ingreso y Creacion de una nueva persona"
            {
                // 2. La persona no existe, y se tiene que crear de 0 un Registro de persona asociando esa persona al rol de PARTICIPANTE
                
                // 2.1 Obtenemos los datos de los respectivos Controles y Validamos
                String Dni = txtDNIParticipante.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String correo = txtCorreo.getText().trim();
                
                List<TipoRol> listaRol = new ArrayList<>();
                listaRol.add(TipoRol.PARTICIPANTE);
                
                validarCamposObligatorios(Dni, nombre, apellido);
                
                // 2.2 Hacemos alta de la persona
                Persona unaPersona = personaServicio.validarEInsertar(Dni, nombre, apellido, telefono, correo, listaRol);
                eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);
                Alerta.mostrarExito("Inscripcion Realizada Correctamente");
                
            }
            
        } catch (IllegalArgumentException e) {
            Alerta.mostrarError("Error: Asegurese de Ingresar Datos Validos\nEvite Campos Obligatorios Vacios\n" + e.getMessage());
            limpiarCampos();
        }
        catch (Exception e)
        {
            Alerta.mostrarError("Error: Ocurrio un Error inesperado\n" + e.getMessage());
            limpiarCampos();
        }
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

}
