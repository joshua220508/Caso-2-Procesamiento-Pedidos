
module com.universidad.procesamientopedidos {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.universidad.procesamientopedidos to javafx.fxml;
    opens com.universidad.procesamientopedidos.controller to javafx.fxml;

    exports com.universidad.procesamientopedidos;
}
