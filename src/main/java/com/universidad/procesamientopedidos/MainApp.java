
package com.universidad.procesamientopedidos;

import com.universidad.procesamientopedidos.controller.PedidoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("pedido-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        PedidoController controller = fxmlLoader.getController();

        stage.setTitle("Procesamiento de pedidos con cancelación");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(300);

        stage.setOnCloseRequest(event -> controller.cerrarAplicacion());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
