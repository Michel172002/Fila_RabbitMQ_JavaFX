package com.example.filafx.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PainelController implements Initializable {
    @FXML
    private Label guicheAtual;
    @FXML
    private Label guicheAnterior1;
    @FXML
    private Label guicheAnterior2;
    @FXML
    private Label guicheAnterior3;
    @FXML
    private Label senhaAtual;
    @FXML
    private Label senhaAnterior1;
    @FXML
    private Label senhaAnterior2;
    @FXML
    private Label senhaAnterior3;

    private LinkedList<SenhaAtendente> senhasChamadas = new LinkedList<>();
    private int tamanhoMaximo = 4;

    private static final String FILA_SENHAS_CHAMADAS = "fila_senhas_chamadas";

    private static final int ATUALIZACAO_INTERVALO_SEGUNDOS = 2; // Intervalo de atualização em segundos
    private ScheduledExecutorService executorService;

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            abrirConexao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        executorService = Executors.newSingleThreadScheduledExecutor();

        // Agende a tarefa de atualização para ser executada em intervalos regulares
        executorService.scheduleAtFixedRate(this::atualizarLabel, 0, ATUALIZACAO_INTERVALO_SEGUNDOS, TimeUnit.SECONDS);
    }

    private void abrirConexao() throws Exception {
        if (connection == null || !connection.isOpen()) {
            connection = factory.newConnection();
            channel = connection.createChannel();
        }
    }

    private void fecharConexao() throws Exception {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    private void pegarMensagens() throws Exception {
        channel.queueDeclare(FILA_SENHAS_CHAMADAS, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String senhaAtendenteJson = new String(delivery.getBody(), StandardCharsets.UTF_8);

            Gson gson = new Gson();
            SenhaAtendente senhaAtendente = gson.fromJson(senhaAtendenteJson, SenhaAtendente.class);

            if (senhasChamadas.size() >= tamanhoMaximo) {
                senhasChamadas.removeLast();
            }
            senhasChamadas.addFirst(senhaAtendente);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(FILA_SENHAS_CHAMADAS, false, deliverCallback, consumerTag -> {
        });
    }

    private void atualizarLabel() {
        try {
            pegarMensagens();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            senhaAtual.setText(!senhasChamadas.isEmpty() ? senhasChamadas.getFirst().getSenha() : "");
            senhaAnterior1.setText(senhasChamadas.size() > 1 ? senhasChamadas.get(1).getSenha() : "");
            senhaAnterior2.setText(senhasChamadas.size() > 2 ? senhasChamadas.get(2).getSenha() : "");
            senhaAnterior3.setText(senhasChamadas.size() > 3 ? senhasChamadas.getLast().getSenha() : "");

            guicheAtual.setText(!senhasChamadas.isEmpty() ? senhasChamadas.getFirst().getGuiche() : "");
            guicheAnterior1.setText(senhasChamadas.size() > 1 ? senhasChamadas.get(1).getGuiche() : "");
            guicheAnterior2.setText(senhasChamadas.size() > 2 ? senhasChamadas.get(2).getGuiche() : "");
            guicheAnterior3.setText(senhasChamadas.size() > 3 ? senhasChamadas.getLast().getGuiche() : "");
        });
    }
}
