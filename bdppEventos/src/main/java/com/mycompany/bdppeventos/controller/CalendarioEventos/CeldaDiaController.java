package com.mycompany.bdppeventos.controller.CalendarioEventos;

import com.mycompany.bdppeventos.model.entities.Evento;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;


// Controlador de una Celda del Calendario
public class CeldaDiaController implements Initializable {

    @FXML
    private VBox root; // VBox principal (celda)
    
    @FXML
    private Label lblDia; // Representa el numero del dia   {1,2,3.... 28/29/30/31}
    @FXML
    private VBox contenedorEventos; // contenedor donde se añaden los Label de cada evento visible.
    @FXML
    private Label lblMasEventos; // La label que muestra + 2 mas

    // Variables de configuración
    private LocalDate fecha; //  la fecha que representa esta celda.
    private List<Evento> eventos; // lista de eventos asociados a esa fecha.
    private boolean esDiaInactivo; // indica si la celda pertenece a otro mes
    private Consumer<LocalDate> mostrarEventos; // fUNCION EXTERNA que esta clase puede ejecutar 

    // COSNTATES
    private static final int MAX_EVENTOS_VISIBLES = 3; // máximo eventos mostrados en la celda.

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar constraints del GridPane para esta celda
        configurarConstraintsGridPane(); // configura cómo se comporta la celda dentro del GridPane padre.

        // Configurar eventos de clic
        configurarEventosClic();
    }

    private void configurarConstraintsGridPane() {
        GridPane.setHgrow(root, Priority.ALWAYS);
        // root ocupa todo el espacio horizontal disponible
        GridPane.setVgrow(root, Priority.NEVER);
        // root no debe crecer mas alla de su altura natural
        GridPane.setFillHeight(root, true);
        // root debe llenar toda la altura disponible verticalmente
    }

    private void configurarEventosClic() {
        // Doble clic para mostrar todos los eventos de este día
        root.setOnMouseClicked(event -> {
            // Verificar si fue doble clic y si hay eventos en la fecha
            if (event.getClickCount() == 2 && !eventos.isEmpty()) {
                // 'mostrarEventos' es una función (callback) que fue pasada para mostrar detalles de eventos
                if (mostrarEventos != null) {
                    // Ejecutar esa función pasando la fecha actual para mostrar los eventos de ese día
                    mostrarEventos.accept(fecha);
                }
            }
        });


        // Clic en "más eventos"
        lblMasEventos.setOnMouseClicked(event -> {
            // 'mostrarEventos' es una función (callback) que fue pasada para mostrar detalles de eventos
            if (mostrarEventos != null) {            
                // Ejecutar esa función pasando la fecha actual para mostrar los eventos de ese día
                mostrarEventos.accept(fecha);
            }
        });
    }

    public void configurarCelda(LocalDate fecha, List<Evento> eventos, boolean esDiaInactivo,
            Consumer<LocalDate> MostrarEventos) {
        this.fecha = fecha; 
        this.eventos = eventos;
        this.esDiaInactivo = esDiaInactivo;
        this.mostrarEventos = MostrarEventos;

        actualizarVisualizacion();
    }

    private void actualizarVisualizacion() {        
        // Configurar número del día (1,2,...)
        lblDia.setText(String.valueOf(fecha.getDayOfMonth()));

        // Configurar clases CSS condicionales
        if (esDiaInactivo) {
            root.getStyleClass().add("dia-inactivo");
        } else {
            root.getStyleClass().remove("dia-inactivo");
        }

        if (fecha.equals(LocalDate.now())) {
            root.getStyleClass().add("dia-actual");
        } else {
            root.getStyleClass().remove("dia-actual");
        }

        // Limpiar eventos anteriores
        contenedorEventos.getChildren().clear();
        lblMasEventos.setVisible(false);

        // Solo mostrar eventos para días del mes actual
        if (!esDiaInactivo && !eventos.isEmpty()) {
            root.getStyleClass().add("dia-con-eventos");

            // Mostrar eventos limitados
            int eventosParaMostrar = Math.min(eventos.size(), MAX_EVENTOS_VISIBLES);
            // Retorna el menor entre eventos.size y la constante de MAX_EVENTOS_VISIBLES

            for (int i = 0; i < eventosParaMostrar; i++) {
                Evento evento = eventos.get(i);
                // Obtengo de la lista eventos de este dia, el evento en la posicion i
                Label lblEvento = crearLabelEvento(evento);
                // Creo una label pasando el evento
                contenedorEventos.getChildren().add(lblEvento);
                // Agrego la etiqueta al contenedor de eventos 
            }

            // Mostrar indicador si hay más eventos
            if (eventos.size() > MAX_EVENTOS_VISIBLES) {
                // Si hay mas mas eventos que los maximos visibles
                int eventosRestantes = eventos.size() - MAX_EVENTOS_VISIBLES;
                // Determinar los eventos restantes
                lblMasEventos.setText("+" + eventosRestantes + " más");
                // Le asigno un nuevo texto a la etiqueta lblMas evento
                lblMasEventos.setVisible(true);
                // La hago visible
            }
        } else {
            root.getStyleClass().remove("dia-con-eventos");
        }
    }

    private Label crearLabelEvento(Evento evento) {
        Label lblEvento = new Label(evento.getNombre());
        // Creo una etiqueta, dentro de la misma va el nombre del evento
        lblEvento.getStyleClass().add("evento-label");
        // Le agrego el estilo de evento-label

        // Ajusto el CSS que tendra dependiendo de estado
        String estadoClass = "evento-" + evento.getEstado().name().toLowerCase().replace("_", "-");
        // Obtengo el CSS que tendra el evento con su determinado estado(En minusculas), remplazando _ por -
        lblEvento.getStyleClass().add(estadoClass);
        // Agrego el estilo a la lbl        
        return lblEvento;
    }
    
}
