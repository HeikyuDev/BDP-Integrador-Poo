package com.mycompany.bdppeventos.controller.CalendarioEventos;

import com.mycompany.bdppeventos.App;
import com.mycompany.bdppeventos.model.entities.Evento;
import com.mycompany.bdppeventos.model.enums.EstadoEvento;
import com.mycompany.bdppeventos.services.Evento.EventoServicio;
import com.mycompany.bdppeventos.util.Alerta;
import com.mycompany.bdppeventos.util.RepositorioContext;
import com.mycompany.bdppeventos.util.StageManager;
import com.mycompany.bdppeventos.view.Vista;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

/**
 * Controlador principal del Calendario de Eventos
 *
 * Responsabilidades: - Gestionar la navegación entre meses - Cargar y mostrar
 * eventos del mes actual - Generar dinámicamente las celdas del calendario -
 * Coordinar la interacción con las celdas individuales
 */
public class CalendarioEventosController implements Initializable {

    // ========== CONTROLES FXML ==========
    @FXML
    private Label lblMes;              // Etiqueta que muestra el mes y año actual    
    @FXML
    private GridPane gridCalendario;   // Contenedor principal del calendario (7x6 grid)

    // ========== VARIABLES DE ESTADO ==========
    private YearMonth mesActual;                    // Mes y año que se está visualizando actualmente
    private EventoServicio eventoServicio;          // Servicio para operaciones CRUD de eventos
    private List<Evento> eventosDelMes = new ArrayList<>();  // Lista de eventos del Mes       

    /**
     * Inicialización del controlador Se ejecuta automáticamente después de
     * cargar el FXML
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Secuencia de inicialización
        inicializarServiciosyEstados();        // Configura servicios y estado inicial        
        cargarCalendarioInicial();     // Genera el calendario del mes actual
    }

    /**
     * Inicializa los servicios necesarios y el estado inicial del calendario
     */
    private void inicializarServiciosyEstados() {
        // Inicializar servicio de eventos con el repositorio configurado
        eventoServicio = new EventoServicio(RepositorioContext.getRepositorio());

        // Establecer el mes y año actual como punto de partida
        mesActual = YearMonth.now();
    }

    /**
     * Carga inicial del calendario con el mes actual
     */
    private void cargarCalendarioInicial() {
        cargarEventosYGenerarCalendario();
    }

    /**
     * Configura los listeners para los botones de navegación entre meses
     */
    @FXML
    private void retrocederYcargarCalendario() {
        // Botón anterior: retrocede un mes y recarga el calendario        
        // Le resto 1 Mes a la fecha almacenada en mesActual
        mesActual = mesActual.minusMonths(1);
        // Recargo el calendario para mostrar los eventos de ese mes
        cargarEventosYGenerarCalendario();
    }

    @FXML
    private void avanzarYcargarCalendario() {
        // Botón siguiente: avanza un mes y recarga el calendario
        // Le Sumo 1 Mes a la fecha almacenada en mesActual
        mesActual = mesActual.plusMonths(1);
        // Recargo el calendario para mostrar los eventos de ese mes
        cargarEventosYGenerarCalendario();

    }

    /**
     * Método principal que coordina la carga de eventos y generación del
     * calendario Este método se llama cada vez que se cambia de mes o se
     * necesita refrescar
     */
    private void cargarEventosYGenerarCalendario() {
        try {
            // 1. Cargar eventos del mes desde la base de datos
            cargarEventosDelMes(); // El atributo eventosDelMes tiene los eventos que ocurren en ese mes (Puede ser una lista vacia)

            // 2. Generar visualmente el calendario con los eventos cargados
            generarCalendario();

            // 3. Actualizar el título del mes en la interfaz
            actualizarTituloMes();

        } catch (Exception e) {
            // En caso de error, mostrar mensaje al usuario y mantener estado anterior
            Alerta.mostrarError("Error al cargar eventos del calendario: " + e.getMessage());
        }
    }

    /**
     * Carga los eventos del mes actual desde la base de datos Filtra solo
     * eventos con estados válidos para mostrar en el calendario
     */
    private void cargarEventosDelMes() {
        // Calcular el rango de fechas del mes actual
        LocalDate inicioMes = mesActual.atDay(1);                    // Primer día del mes
        LocalDate finMes = mesActual.atEndOfMonth();                 // Último día del mes

        // Estados de eventos que se mostrarán en el calendario
        List<EstadoEvento> estadosValidos = Arrays.asList(
                EstadoEvento.CONFIRMADO, // Eventos confirmados
                EstadoEvento.FINALIZADO, // Eventos ya finalizados
                EstadoEvento.EN_EJECUCION // Eventos actualmente en ejecución
        );

        // Cargar eventos del rango de fechas con estados válidos
        eventosDelMes = eventoServicio.obtenerEventosEnRango(inicioMes, finMes, estadosValidos);
    }

    /**
     * Genera visualmente el calendario completo Crea una matriz de 7x6 (42
     * celdas) que representa las semanas del mes
     */
    private void generarCalendario() {
        // Limpiar calendario anterior
        gridCalendario.getChildren().clear();

        // Información del mes actual
        LocalDate primerDia = mesActual.atDay(1);        // Primer día del mes
        int diasEnMes = mesActual.lengthOfMonth();        // Total de días en el mes

        // Calcular posición inicial (lunes = 0, martes = 1.... etc.)
        int diaInicioSemana = primerDia.getDayOfWeek().getValue() - 1;

        // Variables para controlar la posición en el grid
        int fila = 0;
        int columna = 0;

        // ===== FASE 1: Generar días del mes anterior (para completar primera semana) =====
        if (diaInicioSemana > 0) {
            // Calculo la fecha inicial Restando el primer dia del mes - el dia de inicio 
            // MARTES = 1, MIERCOLES = 2...  1/8/2025 (-4)-> 28/7/2025(Fecha inicial)
            LocalDate fechaInicial = primerDia.minusDays(diaInicioSemana);

            for (int i = 0; i < diaInicioSemana; i++) {
                // dia inicio semana representaria la cantidad de celdas de mes adyacente se crearian
                LocalDate fechaAnterior = fechaInicial.plusDays(i);
                // En la primera iteracion fecha anterior seria equivalente a fecha inicial (La primer columna de la primer fila)
                VBox celdaDia = crearCeldaDia(fechaAnterior, true); // true = es día de mes anterior
                // Creamos una nueva celda que representaria una celda del mes anterior
                gridCalendario.add(celdaDia, columna, fila);
                // Agregamos la celda a la grilla en la columna y fila pasadas como parametro
                // Inicialmente 0 y 0 -> 1 - 0 -> 2 - 0 -> 3 - 0 ....
                columna++;
            }
        } else {
            columna = 0; // Si el mes empieza en Lunes, columna inicial es 0
        }

        // ===== FASE 2: Generar días del mes actual =====
        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = mesActual.atDay(dia);
            // Obtengo la fecha del determinado dia
            VBox celdaDia = crearCeldaDia(fecha, false); // false = es día del mes actual            
            // Creo una nueva celda que representa las celdas de cada dia
            gridCalendario.add(celdaDia, columna, fila);

            // Avanzar a siguiente posición
            columna++;

            // Valido si pasamos a la siguiente Fila
            if (columna > 6) { // Si completamos una semana (7 días)
                columna = 0;
                fila++;
            }
        }

        // ===== FASE 3: Completar días del siguiente mes (para llenar última semana) =====
        if (columna > 0) {
            LocalDate primerDiaSiguienteMes = mesActual.plusMonths(1).atDay(1);

            for (int i = columna; i <= 6; i++) {
                // i representaria la columna, E iría hasta 6.
                LocalDate fechaSiguiente = primerDiaSiguienteMes.plusDays(i - columna);
                // Obtengo la fecha siguiente {1IT: i - columna = 0} por lo tanto no se suman dias y fecha siguiente es igual al primer dia
                VBox celdaDia = crearCeldaDia(fechaSiguiente, true); // true = es día de mes siguiente
                // Creo una celda, para los dias del siguiente mes
                gridCalendario.add(celdaDia, i, fila);
                // Agrego la celda a la determinada seccion [fila,i]
            }
        }
    }

    /**
     * Crea una celda individual del calendario utilizando el StageManager     
     * @param fecha La fecha que representará esta celda
     * @param esDeMesAdyacente true si es del mes anterior/siguiente, false si
     * es del mes actual
     * @return VBox configurado con el controlador CeldaDiaController
     */
    private VBox crearCeldaDia(LocalDate fecha, boolean esDeMesAdyacente) {
        try {
            // Usar StageManager para cargar la vista con su controlador
            Pair<CeldaDiaController, VBox> resultado = StageManager.cargarVistaVBox(Vista.PanelCeldaDia.getRutaFxml());

            if (resultado != null) {
                VBox celdaDia = resultado.getValue();                    // El nodo visual
                CeldaDiaController controller = resultado.getKey();      // El controlador

                // Obtener eventos específicos para esta fecha
                List<Evento> eventosDelDia = obtenerEventosParaFecha(fecha);

                // Configurar la celda con todos los datos necesarios
                controller.configurarCelda(
                        fecha, // La fecha de esta celda
                        eventosDelDia, // Eventos que ocurren en esta fecha
                        esDeMesAdyacente, // Si pertenece al mes actual o adyacente
                        this::mostrarDetalleEventosDelDia // Callback para mostrar detalles (Le mando este metodo para que ejecute)
                );

                return celdaDia;

            } else {
                throw new RuntimeException("StageManager retornó null al cargar la celda del calendario");
            }

        } catch (Exception e) {
            // Lanzar excepción con información detallada para debugging
            throw new RuntimeException("Error crítico creando celda del calendario para fecha " + fecha
                    + ": " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el título del mes en la interfaz Formato: "Enero 2024",
     * "Febrero 2024", etc.
     */
    private void actualizarTituloMes() {
        // Obtener nombre del mes en español
        String mesNombre = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));

        // Obtengo el mes del año utilizando la primera letra en mayuscula seguido de las demas en minusculas
        String titulo = mesNombre.substring(0, 1).toUpperCase()
                // Obtengo la primer letra del mes en mayuscula
                + mesNombre.substring(1) + " "
                // Obtengo las demas letras del mes y agrego un espacio
                + mesActual.getYear();
                // Le agrego el año al que corresponde el mes
        

        lblMes.setText(titulo);
    }

    /**
     * Obtiene todos los eventos que ocurren en una fecha específica Un evento
     * puede durar varios días, por lo que se verifica si la fecha está dentro
     * del rango de duración del evento
     *
     * @param fecha La fecha para la cual buscar eventos
     * @return Lista de eventos ordenados por fecha de inicio
     */
    private List<Evento> obtenerEventosParaFecha(LocalDate fecha) {
        return eventosDelMes.stream()
                .filter(evento -> estaEventoEnFecha(evento, fecha)) // Filtrar eventos que incluyen esta fecha
                .sorted(Comparator.comparing(Evento::getFechaInicio)) // Ordenar por fecha de inicio
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un evento específico ocurre en una fecha dada Un evento
     * ocurre en una fecha si: - La fecha es mayor o igual a la fecha de inicio
     * del evento - La fecha es menor o igual a la fecha de fin del evento
     *
     * @param evento El evento a verificar
     * @param fecha La fecha a verificar
     * @return true si el evento ocurre en la fecha, false en caso contrario
     */
    private boolean estaEventoEnFecha(Evento evento, LocalDate fecha) {
        return !fecha.isBefore(evento.getFechaInicio()) && !fecha.isAfter(evento.getFechaFin());
    }

    /**
     * Muestra un modal o diálogo con los detalles de todos los eventos de un
     * día específico Este método es llamado cuando el usuario hace clic en una
     * celda del calendario
     *
     * @param fecha La fecha de la cual mostrar los eventos
     */
    private void mostrarDetalleEventosDelDia(LocalDate fecha) {
        // Obtiene la lista de eventos de esa fecha
        List<Evento> eventos = obtenerEventosParaFecha(fecha);

        // Crear una Alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        // Definir formato de fecha día/mes/año
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Configuraciones de la alerta                
        
        
        alert.setTitle("Eventos del Día");
        alert.setHeaderText("Eventos programados para " + String.valueOf(fecha.format(formatoFecha)));                
        
        // Construir el contenido del diálogo
        StringBuilder contenido = new StringBuilder();

        if (eventos.isEmpty()) {
            contenido.append("No hay eventos programados para este día.");
        } else {
            for (Evento evento : eventos) {
                contenido.append("• ").append(evento.getNombre())
                        .append(" (").append(evento.getEstado()).append(")")
                        .append("\n  Período: ").append(evento.getFechaInicio().format(formatoFecha))
                        .append(" → ").append(evento.getFechaFin().format(formatoFecha));

                contenido.append("\n\n");
            }
        }

        alert.setContentText(contenido.toString());
        alert.showAndWait();
    }
}
