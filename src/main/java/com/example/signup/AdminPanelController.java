package com.example.signup;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPanelController {
    @FXML
    TextField groupNameField;

    @FXML
    private ListView<String> listViewMyGroups;

    @FXML
    private Label Label_Name;

    @FXML
    private Button Button_AddUserToGroup;

    @FXML
    private Button Button_RemoveUserFromGroup;

    @FXML
    private Button Button_CreateGroup;

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> columnUsername;

    @FXML
    private TableColumn<User, String> columnRole;

    private Connection connection;
    private int userId;

    public void initialize() throws SQLException {
        String username = LoggedInController.getLoggedInUser();
        userId = LoggedInController.getUserIdFromDatabase(username);
        loadUserGroups();
        loadUsers();
        Label_Name.setText(username);
    }

    public AdminPanelController() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserGroups() throws SQLException {
        List<String> userGroups = new ArrayList<>();

        // SQL lekérdezés a csoportok betöltéséhez
        String query = "SELECT groupname FROM groups";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Csoportok beolvasása az eredményhalmazból
            while (resultSet.next()) {
                String groupName = resultSet.getString("groupname");
                userGroups.add(groupName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        // Az eredmények megjelenítése vagy feldolgozása
        for (String group : userGroups) {
            System.out.println(group);
        }


}

    private void loadUsers() throws SQLException {
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        String query = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String role = resultSet.getInt("role") > 2 ? "Admin" : "User";
            User user = new User(username, role);
            tableViewUsers.getItems().add(user);
        }
    }

    @FXML
    private void handleAddUserToGroup() {
        // Kód a felhasználó hozzáadásához a csoportban
    }

    @FXML
    private void handleRemoveUserFromGroup() {
        // Kód a felhasználó eltávolításához a csoportból
    }

    @FXML
    private void handleCreateGroup() {
        String groupName = groupNameField.getText();

        // Ellenőrzés, hogy a név mező nem üres
        if (groupName.isEmpty()) {
            showAlert("Hiba", "Hiányzó adat", "Kérlek add meg a csoport nevét.");
            return;
        }

        try {
            // Új csoport létrehozása a groupName felhasználásával
            String query = "INSERT INTO groups (groupname) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupName);
            statement.executeUpdate();


            // Sikeres létrehozás esetén üzenet megjelenítése
            showAlert("Siker", "Csoport létrehozva", "Az új csoport sikeresen létrejött.");
        } catch (Exception e) {
            // Hiba esetén hibaüzenet megjelenítése
            showAlert("Hiba", "Csoport létrehozása sikertelen", "Hiba történt a csoport létrehozása közben.");
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

