package com.example.vvproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.example.vvproject.LoginController.showLogin;

public class RegistrationController {

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField groupTextField;

    @FXML
    private Button registerButton;

    private LoginController loginController;

    public RegistrationController(LoginController loginController) {
        this.loginController = loginController;
    }
    public RegistrationController() {
        // empty constructor
    }

    @FXML
    public void initialize() {
        registerButton.setOnAction(e -> {
            String name = nameTextField.getText().trim();
            String password = passwordField.getText().trim();
            String group = groupTextField.getText().trim();

            if (name.isEmpty() || password.isEmpty() || group.isEmpty()) {
                // Ha valamelyik mező üres, akkor hibaüzenet jelenik meg
                LoginController.showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Hiányzó adatok", "Kérlek töltsd ki az összes mezőt!");
                return;
            }

            // Írjuk ki a felhasználó adatait a users.csv fájlba
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true))) {
                writer.write(String.format("%s,%s,%s\n", name, password, group));
                writer.flush();
            } catch (IOException ex) {
                LoginController.showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Hiba", "Hiba történt az adatok mentése közben.");
                ex.printStackTrace();
            }

            // Visszatérünk a login oldalra
            showLogin();
        });
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }


    @FXML
    private void handleRegistration() {
        String name = nameTextField.getText().trim();
        String password = passwordField.getText().trim();
        String group = groupTextField.getText().trim();

        if (name.isEmpty() || password.isEmpty() || group.isEmpty()) {
            // Ha valamelyik mező üres, akkor hibaüzenet jelenik meg
            LoginController.showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Hiányzó adatok", "Kérlek töltsd ki az összes mezőt!");
            return;
        }

        // Írjuk ki a felhasználó adatait a users.csv fájlba
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true))) {
            writer.write(String.format("%s,%s,%s\n", name, password, group));
            writer.flush();
        } catch (IOException ex) {
            LoginController.showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Hiba", "Hiba történt az adatok mentése közben.");
            ex.printStackTrace();
            return;
        }

        LoginController.showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Sikeres regisztráció", "A regisztráció sikeresen megtörtént!");

        // Visszatérünk a login oldalra
        showLogin();
    }

    @FXML
    private void handleBack() {
        // Visszatérünk a login oldalra
        showLogin();
    }
}