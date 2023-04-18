package com.example.vvproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private User user;

    @FXML
    private void initialize() {
        // üres metódus
    }

    public void setUser(User user) {
        this.user = user;
        // felhasználónév és jelszó mezők inicializálása
        usernameField.setText(user.getName());
        passwordField.setText(user.getPassword());
    }

    @FXML
    private void onSaveButtonClicked() {
        // felhasználónév és jelszó mentése
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        // felhasználó mentése a users.csv fájlba
        User.updateUserInCsv(user);
        // üzenet megjelenítése a felhasználónak
        showAlert(Alert.AlertType.INFORMATION, "Profil frissítése", "Sikeres frissítés", "A profilod sikeresen frissült.");
        // ablak bezárása
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClicked() {
        // ablak bezárása
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void setUser(LoginController.User loggedInUser) {
    }
}