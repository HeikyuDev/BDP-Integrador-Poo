package com.mycompany.bdppeventos.controller.InscribirParticipantes;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.mycompany.bdppeventos.model.entities.CicloDeCine;
import com.mycompany.bdppeventos.model.entities.Concierto;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.entities.Exposicion;
import com.mycompany.bdppeventos.model.entities.Feria;
import com.mycompany.bdppeventos.model.entities.Persona;
import com.mycompany.bdppeventos.model.entities.Taller;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.model.enums.TipoEvento;
import com.mycompany.bdppeventos.model.enums.TipoRol;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.services.Participacion.ParticipacionServicio;
import com.mycompany.bdppeventos.services.Persona.PersonaServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.ConfiguracionIgu;
import com.mycompany.bdppeventos.util.RepositorioContext;

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
    private Button btnInscribir, btnCancelar;

    // Tablas
    @FXML
    private TableView<Evento> tblEvento;

    @FXML
    private TableColumn<Evento, String> colNombre, colUbicacion, colCupo;

    @FXML
    private TableColumn<Evento, LocalDate> colFechaInicio;

    // Listas Observables Asociadas a las table view
    private ObservableList<Evento> listaEventos = FXCollections.observableArrayList();

    // Servicios
    private EventoServicio eventoServicio;
    private PersonaServicio personaServicio;
    private ParticipacionServicio participacionServicio;

    // FLAGS
    private boolean existeRegistro;

    // CONSTANTE DE FILTRO
    private final List<EstadoEvento> EstadosHabilitados = Arrays.asList(EstadoEvento.CONFIRMADO,
            EstadoEvento.EN_EJECUCION);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarServicios();
        // Configuramos la IGU
        configuracionesIgu();
        // Asociamos las listas Observables a Las tablas
        tblEvento.setItems(listaEventos);
        // Cargamos todos los eventos al inicializar
        cargarTodosLosEventos();
    }

    private void inicializarServicios() {
        // Inicializamos los Servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        personaServicio = new PersonaServicio(RepositorioContext.getRepositorio());
        participacionServicio = new ParticipacionServicio(RepositorioContext.getRepositorio());
    }

    private void configuracionesIgu() {
        configurarTabla();
        configurarColumnas();
        ConfiguracionIgu.configuracionEnumEnCombo(cmbTipoEvento, TipoEvento.class);
    }

    private void configurarTabla() {
        // Aplica política de ajuste automático
        tblEvento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurar texto para tablas vacías
        tblEvento.setPlaceholder(new Label("No hay eventos para mostrar"));

        // Simula proporciones para tblEvento
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        colUbicacion.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        colFechaInicio.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        colCupo.setMaxWidth(1f * Integer.MAX_VALUE * 25);
    }

    private void configurarColumnas() {
        // Configuraciones de las columnas de la tabla Eventos
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colCupo.setCellValueFactory(cellData -> ConfiguracionIgu.formatCupoMaximoEvent(cellData.getValue()));

        // Configuraciones de las Celdas
        ConfiguracionIgu.configurarColumnaFecha(colFechaInicio);
    }

    // Metodo para cargar todos los eventos sin filtros
    private void cargarTodosLosEventos() {
        try {
            // Obtengo todos los eventos basandome en los estados habilitados para la
            // inscripcion (CONFIRMADO/EN_EJECUCION)
            List<Evento> todosLosEventos = eventoServicio.obtenerEventosPorEstado(EstadosHabilitados, true);
            // Agrego a la lista obserbable los eventos CONFIRMADOS/EN_EJECUCION
            listaEventos.setAll(todosLosEventos);
        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar los eventos: " + e.getMessage());
        }
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

    @FXML
    private void filtrarEvento() {
        try {
            // Obtenemos el Tipo de Evento Seleccionado
            TipoEvento tipoSeleccionado = cmbTipoEvento.getSelectionModel().getSelectedItem();
            List<Evento> listaPorTipo = obtenerListaPorTipo(tipoSeleccionado);
            listaEventos.setAll(listaPorTipo);
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un Error Inesperado: " + e.getMessage());
        }
    }

    private List<Evento> obtenerListaPorTipo(TipoEvento unTipoEvento) {
        // Obtengo todos los eventos habilitados e Inscribibles
        List<Evento> listaFiltrada = eventoServicio.obtenerEventosPorEstado(EstadosHabilitados, true);

        // Si es null, devolver todos los eventos
        if (unTipoEvento == null) {
            return listaFiltrada;
        }

        // Filtrar usando streams
        return listaFiltrada.stream()
                .filter(evento -> esDelTipoEspecificado(evento, unTipoEvento))
                .collect(Collectors.toList());
    }

    // Metodo Auxiliar parsa validar si un Evento pertenece a un tipo específico
    private boolean esDelTipoEspecificado(Evento evento, TipoEvento tipo) {
        return switch (tipo) {
            case EXPOSICION ->
                evento instanceof Exposicion;
            case TALLER ->
                evento instanceof Taller;
            case CONCIERTO ->
                evento instanceof Concierto;
            case CICLO_DE_CINE ->
                evento instanceof CicloDeCine;
            case FERIA ->
                evento instanceof Feria;
        };
    }

    @FXML
    private void verificarExistencia() {
        try {
            // Obtenemos el DNI del Participante ingresado en el TextField DNI
            String dniIngresado = txtDNIParticipante.getText().trim();
            // Validamos si el DNI es valido
            validarDNI(dniIngresado);
            // Utilizando el PersonaServicio validamos si ya existe, o no una entidad con
            // dicho DNI
            determinarExistencia(dniIngresado);
            // Configuramos la IGU
            configuracionPosValidacion();
        } catch (Exception e) {
            Alerta.mostrarError("Ocurrió un Error inesperado: " + e.getMessage());
            estadoInicial();
        }
    }

    private void validarDNI(String dniIngresado) {
        // Validamos si es válido el DNI ingresado
        if (dniIngresado == null || dniIngresado.isEmpty()) {
            throw new IllegalArgumentException("Error: Por favor ingrese un DNI VÁLIDO");
        }

        // Validar que contenga solo números
        if (!dniIngresado.matches("\\d+")) {
            throw new IllegalArgumentException("Error: El DNI debe contener solo números");
        }
        // Validar que contenga un mínimo de 7 caracteres
        if (dniIngresado.length() < 7 || dniIngresado.length() > 12) {
            throw new IllegalArgumentException("Error: El DNI debe tener un mínimo de 7 dígitos y un maximo de 12");
        }
    }

    private void determinarExistencia(String dniIngresado) {
        if (personaServicio.buscarPorId(dniIngresado) == null) {
            // Si el DNI ingresado no corresponde a una persona registrada, habilita el
            // campo de ingreso de datos
            vboxDatos.setDisable(false);
            Alerta.mostrarExito("El DNI ingresado NO corresponde a ninguna persona registrada\n"
                    + "Ingrese Acontinuacion los datos del Participante");
            existeRegistro = false;
        } else {
            // Si el DNI ingresado corresponde a una persona registrada
            Alerta.mostrarExito("El DNI ingresado corresponde a una persona registrada");
            existeRegistro = true;
        }
    }

    private void configuracionPosValidacion() {
        // Habilitamos el botón de Inscripción
        btnInscribir.setDisable(false);
        // Habilitamos el botón de Cancelar
        btnCancelar.setDisable(false);
        // DesHabilitamos el textField de DNI
        txtDNIParticipante.setDisable(true);
    }

    @FXML
    private void cancelar() {
        limpiarCampos();
    }

    private void estadoInicial() {
        // Deshabilito el layout de ingreso de datos
        vboxDatos.setDisable(true);
        // Deshabilito el btn de Inscripción
        btnInscribir.setDisable(true);
        // Desabilito el btn de Cancelar
        btnCancelar.setDisable(true);
        // Habilito el TextField del dni
        txtDNIParticipante.setDisable(false);

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
    private void inscribirParticipante() {
        try {
            // Validaciones iniciales
            Evento eventoSeleccionado = validarYObtenerEventoSeleccionado();

            // Validamos que haya cupo
            validarCupoDisponible(eventoSeleccionado);

            // Procesar inscripción según si la persona existe o no
            if (existeRegistro) {
                procesarPersonaExistente(eventoSeleccionado);
            } else {
                procesarPersonaNueva(eventoSeleccionado);
            }
        } catch (IllegalArgumentException e) {
            // Error de validación de campos
            Alerta.mostrarError("Error de datos:\n" + e.getMessage());
        } catch (IllegalStateException e) {
            // Error de cupo o estado del evento
            Alerta.mostrarError("Error de Cupo:\n" + e.getMessage());
        } catch (Exception e) {
            // Error inesperado del sistema
            Alerta.mostrarError("Error: Ocurrió un error inesperado\n" + e.getMessage());
            limpiarCampos();
        }
    }

    // Valida si se selecciono una fila de la tabla
    private Evento validarYObtenerEventoSeleccionado() {
        Evento eventoSeleccionado = tblEvento.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            throw new IllegalArgumentException("Debe seleccionar un evento para inscribir al participante");
        }
        return eventoSeleccionado;
    }

    // Validación del cupo maximo
    private void validarCupoDisponible(Evento evento) {
        if (!evento.isTieneCupo()) {
            // Si no tiene cupo, no hago ninguna validación
            return;
        }

        int participantesActuales = participacionServicio.contarPersonasPorRol(evento, TipoRol.PARTICIPANTE);
        int capacidadMaxima = evento.getCapacidadMaxima();

        if (participantesActuales >= capacidadMaxima) {
            throw new IllegalStateException(
                    String.format("No se puede inscribir al participante. "
                            + "El evento '%s' ya alcanzó su capacidad máxima (%d/%d participantes).",
                            evento.getNombre(), participantesActuales, capacidadMaxima));
        }
    }

    // Maneja las acciones necesarias después de una inscripción exitosa
    private void manejarInscripcionExitosa() {
        Alerta.mostrarExito("Inscripción Realizada Correctamente");
        limpiarCampos();
        actualizarTabla(); // Refrescar la tabla para mostrar cambios en cupos
    }

    // Metodo que inscribe una persona existente a un Eventos
    private void procesarPersonaExistente(Evento eventoSeleccionado) throws Exception {
        String dni = txtDNIParticipante.getText().trim();
        Persona unaPersona = personaServicio.buscarPorId(dni);

        // Validar si ya está inscripta
        if (participacionServicio.existeParticipacion(eventoSeleccionado, unaPersona, TipoRol.PARTICIPANTE)) {
            throw new Exception("La persona: " + unaPersona.getInformacionPersonal()
                    + "\nYa se encuentra inscripta en el evento");
        }

        // Confirmación del usuario
        if (!confirmarInscripcion(eventoSeleccionado)) {
            return;
        }

        // Verificar y asignar rol si es necesario
        if (!unaPersona.getUnaListaRoles().contains(TipoRol.PARTICIPANTE)) {
            personaServicio.asignarRol(unaPersona, TipoRol.PARTICIPANTE);
        }

        // Inscribir al participante
        eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);
        manejarInscripcionExitosa();
    }

    private boolean confirmarInscripcion(Evento unEvento) {
        String dni = txtDNIParticipante.getText().trim();
        return Alerta.confirmarAccion(
                String.format("¿Inscribir el Participante con el DNI: '%s' al evento:\n%s?", dni,
                        unEvento.getNombre()));
    }

    private void procesarPersonaNueva(Evento eventoSeleccionado) throws Exception {
        // Obtener datos del formulario
        String dni = txtDNIParticipante.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        // Validar campos obligatorios ANTES de proceder
        validarCamposObligatorios(dni, nombre, apellido);

        // Confirmación del usuario
        if (!confirmarInscripcion(eventoSeleccionado)) {
            return;
        }

        // Crear lista de roles
        List<TipoRol> listaRol = Arrays.asList(TipoRol.PARTICIPANTE);

        // Crear persona e inscribir
        Persona unaPersona = personaServicio.validarEInsertar(dni, nombre, apellido, telefono, correo, listaRol);
        eventoServicio.inscribirParticipantes(eventoSeleccionado, unaPersona);
        manejarInscripcionExitosa();
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

        // Si hay errores, lanzar excepción SIN mostrar alerta
        if (sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

}