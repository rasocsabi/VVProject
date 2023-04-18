package com.example.vvproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    private static final String fileName = "users.csv";

    @FXML
    private static TextField usernameField;

    @FXML
    private static PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    public static void showAlert(Alert.AlertType information, String felhasználó_törlése, String message, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static User getLoggedInUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    private User getUser(String username) {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Stage stage = (Stage) registrationPane.getScene().getWindow();
            // stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        // Ha a fájl nem létezik, létrehozzuk üresen
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void onLoginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (authenticate(username, password)) {
            User loggedInUser = getUser(username);
            // Sikeres bejelentkezés esetén lekérdezzük a felhasználó csoportját
            String group = getUserGroup(username);
            if (group.equals("1")) {
                // Ha a csoport 1, átirányítjuk az admin felületre
                messageLabel.setText("Sikeres bejelentkezés!");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/admin.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                pause.play();
            } else if (group.equals("2")) {
                // Ha a csoport 2, átirányítjuk a felhasználói felületre
                messageLabel.setText("Sikeres bejelentkezés!");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vvproject/user.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                pause.play();
            }
        } else {
            // Ha a felhasználónév vagy jelszó helytelen, kiírjuk, majd várunk 3 másodpercet és átirányítjuk a regisztrációs felületre
            messageLabel.setText("Sikertelen bejelentkezés!");
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> {
            });
            pause.play();
        }
    }

    private String getUserGroup(String username) {
        String group = "";
        try {
            File file = new File("users.csv");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields[0].equals(username)) {
                    group = fields[2];
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return group;
    }

    public boolean authenticate(String username, String password) {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    public void addUser(String username, String password, String group) {
        User user = new User(username, password, group);
        List<User> users = readUsersFromFile();
        users.add(user);
        writeUsersToFile(users);
    }

    private static List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length != 3) {
                    continue;
                }
                User user = new User(parts[0], parts[1], parts[2]);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void writeUsersToFile(List<User> users) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getGroup() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private RegistrationController registrationController;

    @FXML
    private void handleRegistration(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/vvproject/registration.fxml"));
        Parent registrationRoot = fxmlLoader.load();
        registrationController = fxmlLoader.getController();
        registrationController.setLoginController(this); // itt allitjuk be a LoginController-t
        Scene registrationScene = new Scene(registrationRoot);
        Stage registrationStage = new Stage();
        registrationStage.setScene(registrationScene);
        registrationStage.show();
    }

    static class User {

        private String username;
        private String password;
        private String group;

        public User(String username, String password, String group) {
            this.username = username;
            this.password = password;
            this.group = group;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getGroup() {
            return group;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) { //ellenőrizze a felhasználói adatokat
            // Belépteti a felhasználót, és megjeleníti a fő alkalmazásablakot
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setLoginController(this);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            // Hibaüzenet jelenik meg, ha a felhasználónév vagy a jelszó helytelen
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!");
            alert.showAndWait();
        }
    }

}