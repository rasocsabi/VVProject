package com.example.vvproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private final List<User> userList = new ArrayList<>();
    public void initialize() {
        // userList feltöltése a megfelelő adatokkal
    }
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    void onLoginButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hiba történt", "A bejelentkezési ablak megnyitása nem sikerült.");
        }
    }

    @FXML
    void onRegisterButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/registration.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hiba történt", "A regisztrációs ablak megnyitása nem sikerült.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }



    private void onEditUsersButtonClicked(ActionEvent event) {
        // A felhasználók szerkesztése gombra kattintva megjelenítjük az összes felhasználót és a hozzájuk tartozó szerkesztés gombot
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/editusers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            EditUsersController controller = loader.getController();
           // controller.setUsers(userList.getUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/edituser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            EditUserController controller = loader.getController();
            controller.setUser(user);
            UserListController userListController = null;
            controller.setUserListController(userListController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}