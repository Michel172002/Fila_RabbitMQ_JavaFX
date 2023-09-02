package com.example.filafx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class RetirarSenhaController {

    @FXML
    private Label label_senha;

    @FXML
    private void RetirarSenha(MouseEvent mouseEvent) throws InterruptedException {
        label_senha.setText("N001");
        LimparLabelSenha(label_senha);
    }

    @FXML
    private void RetirarSenhaPrioridade(MouseEvent mouseEvent) throws InterruptedException {
        label_senha.setText("P001");
        LimparLabelSenha(label_senha);
    }

    public static void LimparLabelSenha(Label label){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        label.setText("");
                    }
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
