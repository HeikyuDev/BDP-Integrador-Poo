module com.mycompany.bdppeventos {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.bdppeventos to javafx.fxml;
    exports com.mycompany.bdppeventos;
}
