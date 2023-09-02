module com.example.filafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.filafx to javafx.fxml;
    exports com.example.filafx;

    exports com.example.filafx.controller;
    opens com.example.filafx.controller;
}