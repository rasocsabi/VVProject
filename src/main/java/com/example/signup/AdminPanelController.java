package com.example.signup;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

import static com.example.signup.RoleUtils.getUserRole;

public class AdminPanelController {
    private static final Logger LOGGER = Logger.getLogger(AdminPanelController.class.getName());
    private final ObservableList<String> userGroups = FXCollections.observableArrayList();
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
    @FXML
    private Button userProfileButton;

    public AdminPanelController() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

            User user = new User(userId, username, groupName);
            userList.add(user);
        }

        tableViewUsers.setItems(userList);
    }

    @FXML
    private void handleAddUserToGroup() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            openGroupSelectionWindow(selectedUser);

        } else {
            showWarningAlert("Invalid Selection", "Please select a user.");
        }
    }

    private void openGroupSelectionWindow(User user) {
        try {
            // Betöltjük a GroupSelection.fxml fájlt
            FXMLLoader loader = new FXMLLoader(getClass().getResource("groupselector.fxml"));
            Parent root = loader.load();

            // Létrehozzuk a GroupSelectionController példányt
            GroupSelectionController controller = loader.getController();
            controller.setUser(user);
            controller.setAdminPanelController(this);

            // Létrehozunk egy új ablakot (Stage)
            Stage stage = new Stage();
            stage.setTitle("Group Selection");
            stage.setScene(new Scene(root));

            // Megjelenítjük az ablakot
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Hiba kezelése
        }
    }

    public void assignUserToGroup(User user, String groupName) {
        try {
            // Felhasználónevének lekérése
            String username = user.getUsername();

            // Felhasználó ID lekérdezése az adatbázisból
            int userId = getUserIdByUsername(username);
            System.out.println(userId);

            // Csoport ID lekérdezése
            int groupId = getGroupId(groupName);

            if (groupId != -1) {
                // Csoport-hozzárendelés végrehajtása
                String insertQuery = "INSERT INTO groupuser (userid, groupid) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertQuery);
                statement.setInt(1, userId);
                statement.setInt(2, groupId);
                statement.executeUpdate();

                showAlert("Success", "User Added to Group", "User successfully added to the group.");
            } else {
                showWarningAlert("Invalid Group", "The selected group is invalid.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to Add User to Group", "An error occurred while adding the user to the group.");
        }
    }

    private int getUserIdByUsername(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1; // Ha nem található a felhasználó, akkor -1-et adunk vissza
    }

    private int getGroupId(String groupName) {
        try {
            String query = "SELECT id FROM groups WHERE groupname = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
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

    public void handlenewprojectButton(ActionEvent actionEvent) {
        DatabaseUtils.changeScene(actionEvent, "projectcalculator.fxml", "New Project!", null, null);
    }

    @FXML
    private void handleEditRoleButton(ActionEvent event) {
        String userRole = getUserRole(LoggedInController.getLoggedInUser());

        // Jogosultság ellenőrzése
        if (userRole != null && userRole.equals("5")) {

            try {
                // Betöltjük a role.fxml fájlt
                FXMLLoader loader = new FXMLLoader(getClass().getResource("role.fxml"));
                Parent root = loader.load();

                // Létrehozzuk a Scene-t az új színpaddal
                Scene scene = new Scene(root);

                // Hozzáadjuk a Scene-t az aktuális Stage-hez (ablakhoz)
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nincs jogosultság
            System.out.println("Nincs jogosultságod a művelet végrehajtásához.");
        }
    }
}