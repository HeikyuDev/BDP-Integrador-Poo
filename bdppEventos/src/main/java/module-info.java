module com.mycompany.bdppeventos {
    
    // JavaFX con transitive
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    
    // ControlsFX (NUEVA LIBRERÍA)
    requires org.controlsfx.controls;
    
    // JPA con Jakarta
    requires jakarta.persistence;
    requires java.sql;
    
    // EclipseLink
    requires eclipselink;
    requires java.instrument;
    requires java.management;
    requires java.naming;
    requires java.xml;
    
    // PostgreSQL
    requires org.postgresql.jdbc;
    requires java.base;
    
    // ABRIR PAQUETES PARA JAVAFX FXML
    opens com.mycompany.bdppeventos.controller to javafx.fxml;
    opens com.mycompany.bdppeventos.controller.ABMPersona to javafx.fxml;
    
    // ABRIR ENTIDADES PARA JPA
    opens com.mycompany.bdppeventos.model.entities to 
        jakarta.persistence, 
        eclipselink, 
        java.base;
    
    // ABRIR ENUMS PARA JPA (si existen)
    opens com.mycompany.bdppeventos.model.enums to 
        jakarta.persistence, 
        eclipselink, 
        java.base;
    
    // EXPORTAR PAQUETES PRINCIPALES
    exports com.mycompany.bdppeventos;
    exports com.mycompany.bdppeventos.controller;
    exports com.mycompany.bdppeventos.model.entities;
    exports com.mycompany.bdppeventos.model.enums;
}