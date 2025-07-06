module com.mycompany.bdppeventos {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    // JPA con Jakarta
    requires jakarta.persistence;
    requires java.sql;
    
   
    // PostgreSQL
    requires org.postgresql.jdbc;
    requires java.base;
    
    // Exports para JavaFX
    opens com.mycompany.bdppeventos to javafx.fxml;
    exports com.mycompany.bdppeventos;
    
    // IMPORTANTE: Abrir entidades para JPA
    opens com.mycompany.bdppeventos.model.entities to         
        jakarta.persistence;
    
    // IMPORTANTE: Abrir el paquete view para JavaFX (ESTO FALTABA)
    opens com.mycompany.bdppeventos.view to javafx.fxml;
    exports com.mycompany.bdppeventos.view;
}