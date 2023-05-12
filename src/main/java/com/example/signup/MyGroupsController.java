package com.example.signup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyGroupsController {
    @FXML
    private ListView<String> listViewMyGroups;

    @FXML
    private Label Label_Name;


    private Connection connection;
    private int userId; // Az osztályszintű változó

    public void initialize() throws SQLException {
        // A userId közvetlen hozzáférése az osztályszintű változóhoz
        userId = LoggedInController.userId;
        loadUserGroups();
        Label_Name.setText(LoggedInController.getLoggedInUser());
    }

    public MyGroupsController() {
        // Inicializálás, adatbázis kapcsolat létrehozása
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserId(String username) throws SQLException {
        // Nem szükséges a this.username beállítása
        loadUserGroups();
    }

    private void loadUserGroups() throws SQLException {
        List<String> userGroups = new ArrayList<>();
        String useridsql ="SELECT id FROM users WHERE username = ?";
        PreparedStatement useridstatement = connection.prepareStatement(useridsql);
        useridstatement.setString(1, LoggedInController.getLoggedInUser());

        try {
            // Ellenőrizze, hogy a felhasználó admin vagy csoportvezető-e
            PreparedStatement statement = connection.prepareStatement("SELECT role FROM users WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int role = resultSet.getInt("role");

                // Admin vagy csoportvezető
                if (role == 3 || role == 2) {
                    // Lekérdezze a felhasználó csoportjait
                    String query = "SELECT groups.groupname, users.username " +
                            "FROM groupuser " +
                            "INNER JOIN groups ON groupuser.groupid = groups.id " +
                            "INNER JOIN users ON groups.groupleaderid = users.id " +
                            "WHERE groupuser.userid = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, userId);
                    resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        String groupName = resultSet.getString("groupname");
                        String groupLeader = resultSet.getString("username");
                        userGroups.add(groupName + " (Group Leader: " + groupLeader + ")");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Listaelemek hozzáadása a ListView-hoz
        listViewMyGroups.getItems().addAll(userGroups);
        listViewMyGroups.setCellFactory(TextFieldListCell.forListView());
    }
}