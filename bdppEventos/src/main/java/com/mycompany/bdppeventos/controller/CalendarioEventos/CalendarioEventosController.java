package com.mycompany.bdppeventos.controller.CalendarioEventos;

import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarioEventosController implements Initializable {
    
    // CONTROLES FXML
    @FXML private Label lblMes;
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private GridPane gridCalendario;
    
    // Variables
    private YearMonth mesActual;
    private EventoServicio eventoServicio; 
    private List<Evento> eventosDelMes = new ArrayList<>();
    
    // CONFIGURACIÓN PARA CONTROLAR EL TAMAÑO (más restrictivo)
    private static final int MAX_EVENTOS_VISIBLES = 2; // Máximo 2 eventos visibles por celda
    private static final double ALTURA_CELDA_FIJA = 100.0; // Altura fija para cada celda
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos los servicios
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());
        
        // Inicializamos variables
        mesActual = YearMonth.now();
        
        // IMPORTANTE: Configurar constraints del GridPane para que tenga filas de altura fija
        configurarConstraintsCalendario();
        
        // Configuramos los botones de navegación
        btnAnterior.setOnAction(e -> {
            mesActual = mesActual.minusMonths(1);
            cargarEventosYGenerarCalendario();
        });
        
        btnSiguiente.setOnAction(e -> {
            mesActual = mesActual.plusMonths(1);
            cargarEventosYGenerarCalendario();
        });
        
        // Carga inicial
        cargarEventosYGenerarCalendario();
    }
    
    private void configurarConstraintsCalendario() {
        // Limpiar constraints existentes
        gridCalendario.getRowConstraints().clear();
        
        // Crear exactamente 6 filas con altura ESTRICTAMENTE fija
        for (int i = 0; i < 6; i++) {
            javafx.scene.layout.RowConstraints rowConstraint = new javafx.scene.layout.RowConstraints();
            rowConstraint.setPrefHeight(ALTURA_CELDA_FIJA);
            rowConstraint.setMinHeight(ALTURA_CELDA_FIJA);
            rowConstraint.setMaxHeight(ALTURA_CELDA_FIJA);
            rowConstraint.setVgrow(Priority.NEVER); // CRÍTICO: Nunca permitir crecimiento
            rowConstraint.setFillHeight(true); // Llenar la altura disponible
            gridCalendario.getRowConstraints().add(rowConstraint);
        }
        
        // Configurar el GridPane para tener altura fija total
        gridCalendario.setPrefHeight(6 * ALTURA_CELDA_FIJA);
        gridCalendario.setMinHeight(6 * ALTURA_CELDA_FIJA);
        gridCalendario.setMaxHeight(6 * ALTURA_CELDA_FIJA);
    }
    
    private void cargarEventosYGenerarCalendario() {
        try {
            // Optimización: solo cargar eventos del mes actual
            LocalDate inicioMes = mesActual.atDay(1);
            LocalDate finMes = mesActual.atEndOfMonth();
            
            eventosDelMes = eventoServicio.obtenerEventosEnRango(
                inicioMes, 
                finMes,
                Arrays.asList(EstadoEvento.CONFIRMADO, EstadoEvento.FINALIZADO, EstadoEvento.EN_EJECUCION)
            );
            
            generarCalendario();
            
        } catch (Exception e) {
            Alerta.mostrarError("Error al cargar eventos:" + e.getMessage());
            System.err.println("Error al cargar eventos: " + e.getMessage());
        }
    }
    
    private void generarCalendario() {
        gridCalendario.getChildren().clear();
        
        LocalDate primerDia = mesActual.atDay(1);
        int diasEnMes = mesActual.lengthOfMonth();
        int diaInicioSemana = primerDia.getDayOfWeek().getValue() % 7; // Domingo = 0
        
        // Agregar días del mes anterior (grises/inactivos)
        if (diaInicioSemana > 0) {
            LocalDate ultimoDiaMesAnterior = primerDia.minusDays(1);
            LocalDate fechaAnterior = ultimoDiaMesAnterior.minusDays(diaInicioSemana - 1);
            
            for (int i = 0; i < diaInicioSemana; i++) {
                VBox celdaAnterior = crearCeldaDia(fechaAnterior.plusDays(i), true);
                gridCalendario.add(celdaAnterior, i, 0);
            }
        }
        
        // Días del mes actual
        int fila = 0;
        int columna = diaInicioSemana;
        
        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = mesActual.atDay(dia);
            VBox celda = crearCeldaDia(fecha, false);
            
            gridCalendario.add(celda, columna, fila);
            columna++;
            if (columna > 6) {
                columna = 0;
                fila++;
            }
        }
        
        // Rellenar días del siguiente mes para completar la última fila
        if (columna > 0) {
            LocalDate primerDiaSiguienteMes = mesActual.plusMonths(1).atDay(1);
            for (int i = columna; i <= 6; i++) {
                VBox celdaSiguiente = crearCeldaDia(
                    primerDiaSiguienteMes.plusDays(i - columna), 
                    true
                );
                gridCalendario.add(celdaSiguiente, i, fila);
            }
        }
        
        actualizarTituloMes();
    }
    
    private VBox crearCeldaDia(LocalDate fecha, boolean esDiaInactivo) {
        VBox celda = new VBox();
        celda.getStyleClass().add("calendario-dia");
        celda.setSpacing(2); // Espaciado más pequeño
        celda.setAlignment(Pos.TOP_LEFT);
        
        // CRÍTICO: Configurar tamaños ESTRICTAMENTE fijos
        celda.setMaxWidth(Double.MAX_VALUE);
        celda.setMinHeight(ALTURA_CELDA_FIJA);
        celda.setMaxHeight(ALTURA_CELDA_FIJA);
        celda.setPrefHeight(ALTURA_CELDA_FIJA);
        
        // IMPORTANTE: Control total del crecimiento
        GridPane.setHgrow(celda, Priority.ALWAYS);
        GridPane.setVgrow(celda, Priority.NEVER); // NUNCA permitir crecimiento vertical
        GridPane.setFillHeight(celda, true); // Llenar altura disponible
        
        // Estilos adicionales
        if (esDiaInactivo) {
            celda.getStyleClass().add("dia-inactivo");
        }
        if (fecha.equals(LocalDate.now())) {
            celda.getStyleClass().add("dia-actual");
        }
        
        // Número del día
        Label lblDia = new Label(String.valueOf(fecha.getDayOfMonth()));
        lblDia.getStyleClass().add("numero-dia");
        lblDia.setMaxHeight(18); // Altura fija para el número
        celda.getChildren().add(lblDia);
        
        // Solo mostrar eventos para días del mes actual
        if (!esDiaInactivo) {
            List<Evento> eventosEnFecha = eventosParaFecha(fecha);
            
            // CONTROL MUY ESTRICTO: Solo 2 eventos máximo
            int eventosParaMostrar = Math.min(eventosEnFecha.size(), MAX_EVENTOS_VISIBLES);
            
            for (int i = 0; i < eventosParaMostrar; i++) {
                Evento evento = eventosEnFecha.get(i);
                Label lblEvento = new Label(truncarTexto(evento.getNombre(), 12)); // Texto más corto
                lblEvento.getStyleClass().add("evento-label");
                lblEvento.setWrapText(false);
                lblEvento.setMaxHeight(16); // Altura más pequeña
                lblEvento.setPrefHeight(16);
                lblEvento.setMinHeight(16);
                
                // Agregar clase CSS según el estado del evento
                String estadoClass = "evento-" + evento.getEstado().name().toLowerCase().replace("_", "-");
                lblEvento.getStyleClass().add(estadoClass);
                
                celda.getChildren().add(lblEvento);
            }
            
            // Mostrar indicador si hay más eventos
            if (eventosEnFecha.size() > MAX_EVENTOS_VISIBLES) {
                int eventosRestantes = eventosEnFecha.size() - MAX_EVENTOS_VISIBLES;
                Label lblMas = new Label("+" + eventosRestantes + " más");
                lblMas.getStyleClass().add("mas-eventos");
                lblMas.setMaxHeight(14);
                lblMas.setPrefHeight(14);
                lblMas.setMinHeight(14);
                lblMas.setOnMouseClicked(event -> {
                    mostrarEventosDelDia(fecha, eventosEnFecha);
                });
                celda.getChildren().add(lblMas);
            }
            
            // Hacer clickeable para ver detalles (doble click)
            if (!eventosEnFecha.isEmpty()) {
                celda.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        mostrarEventosDelDia(fecha, eventosEnFecha);
                    }
                });
                celda.getStyleClass().add("dia-con-eventos");
            }
        }
        
        return celda;
    }
    
    private void actualizarTituloMes() {
        String mesNombre = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));
        String titulo = mesNombre.substring(0, 1).toUpperCase() + mesNombre.substring(1) + " " + mesActual.getYear();
        lblMes.setText(titulo);
    }
    
    private List<Evento> eventosParaFecha(LocalDate fecha) {
        return eventosDelMes.stream()
                .filter(ev -> !fecha.isBefore(ev.getFechaInicio()) && !fecha.isAfter(ev.getFechaFin()))
                .sorted(Comparator.comparing(Evento::getFechaInicio))
                .collect(Collectors.toList());
    }
    
    private String truncarTexto(String texto, int maxLongitud) {
        if (texto == null) return "";
        if (texto.length() <= maxLongitud) return texto;
        return texto.substring(0, maxLongitud - 3) + "...";
    }
    
    private void mostrarEventosDelDia(LocalDate fecha, List<Evento> eventos) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eventos del día");
        alert.setHeaderText("Eventos para " + fecha.toString());
        
        StringBuilder contenido = new StringBuilder();
        for (Evento evento : eventos) {
            contenido.append("• ").append(evento.getNombre())
                    .append(" (").append(evento.getEstado()).append(")")
                    .append("\n  Desde: ").append(evento.getFechaInicio())
                    .append(" hasta: ").append(evento.getFechaFin())
                    .append("\n\n");
        }
        
        alert.setContentText(contenido.toString());
        alert.showAndWait();
    }
}