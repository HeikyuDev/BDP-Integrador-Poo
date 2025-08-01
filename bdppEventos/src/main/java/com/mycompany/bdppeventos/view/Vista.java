package com.mycompany.bdppeventos.view;



// Declaramos un enumerado con todas las vistas de la aplicación
public enum Vista {


    // FXML ventana principal
    PantallaPrincipal("","PantallaPrincipal"),    
    
    // FXML de la seccion ABM eventos
    FormularioEvento("","ABMEvento/FormularioEvento"),
    PanelCicloCine("","ABMEvento/PanelCicloCine"),
    PanelConcierto("","ABMEvento/PanelConcierto"),
    PanelExposicion("","ABMEvento/PanelExposicion"),
    PanelFeria("","ABMEvento/PanelFeria"),
    PanelTaller("","ABMEvento/PanelTaller"),
    PanelVacio("","ABMEvento/PanelVacio"),
    
    // FXML DE LA VISUALIZACION DE LOS DATOS
    
    VisualizacionExposicion("Detalles de la Exposicion","ABMEvento/Visualizaciones/VisualizacionExposicion"),
    VisualizacionTaller("Detalles del Taller","ABMEvento/Visualizaciones/VisualizacionTaller"),
    VisualizacionConcierto("Detalles del Concierto","ABMEvento/Visualizaciones/VisualizacionConcierto"),
    VisualizacionCicloCine("Detalles del Ciclo de Cine","ABMEvento/Visualizaciones/VisualizacionCicloDeCine"),
    VisualizacionFeria("Detalles de la Feria","ABMEvento/Visualizaciones/VisualizacionFeria"),
    // FXML de la seccion  ABM Persona
    
    ListaDePersonas("","ABMPersona/ListaDePersonas"),
    FormularioPersona("","ABMPersona/FormularioPersona"),
    
    // FXML de la seccion Inscribir Participante
    
    InscribirParticipante("","ABMParticipante/InscribirParticipante"),
    
    // FXML de la seccion ABM Proyeccion
    
    FormularioProyeccion("Formulario Proyeccion","ABMProyeccion/FormularioProyeccion"),
    
    // FXML de la seccion ABM Pelicula
    
    FormularioPelicula("Formulario Pelicula","ABMPelicula/FormularioPelicula"),
    
    // FXML de la seccion ABM Tipo De Arte
    
    FormularioTipoDeArte("Formulario Tipo De Arte","ABMTipoDeArte/FormularioTipoDeArte"),
    
    // FXML de la seccion de Ver Participantes
    
    PanelVerParticipantes("","VerParticipantes/VerParticipantes");
    
    private final String rutaFxml;  // Ruta relativa del archivo FXML que define la vista
    private final String titulo;

    // Constructor del enumerado, se utiliza para inicializar las constantes con un título y una ruta FXML
    private Vista(String titulo ,String rutaFxml) {    
        this.titulo = titulo;
        this.rutaFxml = rutaFxml;
    }        

    // Devuelve El titulo correspondiente de la vista

    public String getTitulo() {
        return titulo;
    }    
    
    // Devuelve la ruta completa del archivo FXML correspondiente a la vista
    public String getRutaFxml() {
        return "/fxml/" + this.rutaFxml + ".fxml";  // Se asume que todos los archivos FXML están en la carpeta "fxml"
    }
    
}
