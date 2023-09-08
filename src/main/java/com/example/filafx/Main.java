package com.example.filafx;

import com.example.filafx.controller.AtendenteController;
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
        AtendenteController atendenteController = new AtendenteController();
        atendenteController.setGuiche("G01");
        atendenteController.setTitulo("ATENDENTE 01 / GUICHE 01");
        fxmlLoaderAtendente.setController(atendenteController);
        Stage stageAtentende = new Stage();
        Scene sceneAtendente = new Scene(fxmlLoaderAtendente.load());
        stageAtentende.setTitle("Painel atentendente");
        stageAtentende.setScene(sceneAtendente);

        FXMLLoader fxmlLoaderAtendente2 = new FXMLLoader(Main.class.getResource("AtendentePainel.fxml"));
        AtendenteController atendenteController2 = new AtendenteController();
        atendenteController2.setGuiche("G02");
        atendenteController2.setTitulo("ATENDENTE 02 / GUICHE 02");
        fxmlLoaderAtendente2.setController(atendenteController2);
        Stage stageAtentende2 = new Stage();
        Scene sceneAtendente2 = new Scene(fxmlLoaderAtendente2.load());
        stageAtentende2.setTitle("Painel atentendente2");
        stageAtentende2.setScene(sceneAtendente2);

        FXMLLoader fxmlLoaderPainel = new FXMLLoader(Main.class.getResource("SenhasChamadasPainel.fxml"));
        Stage stagePainel = new Stage();
        Scene scenePainel = new Scene(fxmlLoaderPainel.load());
        stagePainel.setTitle("Painel exibir senhas Chamadas");
        stagePainel.setScene(scenePainel);

//        stage.setX(0);
//        stageAtentende.setX(70);
//        stageAtentende.setY(500);
//        stageAtentende2.setX(470);
//        stageAtentende2.setY(500);
//        stagePainel.setX(1000);

        stage.show();
        stageAtentende.show();
        stageAtentende2.show();
        stagePainel.show();
    }

    public static void main(String[] args) {
        launch();
    }
}