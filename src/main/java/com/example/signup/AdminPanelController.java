package com.example.signup;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPanelController {
    @FXML
    public ListView listViewUserGroups;
    @FXML
    TextField groupNameField;

    @FXML
    private ListView<String> listViewMyGroups;

    @FXML
    private Label Label_Name;

    @FXML
    private Button Button_CreateGroup;

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> columnUsername;

    private Connection connection;
    private int userId;

    public void initialize() throws SQLException {
        String username = LoggedInController.getLoggedInUser();
        userId = LoggedInController.getUserIdFromDatabase(username);
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

    private void loadUserGroups(User user) {
        try {
            // Felhasználó csoportjainak betöltése adatbázisból
            String query = "SELECT groupname FROM groups WHERE id IN (SELECT groupid FROM groupuser WHERE userid = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();

            ObservableList<String> userGroups = FXCollections.observableArrayList();

            // Csoportok beolvasása az eredményhalmazból
            while (resultSet.next()) {
                String groupName = resultSet.getString("groupname");
                userGroups.add(groupName);
            }

            listViewUserGroups.setItems(userGroups); // ListView frissítése a felhasználó csoportjaival
        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }
    }
    private void loadUsers() throws SQLException {
        columnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        String query = "SELECT u.username, (\n" +
                "    SELECT GROUP_CONCAT(g.groupname SEPARATOR ', ')\n" +
                "    FROM groups g\n" +
                "    WHERE g.id IN (\n" +
                "        SELECT gu.groupid\n" +
                "        FROM groupuser gu\n" +
                "        WHERE gu.userid = u.id\n" +
                "    )\n" +
                ") AS groupname\n" +
                "FROM users u\n";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<User> userList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String groupName = resultSet.getString("groupname");

            User user = new User(username, groupName);
            userList.add(user);
        }

        tableViewUsers.setItems(userList);
    }
    @FXML
    private void handleAddUserToGroup() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        String selectedGroup = groupNameField.getText();

        if (selectedUser != null && !selectedGroup.isEmpty()) {
            try {
                String query = "INSERT INTO groupuser (userid, groupid) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedUser.getId());
                statement.setString(2, selectedGroup);
                statement.executeUpdate();

                showAlert("Success", "User Added to Group", "User successfully added to the group.");

            } catch (SQLException e) {
                showAlert("Error", "Failed to Add User to Group", "An error occurred while adding the user to the group.");
                e.printStackTrace();
            }
        } else {
            showWarningAlert("Invalid Selection", "Please select a user and enter a group name.");
        }
    }

    @FXML
    private void handleRemoveUserFromGroup() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        String selectedGroup = groupNameField.getText();

        if (selectedUser != null) {
            try {
                String query = "DELETE FROM groupuser WHERE userid = ? AND groupid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedUser.getId());
                statement.setString(2, selectedGroup);
                statement.executeUpdate();

                showAlert("Success", "User Removed from Group", "User successfully removed from the group.");

            } catch (SQLException e) {
                showAlert("Error", "Failed to Remove User from Group", "An error occurred while removing the user from the group.");
                e.printStackTrace();
            }
        } else {
            showWarningAlert("No User Selected", "Please select a user from the table.");
        }
    }

    @FXML
    void handleUserProfile(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            loadUserGroups(selectedUser); // Csoportok betöltése a kiválasztott felhasználóhoz
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openUserProfile(User user) {
        // Implementáld a felhasználó profil oldalának megnyitásához szükséges logikát
        // Például navigálj az adott felhasználó profil oldalára vagy jelenítsd meg a szükséges adatokat egy új ablakban/stage-ben.
    }
}