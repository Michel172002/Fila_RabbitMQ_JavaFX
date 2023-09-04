package com.example.filafx.controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RetirarSenhaController {

    @FXML
    private Label label_senha;

    @FXML
    private Button btn_pref;

    @FXML
    private Button btn_normal;

    private static int prioridade = 0;

    private static int normal = 0;

    private static final String FILA_PRIORIDADE = "fila_prioridade";
    private static final String FILA_NORMAL = "fila_normal";

    @FXML
    private void RetirarSenha(MouseEvent mouseEvent) throws InterruptedException {
        String senha = GerarSenha("N");
        label_senha.setText(senha);
        try{
            EnviarSenhaParaFila(senha, FILA_NORMAL);
        }catch (Exception e){
            System.out.println("Erro ao enviar para o RabbitMQ: " + e);
        }
        DisabilitarBotoes(btn_normal, btn_pref, true);
        LimparLabelSenha(label_senha, btn_normal, btn_pref);
    }

    @FXML
    private void RetirarSenhaPrioridade(MouseEvent mouseEvent) throws InterruptedException {
        String senha = GerarSenha("P");
        label_senha.setText(senha);
        try{
            EnviarSenhaParaFila(senha, FILA_PRIORIDADE);
        }catch (Exception e){
            System.out.println("Erro ao enviar para o RabbitMQ: " + e);
        }
        DisabilitarBotoes(btn_normal, btn_pref, true);
        LimparLabelSenha(label_senha, btn_normal, btn_pref);
    }

    public static void DisabilitarBotoes(Button btn_normal, Button btn_pref, Boolean isDisable){
        btn_normal.setDisable(isDisable);
        btn_pref.setDisable(isDisable);
    }

    public static String GerarSenha(String tipo){
        if(Objects.equals(tipo, "P")){
            prioridade++;
            return String.format("P%03d", prioridade);
        } else if (Objects.equals(tipo, "N")) {
            normal++;
            return String.format("N%03d", normal);
        }
        return null;
    }

    public static void EnviarSenhaParaFila(String senha, String fila) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){
            channel.queueDeclare(fila, true, false, false, null);

            String senhaPreparada = String.join(" ", senha);

            channel.basicPublish("", fila,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    senhaPreparada.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void LimparLabelSenha(Label label, Button btn_normal, Button btn_pref){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), actionEvent -> {
                    DisabilitarBotoes(btn_normal, btn_pref, false);
                    label.setText("");
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
