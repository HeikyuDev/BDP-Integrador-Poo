module com.mycompany.bdppeventos {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;

    // Necesario para que FXMLLoader pueda acceder vía reflexión
    opens com.mycompany.bdppeventos.view to javafx.fxml;
    opens com.mycompany.bdppeventos.controller.ABMPersona to javafx.fxml;

    // Si luego tienes otros controladores, agrega más opens así:
    // opens com.mycompany.bdppeventos.controller.OTROCONTROLADOR to javafx.fxml;

    // Para que otras partes del sistema puedan importar tus clases si fuera
    // necesario
    exports com.mycompany.bdppeventos;
    exports com.mycompany.bdppeventos.model.entities;
}
