package com.example.filafx.controller;

import com.rabbitmq.client.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public class AtendenteController implements Initializable {

    @FXML
    private Button btnChamarNovaSenha;

    @FXML
    private Button btnChamarSenhaDnv;

    private static final String FILA_PRIORIDADE = "fila_prioridade";
    private static final String FILA_NORMAL = "fila_normal";
    private static final String FILA_SENHAS_CHAMADAS = "fila_senhas_chamadas";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private int qntSenha = 0;
    private String senhaAtual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
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
            if(qntSenha == 2){
                channel.queueDeclare(FILA_PRIORIDADE, true, false, false, null);
                channel.basicQos(1);
            }else{
                channel.queueDeclare(FILA_NORMAL, true, false, false, null);
                channel.basicQos(1);
            }

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                this.senhaAtual = new String(delivery.getBody(), "UTF-8");

                if (channel.isOpen()) {
                    channel.queueDeclare(FILA_SENHAS_CHAMADAS, true, false, false, null);

                    String senhaPreparada = String.join(" ", senhaAtual);

                    channel.basicPublish("", FILA_SENHAS_CHAMADAS,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            senhaPreparada.getBytes(StandardCharsets.UTF_8));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    FecharConexao();
                }
            };
            if(qntSenha == 2){
                channel.basicConsume(FILA_PRIORIDADE, false, deliverCallback, consumerTag -> { });
                this.qntSenha = 0;
            }else {
                channel.basicConsume(FILA_NORMAL, false, deliverCallback, consumerTag -> { });
                this.qntSenha++;
            }
        }
    }


    @FXML
    private void ChamarSenhaDnv(MouseEvent mouseEvent) throws InterruptedException, IOException {
        AbrirConexao();
        channel.queueDeclare(FILA_SENHAS_CHAMADAS, true, false, false, null);

        String senhaPreparada = String.join(" ", senhaAtual);

        channel.basicPublish("", FILA_SENHAS_CHAMADAS,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                senhaPreparada.getBytes(StandardCharsets.UTF_8));
        FecharConexao();
    }
}
