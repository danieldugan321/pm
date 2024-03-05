module com.example.superpassman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens com.example.superpassman to javafx.fxml;
    exports com.example.superpassman;
}