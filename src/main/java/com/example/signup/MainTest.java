package com.example.signup;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainTest extends ApplicationTest {

    private AtomicBoolean stageShown = new AtomicBoolean(false);

    @Override
    public void start(Stage stage) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("log-in.fxml")));
            stage.setTitle("Log in!");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

            stageShown.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testStart() {
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();
            start(primaryStage);

            Assertions.assertTrue(stageShown.get());
            Assertions.assertEquals("Log in!", primaryStage.getTitle());
            Assertions.assertNotNull(primaryStage.getScene());
            Assertions.assertNotNull(primaryStage.getScene().getRoot());
        });
    }
}
