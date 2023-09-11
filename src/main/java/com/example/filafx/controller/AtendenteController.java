package com.example.filafx.controller;

import com.rabbitmq.client.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import javafx.util.Duration;

public class AtendenteController implements Initializable {

    @FXML
    private Button btnChamarNovaSenha;

    @FXML
    private Button btnChamarSenhaDnv;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelAcabouMensagens;

    private static final String FILA_PRIORIDADE = "fila_prioridade";
    private static final String FILA_NORMAL = "fila_normal";
    private static final String FILA_SENHAS_CHAMADAS = "fila_senhas_chamadas";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private int qntSenhaChamadas = 0;
    private String senhaAtual;

    private String guiche;
    private String titulo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        labelTitulo.setText(titulo);
    }

    private void AbrirConexao(){
        try{
            this.connection = this.factory.newConnection();
            this.channel = this.connection.createChannel();

        }catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
    }

    private void FecharConexao(){
        try {
            if(channel != null && channel.isOpen()){
                channel.close();
            }
            if (connection != null && connection.isOpen()){
                connection.close();
            }
        }catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ChamarNovaSenha(MouseEvent mouseEvent) throws InterruptedException, IOException, TimeoutException {
        AbrirConexao();

        if (channel.isOpen()) {
            channel.queueDeclare(FILA_PRIORIDADE, true, false, false, null);
            channel.queueDeclare(FILA_NORMAL, true, false, false, null);
            channel.basicQos(1);

            AMQP.Queue.DeclareOk resultNormal = channel.queueDeclarePassive(FILA_NORMAL);
            AMQP.Queue.DeclareOk resultPrioridade = channel.queueDeclarePassive(FILA_PRIORIDADE);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                this.senhaAtual = new String(delivery.getBody(), "UTF-8");

                if (channel.isOpen()) {
                    channel.queueDeclare(FILA_SENHAS_CHAMADAS, true, false, false, null);

                    SenhaAtendente senhaAtendente = new SenhaAtendente(senhaAtual, guiche);

                    Gson gson = new Gson();
                    String senhaPreparada = gson.toJson(senhaAtendente);

                    channel.basicPublish("", FILA_SENHAS_CHAMADAS,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            senhaPreparada.getBytes(StandardCharsets.UTF_8));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    FecharConexao();
                }
            };
            if(qntSenhaChamadas == 2 || (resultPrioridade.getMessageCount() > 0 && resultNormal.getMessageCount() == 0)){
                channel.basicConsume(FILA_PRIORIDADE, false, deliverCallback, consumerTag -> { });
                this.qntSenhaChamadas = 0;
                DisabilitarBotoes(btnChamarNovaSenha, btnChamarSenhaDnv, true);
                HabilitarBotoes(labelAcabouMensagens, btnChamarSenhaDnv, btnChamarNovaSenha);
            }else if(resultNormal.getMessageCount() > 0){
                channel.basicConsume(FILA_NORMAL, false, deliverCallback, consumerTag -> { });
                this.qntSenhaChamadas++;
                DisabilitarBotoes(btnChamarNovaSenha, btnChamarSenhaDnv, true);
                HabilitarBotoes(labelAcabouMensagens, btnChamarSenhaDnv, btnChamarNovaSenha);
            }else{
                labelAcabouMensagens.setText("Sem senha para chamar!");
                DisabilitarBotoes(btnChamarNovaSenha, btnChamarSenhaDnv, true);
                HabilitarBotoes(labelAcabouMensagens, btnChamarSenhaDnv, btnChamarNovaSenha);
            }
        }
    }


    @FXML
    private void ChamarSenhaDnv(MouseEvent mouseEvent) throws InterruptedException, IOException {
        if(senhaAtual != null){
            AbrirConexao();
            channel.queueDeclare(FILA_SENHAS_CHAMADAS, true, false, false, null);

            SenhaAtendente senhaAtendente = new SenhaAtendente(senhaAtual, guiche);

            Gson gson = new Gson();
            String senhaPreparada = gson.toJson(senhaAtendente);

            channel.basicPublish("", FILA_SENHAS_CHAMADAS,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    senhaPreparada.getBytes(StandardCharsets.UTF_8));
            FecharConexao();

            DisabilitarBotoes(btnChamarNovaSenha, btnChamarSenhaDnv, true);
            HabilitarBotoes(labelAcabouMensagens, btnChamarSenhaDnv, btnChamarNovaSenha);
        }
    }

    public static void HabilitarBotoes(Label labelSemMenssagem, Button btnChamarSenhaDnv, Button btnChamarNovaSenha){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), actionEvent -> {
                    DisabilitarBotoes(btnChamarNovaSenha, btnChamarSenhaDnv, false);
                    labelSemMenssagem.setText("");
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private static void DisabilitarBotoes(Button btnChamarNovaSenha, Button btnChamarSenhaDnv, boolean isDisable) {
        btnChamarNovaSenha.setDisable(isDisable);
        btnChamarSenhaDnv.setDisable(isDisable);
    }

    public void setGuiche(String guiche){
        this.guiche = guiche;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
}
