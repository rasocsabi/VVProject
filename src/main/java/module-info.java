module com.example.vvproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.vvproject to javafx.fxml;
    exports com.example.vvproject;
}