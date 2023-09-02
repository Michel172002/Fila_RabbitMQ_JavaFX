package com.example.filafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RetirarSenhaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RetirarSenhaApplication.class.getResource("RetirarSenhaPainel.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Painel Retirar Senhas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}