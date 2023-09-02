package com.example.filafx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class RetirarSenhaController {

    @FXML
    private Label label_senha;

    @FXML
    private Button btn_pref;

    @FXML
    private Button btn_normal;

    static int prioridade = 0;

    static int normal = 0;

    @FXML
    private void RetirarSenha(MouseEvent mouseEvent) throws InterruptedException {
        label_senha.setText(GerarSenha("N"));
        DisabilitarBotoes(btn_normal, btn_pref, true);
        LimparLabelSenha(label_senha, btn_normal, btn_pref);
    }

    @FXML
    private void RetirarSenhaPrioridade(MouseEvent mouseEvent) throws InterruptedException {
        label_senha.setText(GerarSenha("P"));
        DisabilitarBotoes(btn_normal, btn_pref, true);
        LimparLabelSenha(label_senha, btn_normal, btn_pref);
    }

    public static void DisabilitarBotoes(Button btn_normal, Button btn_pref, Boolean isDisable){
        btn_normal.setDisable(isDisable);
        btn_pref.setDisable(isDisable);
    }

    public static String GerarSenha(String tipo){
        if(tipo == "P"){
            prioridade++;
            return String.format("P%03d", prioridade);
        } else if (tipo == "N") {
            normal++;
            return String.format("N%03d", normal);
        }
        return null;
    }

    public static void LimparLabelSenha(Label label, Button btn_normal, Button btn_pref){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DisabilitarBotoes(btn_normal, btn_pref, false);
                        label.setText("");
                    }
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
