package com.example.signup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.signup.RoleUtils.getUserRole;

public class LoggedInController implements Initializable {
    public static int userId;
    private static String loggedInUser;
    @FXML
    private Button Button_Logout;
    @FXML
    private Label Label_Welcome;
    @FXML
    private Label Label_Status;
    @FXML
    private Button Button_myprofile;
    @FXML
    private Button Button_mygroups;
    @FXML
    private Button Button_adminpanel;

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(String loggedInUser) {
        LoggedInController.loggedInUser = loggedInUser;
    }

    static int getUserIdFromDatabase(String username) {

        int userId = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button_Logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.changeScene(event, "log-in.fxml", "Log in!", null, null);
            }
        });
        Button_myprofile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.changeScene(event, "myprofile.fxml", "My Profile!", null, null);
            }
        });
        Button_mygroups.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.changeScene(event, "mygroups.fxml", "My Groups!", null, null);
            }
        });
        Button_adminpanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String userRole = getUserRole(LoggedInController.getLoggedInUser());

                // Jogosultság ellenőrzése
                if (userRole != null && userRole.equals("5")) {
                    DatabaseUtils.changeScene(event, "adminpanel.fxml", "Admin Panel!", null, null);
                } else {
                    DatabaseUtils.showAlert("Nincs jogosultságod az oldal megtekintéséhez!");
                }

            }
        });

    }

    public void setStatusInformation(String username, String status) {

        loggedInUser = username;
        Label_Welcome.setText("Hello" + username + "!");
        Label_Status.setText("You have successfully entered, where to next?");

    }


}
