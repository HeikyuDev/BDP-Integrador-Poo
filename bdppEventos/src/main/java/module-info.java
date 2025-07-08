module com.mycompany.bdppeventos {
    
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    // JPA con Jakarta
    requires jakarta.persistence;
    requires java.sql;
    
    // EclipseLink
    requires eclipselink;
    requires java.instrument;
    requires java.management;
    requires java.naming;
    requires java.xml;
    // REMOVIDO: requires java.logging;
    
    // PostgreSQL
    requires org.postgresql.jdbc;
    requires java.base;
    
    // ABRIR PAQUETES PARA JAVAFX FXML
    opens com.mycompany.bdppeventos.controller to javafx.fxml;
    
    // ABRIR ENTIDADES PARA JPA
    opens com.mycompany.bdppeventos.model.entities to 
        jakarta.persistence, 
        eclipselink, 
        java.base;
    
    // ABRIR SERVICES PARA JPA
    opens com.mycompany.bdppeventos.services to 
        eclipselink, 
        jakarta.persistence;
    
    // EXPORTAR PAQUETES PRINCIPALES
    exports com.mycompany.bdppeventos;
    exports com.mycompany.bdppeventos.controller;
}