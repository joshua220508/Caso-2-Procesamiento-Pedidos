
package com.universidad.procesamientopedidos.model;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.concurrent.Task;

public class ProcesadorPedidos extends Task<Void> {

    private static final int TOTAL_PEDIDOS = 30;
    private static final int TIEMPO_POR_PEDIDO = 200;

    private final ReadOnlyIntegerWrapper pedidosCompletados =
            new ReadOnlyIntegerWrapper(0);

    public ReadOnlyIntegerProperty pedidosCompletadosProperty() {
        return pedidosCompletados.getReadOnlyProperty();
    }

    public int getPedidosCompletados() {
        return pedidosCompletados.get();
    }

    @Override
    protected Void call() throws Exception {

        for (int i = 1; i <= TOTAL_PEDIDOS; i++) {

            if (isCancelled() || Thread.currentThread().isInterrupted()) {
                break;
            }

            try {
                Thread.sleep(TIEMPO_POR_PEDIDO);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            }

            if (isCancelled() || Thread.currentThread().isInterrupted()) {
                break;
            }

            final int completados = i;

            Platform.runLater(() -> pedidosCompletados.set(completados));

            updateProgress(completados, TOTAL_PEDIDOS);
            updateMessage("Procesando pedido " + completados
                    + " de " + TOTAL_PEDIDOS);
        }

        return null;
    }
}
