module com.example.filafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.rabbitmq.client;
    requires org.slf4j;
    requires java.sql;

    opens com.example.filafx to javafx.fxml;
    exports com.example.filafx;

    exports com.example.filafx.controller;
    opens com.example.filafx.controller;
}