package com.example.signup;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
    public Label groupLeaderLabel;
   @FXML
   public ListView groupMembersListView;
    @FXML
    private ListView<String> listViewMyGroups;



    @FXML
    private Label Label_Name;


    private Connection connection;
    private int userId; // Az osztályszintű változó

    public void initialize() throws SQLException {
        // A userId közvetlen hozzáférése az osztályszintű változóhoz

        String username = LoggedInController.getLoggedInUser();
        userId = LoggedInController.getUserIdFromDatabase(username);
        loadUserGroups();
        Label_Name.setText(username);
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        DatabaseUtils.changeScene(event, "logged-in.fxml", "Logged in!", null, null);
    }
    public MyGroupsController() {
        // Inicializálás, adatbázis kapcsolat létrehozása
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    listViewMyGroups.setOnMouseClicked(event -> {
                        String selectedGroup = listViewMyGroups.getSelectionModel().getSelectedItem();
                        if (selectedGroup != null) {
                            // Csoport nevének kinyerése a kiválasztott elemből
                            String groupName = selectedGroup.split(" ")[0];

                            // Emberek kilistázása a csoportból
                            List<String> groupMembers;
                            try {
                                groupMembers = getGroupMembers(groupName);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            // A csoport tagjainak listájának megjelenítése egy másik ListView-ban
                            groupMembersListView.setItems(FXCollections.observableArrayList(groupMembers));


                            // Csoportvezető kinyerése és megjelenítése
                            String groupLeader;
                            try {
                                groupLeader = getGroupLeader(groupName);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            groupLeaderLabel.setText("Group Leader: " + groupLeader);
                            // Itt hajtsd végre a megfelelő megjelenítési műveleteket
                        }
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Listaelemek hozzáadása a ListView-hoz
        listViewMyGroups.getItems().addAll(userGroups);
        listViewMyGroups.setCellFactory(TextFieldListCell.forListView());
    }
    private List<String> getGroupMembers(String groupName) throws SQLException {
        List<String> groupMembers = new ArrayList<>();
        String query = "SELECT users.username " +
                "FROM groupuser " +
                "INNER JOIN users ON groupuser.userid = users.id " +
                "INNER JOIN groups ON groupuser.groupid = groups.id " +
                "WHERE groups.groupname = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, groupName);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String memberName = resultSet.getString("username");
            groupMembers.add(memberName);
        }
        return groupMembers;
    }

    private String getGroupLeader(String groupName) throws SQLException {
        String groupLeader = "";
        String query = "SELECT users.username " +
                "FROM groups " +
                "INNER JOIN users ON groups.groupleaderid = users.id " +
                "WHERE groups.groupname = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, groupName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            groupLeader = resultSet.getString("username");
        }
        return groupLeader;
    }
}
