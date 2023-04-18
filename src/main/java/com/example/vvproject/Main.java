package com.example.vvproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Betöltjük a fxml fájlt és az azt tartalmazó konténert
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/vvproject/login.fxml")));

        // Létrehozzuk a Scene-t és hozzáadjuk a konténerünket
        Scene scene = new Scene(root);

        // Beállítjuk a Scene-t a Stage-re és megjelenítjük
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bejelentkezés");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}