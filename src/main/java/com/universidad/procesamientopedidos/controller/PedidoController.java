package com.universidad.procesamientopedidos.controller;

import com.universidad.procesamientopedidos.model.ProcesadorPedidos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PedidoController {
    private static final int TOTAL_PEDIDOS = 30;

    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ProgressBar barraProgreso;
    @FXML
    private Label lblContador;
    @FXML
    private Label lblEstado;
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();
    private ProcesadorPedidos tareaActual;
    private boolean cerrandoAplicacion = false;

    @FXML
    private void initialize() {
        btnIniciar.setDisable(false);
        btnCancelar.setDisable(true);

        barraProgreso.setProgress(0);
        lblContador.setText("Pedidos completados: 0/30");
        lblEstado.setText("Estado: Listo");
    }

    @FXML
    private void iniciarProcesamiento() {
        if (cerrandoAplicacion || tareaActual != null) {
            return;
        }

        barraProgreso.setProgress(0);
        lblContador.setText("Pedidos completados: 0/30");
        lblEstado.setText("Estado: En ejecución");

        btnIniciar.setDisable(true);
        btnCancelar.setDisable(false);

        ProcesadorPedidos nuevaTarea = new ProcesadorPedidos();
        tareaActual = nuevaTarea;

        nuevaTarea.pedidosCompletadosProperty().addListener(
                (observable, anterior, actual) -> {
                    int completados = actual.intValue();

                    lblContador.setText(
                            "Pedidos completados: "
                                    + completados + "/" + TOTAL_PEDIDOS
                    );

                    barraProgreso.setProgress(
                            (double) completados / TOTAL_PEDIDOS
                    );
                }
        );

        executor.execute(() -> {
            try {
                nuevaTarea.run();
            } finally {
                Platform.runLater(() -> finalizarProcesamiento(nuevaTarea));
            }
        });
    }

    @FXML
    private void cancelarProcesamiento() {
        if (tareaActual == null || cerrandoAplicacion) {
            return;
        }

        lblEstado.setText("Estado: Cancelando");
        btnCancelar.setDisable(true);
        btnIniciar.setDisable(true);

        tareaActual.cancel(true);
    }

    private void finalizarProcesamiento(ProcesadorPedidos tarea) {
        if (tarea != tareaActual) {
            return;
        }

        if (cerrandoAplicacion) {
            return;
        }

        if (tarea.isCancelled()) {
            lblEstado.setText("Estado: Cancelado");
        } else if (tarea.getException() != null) {
            lblEstado.setText("Estado: Error");
        } else {
            barraProgreso.setProgress(1);
            lblContador.setText("Pedidos completados: "
                    + TOTAL_PEDIDOS + "/" + TOTAL_PEDIDOS);
            lblEstado.setText("Estado: Completado");
        }

        tareaActual = null;

        btnIniciar.setDisable(false);
        btnCancelar.setDisable(true);
    }

    public void cerrarAplicacion() {
        cerrandoAplicacion = true;

        if (tareaActual != null) {
            tareaActual.cancel(true);
        }

        executor.shutdownNow();
    }
}