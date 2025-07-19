package com.mycompany.bdppeventos.view;



// Declaramos un enumerado con todas las vistas de la aplicación
public enum Vista {


    // FXML ventana principal
    PantallaPrincipal("PantallaPrincipal"),    
    
    // FXML de la seccion ABM eventos
    FormularioEvento("ABMEvento/FormularioEvento"),
    PanelCicloCine("ABMEvento/PanelCicloCine"),
    PanelConcierto("ABMEvento/PanelConcierto"),
    PanelExposicion("ABMEvento/PanelExposicion"),
    PanelFeria("ABMEvento/PanelFeria"),
    PanelTaller("ABMEvento/PanelTaller"),
    
    // FXML de la seccion  ABM Persona
    
    FormularioPersona("ABMPersona/FormularioPersona"),
    FormularioPersona2("ABMPersona/FormularioPersona"),
    
    // FXML de la seccion Inscribir Participante
    
    InscribirParticipante("ABMParticipante/InscribirParticipante"),
    
    // FXML de la seccion ABM Proyeccion
    
    FormularioProyeccion("ABMProyeccion/FormularioProyeccion");
    
            
    private final String rutaFxml;  // Ruta relativa del archivo FXML que define la vista

    // Constructor del enumerado, se utiliza para inicializar las constantes con un título y una ruta FXML
    private Vista(String rutaFxml) {    
        this.rutaFxml = rutaFxml;
    }        

    // Devuelve la ruta completa del archivo FXML correspondiente a la vista
    public String getRutaFxml() {
        return String.format(("fxml/%s.fxml"), rutaFxml);  // Se asume que todos los archivos FXML están en la carpeta "fxml"
    }
}
