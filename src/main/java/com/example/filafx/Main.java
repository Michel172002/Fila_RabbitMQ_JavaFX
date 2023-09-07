package com.example.filafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderRetirarSenha = new FXMLLoader(Main.class.getResource("RetirarSenhaPainel.fxml"));
        Scene scene = new Scene(fxmlLoaderRetirarSenha.load());
        stage.setTitle("Painel Retirar Senhas");
        stage.setScene(scene);

        FXMLLoader fxmlLoaderAtendente = new FXMLLoader(Main.class.getResource("AtendentePainel.fxml"));
        Stage stageAtentende = new Stage();
        Scene sceneAtendente = new Scene(fxmlLoaderAtendente.load());
        stageAtentende.setTitle("Painel atentendente");
        stageAtentende.setScene(sceneAtendente);

        FXMLLoader fxmlLoaderPainel = new FXMLLoader(Main.class.getResource("SenhasChamadasPainel.fxml"));
        Stage stagePainel = new Stage();
        Scene scenePainel = new Scene(fxmlLoaderPainel.load());
        stagePainel.setTitle("Painel exibir senhas Chamadas");
        stagePainel.setScene(scenePainel);

        stage.show();
        stageAtentende.show();
        stagePainel.show();
    }

    public static void main(String[] args) {
        launch();
    }
}