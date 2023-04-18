package com.example.vvproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.example.vvproject.LoginController.showAlert;

public class AdminController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button usersButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button editUsersButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane userTablePane;
    @FXML
    private AnchorPane deleteUserPane;
    @FXML
    private AnchorPane editProfilePane;
    @FXML
    private AnchorPane editUsersPane;

    private List<User> users;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // inicializálás
        users = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        // az adatok betöltése az users.csv fájlból
        File file = new File("users.csv");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                User user = new User(parts[0], parts[1], parts[2]);
                users.add(user);
            }
        } catch (IOException e) {
            System.err.println("Hiba: A fájl nem található!");
        }
    }

    private void saveData() {
        // az adatok mentése az users.csv fájlba
        try (FileWriter writer = new FileWriter("users.csv")) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getGroup() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Hiba: Az adatok mentése sikertelen!");
        }
    }

    @FXML
    private void onUsersButtonClicked(ActionEvent event) {
        // Az összes felhasználó megjelenítése
        userTablePane.getChildren().clear();
        int rowIndex = 0;
        for (User user : users) {
            Label usernameLabel = new Label(user.getUsername());
            usernameLabel.setLayoutX(20);
            usernameLabel.setLayoutY(rowIndex * 30 + 10);

            TextField groupField = new TextField(user.getGroup());
            groupField.setLayoutX(200);
            groupField.setLayoutY(rowIndex * 30 + 5);

            Button saveButton = new Button("Mentés");
            saveButton.setLayoutX(300);
            saveButton.setLayoutY(rowIndex * 30 + 5);
            saveButton.setOnAction(e -> {
                user.setGroup(groupField.getText());
                saveData();
                messageLabel.setText("Az adatok mentése sikeres!");
            });

            userTablePane.getChildren().addAll(usernameLabel, groupField, saveButton);
            rowIndex++;
        }
    }

    @FXML
    private void onDeleteUserButtonClicked(ActionEvent event) {
        // Az összes felhasználó betöltése
        loadData();

        // Az összes felhasználónév lekérdezése
        List<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }

        // Az Alert ablak konfigurálása a felhasználónevek kiválasztásához
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", usernames);
        dialog.setTitle("Felhasználó törlése");
        dialog.setHeaderText("Válassza ki a törölni kívánt felhasználót:");
        dialog.setContentText("Felhasználó:");

        // A felhasználó által kiválasztott felhasználónév lekérdezése
        Optional<String> result = dialog.showAndWait();

        // Ha a felhasználó kiválasztott egy felhasználót, akkor töröljük azt és frissítjük a felhasználók listáját
        result.ifPresent(username -> {
            User userToDelete = null;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    userToDelete = user;
                    break;
                }
            }
            if (userToDelete != null) {
                users.remove(userToDelete);
                saveData();
                refreshUserList();
                showAlert(Alert.AlertType.INFORMATION, "Felhasználó törlése", "Sikeresen törölve", "A felhasználó sikeresen törölve lett.");
            }
        });
    }
    private void refreshUserList() {
        users.clear();
        loadData();
        onUsersButtonClicked(null);
    }




    @FXML
    private void onEditProfileButtonClicked(ActionEvent event) {
        // A profil szerkesztése gombra kattintva megjelenítjük a bejelentkezett felhasználó adatait
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/editprofile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            EditProfileController controller = loader.getController();
            controller.setUser(LoginController.getLoggedInUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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
            controller.setUsers(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}