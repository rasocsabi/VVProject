package com.example.signup;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

public class AdminPanelController {
    @FXML
    public ListView<String> listViewUserGroups;
    @FXML
    TextField groupNameField;



    @FXML
    private Label Label_Name;



    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> columnUsername;

    private Connection connection;
    private int userId;

    private static final Logger LOGGER = Logger.getLogger(AdminPanelController.class.getName());

    @FXML
    private Button userProfileButton;
    private ObservableList<String> userGroups = FXCollections.observableArrayList();
    public void initialize() throws SQLException {
        String username = LoggedInController.getLoggedInUser();
        userId = LoggedInController.getUserIdFromDatabase(username);
        loadUsers();
        Label_Name.setText(username);
        tableViewUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadUserGroups(newValue);
            }
        });
        userProfileButton.setOnAction(event -> {
            User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                openUserProfile(selectedUser);
            }
        });
    }

    public AdminPanelController() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        DatabaseUtils.changeScene(event, "logged-in.fxml", "Logged in!", null, null);
    }


    private void loadUserGroups(User user) {
        try {
            userGroups.clear(); // Előző csoportok törlése

            // Felhasználó csoportjainak betöltése adatbázisból
            String query = "SELECT g.groupname FROM groups g JOIN groupuser gu ON g.id = gu.groupid JOIN users u ON gu.userid = u.id WHERE u.username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            ResultSet resultSet = statement.executeQuery();

            // Csoportok beolvasása az eredményhalmazból és hozzáadása az ObservableList-hez
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

            User user = new User(userId,username, groupName);
            userList.add(user);
        }

        tableViewUsers.setItems(userList);
    }
    @FXML
    private void handleAddUserToGroup() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        String selectedGroup = listViewUserGroups.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try {
                // Keresés az id alapján a users táblában
                String userIdQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userIdStatement = connection.prepareStatement(userIdQuery);
                userIdStatement.setString(1, selectedUser.getUsername());
                ResultSet userIdResultSet = userIdStatement.executeQuery();

                if (userIdResultSet.next()) {
                    int userId = userIdResultSet.getInt("id");

                    // Csoport hozzáadása a groupuser táblához
                    String groupIdQuery = "SELECT id FROM groups WHERE groupname = ?";
                    PreparedStatement groupIdStatement = connection.prepareStatement(groupIdQuery);
                    groupIdStatement.setString(1, selectedGroup);
                    ResultSet groupIdResultSet = groupIdStatement.executeQuery();

                    if (groupIdResultSet.next()) {
                        int groupId = groupIdResultSet.getInt("id");

                        String insertQuery = "INSERT INTO groupuser (userid, groupid) VALUES (?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setInt(1, userId);
                        insertStatement.setInt(2, groupId);
                        insertStatement.executeUpdate();

                        showAlert("Success", "User Added to Group", "User successfully added to the group.");
                    } else {
                        showWarningAlert("Invalid Group", "The selected group is invalid.");
                    }
                } else {
                    showWarningAlert("User Not Found", "The selected user does not exist.");
                }
            } catch (SQLException e) {
                showAlert("Error", "Failed to Add User to Group", "An error occurred while adding the user to the group.");
                e.printStackTrace();
            }
        } else {
            showWarningAlert("Invalid Selection", "Please select a user and a group.");
        }
    }

    @FXML
    private void handleRemoveUserFromGroup() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        String selectedGroup = listViewUserGroups.getSelectionModel().getSelectedItem();

        if (selectedUser != null && selectedGroup != null) {
            try {
                // Felhasználó id-jának lekérdezése
                String userIdQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userIdStatement = connection.prepareStatement(userIdQuery);
                userIdStatement.setString(1, selectedUser.getUsername());
                ResultSet userIdResultSet = userIdStatement.executeQuery();

                if (userIdResultSet.next()) {
                    int userId = userIdResultSet.getInt("id");

                    // Csoport id-jának lekérdezése
                    String groupIdQuery = "SELECT id FROM groups WHERE groupname = ?";
                    PreparedStatement groupIdStatement = connection.prepareStatement(groupIdQuery);
                    groupIdStatement.setString(1, selectedGroup);
                    ResultSet groupIdResultSet = groupIdStatement.executeQuery();

                    if (groupIdResultSet.next()) {
                        int groupId = groupIdResultSet.getInt("id");

                        // Felhasználó eltávolítása a groupuser táblából
                        String deleteQuery = "DELETE FROM groupuser WHERE userid = ? AND groupid = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, userId);
                        deleteStatement.setInt(2, groupId);
                        deleteStatement.executeUpdate();

                        showAlert("Success", "User Removed from Group", "User successfully removed from the group.");
                    } else {
                        showWarningAlert("Invalid Group", "The selected group is invalid.");
                    }
                } else {
                    showWarningAlert("User Not Found", "The selected user does not exist.");
                }
            } catch (SQLException e) {
                showAlert("Error", "Failed to Remove User from Group", "An error occurred while removing the user from the group.");
                e.printStackTrace();
            }
        } else {
            showWarningAlert("Invalid Selection", "Please select a user and a group.");
        }
    }

    @FXML
    private void handleUserProfile(ActionEvent event) {
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
        // Tegyük fel, hogy van egy UserProfile.fxml fájl, amely a felhasználó profiljának megjelenítését definiálja
        // és van egy UserProfileController osztály, amely vezérli a profil megjelenítését

        try {
            // Betöltjük a UserProfile.fxml fájlt
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userprofil.fxml"));
            Parent root = loader.load();

            // Létrehozzuk a UserProfileController példányt és átadjuk a kiválasztott felhasználót
            UserProfileController controller = loader.getController();
            controller.setUser(user);

            // Létrehozunk egy új ablakot (Stage)
            Stage stage = new Stage();
            stage.setTitle("User Profile");
            stage.setScene(new Scene(root));

            // Megjelenítjük az ablakot
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Hiba kezelése
        }
    }
}