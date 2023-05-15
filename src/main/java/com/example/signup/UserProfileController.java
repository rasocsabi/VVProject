package com.example.signup;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileController {
    private final Connection connection;

    private final Logger logger;
    public UserProfileController() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
        logger = Logger.getLogger(UserProfileController.class.getName());
    }

    @FXML
    public Button insertButton;
    @FXML
    private Label nameLabel;

    @FXML
    private Label groupLabel;

    @FXML
    private ListView<String> skillsListView;

    @FXML
    private Button editNameButton;

    @FXML
    private Button editGroupButton;

    @FXML
    private Button addSkillButton;

    @FXML
    private Button removeSkillButton;

    @FXML
    private Button addSkillLevelButton;

    @FXML
    private Button setCostButton;

    private User user;






    private boolean isModified; // Módosítás történt-e


    public void setUser(User user) {
        this.user = user;
        user.setId(user.getIdFromDatabase()); // Az id beállítása az adatbázisból
        updateProfile();
    }

    private void updateProfile() {
        nameLabel.setText(user.getUsername());
        groupLabel.setText(user.getGroupName());
        skillsListView.setItems(user.getSkills());
        isModified = true;

    }
    @FXML
    private void handleInsertButton() {
        if (isModified) {
            try {
                updateUserInDatabase(user);
                updateGroupInDatabase(user);
                updateSkillsInDatabase(user.getSkills());
                // További adatbázis-műveletek végrehajtása szükség szerint
                logger.log(Level.INFO, "User profile updated successfull22y." + user.getId());


                Stage stage = (Stage) insertButton.getScene().getWindow();
                stage.close(); // Ablak bezárása
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error updating user profile in the database.", e);
                // Hiba kezelése
            }
        } else {
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close(); // Ablak bezárása
        }
    }
    private void updateUserInDatabase(User user) throws SQLException {
        String updateQuery = "UPDATE users SET username = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, user.getName());
        statement.setInt(2, user.getId());
        statement.executeUpdate();
    }

    private void updateGroupInDatabase(User user) throws SQLException {
        // Töröljük a felhasználó korábbi csoportjait
        String deleteGroupQuery = "DELETE FROM groupuser WHERE userid = ?";
        PreparedStatement deleteGroupStatement = connection.prepareStatement(deleteGroupQuery);
        deleteGroupStatement.setInt(1, user.getId());
        deleteGroupStatement.executeUpdate();

        // Hozzáadjuk az új csoportokat
        String insertGroupQuery = "INSERT INTO groupuser (userid, groupid) VALUES (?, ?)";
        PreparedStatement insertGroupStatement = connection.prepareStatement(insertGroupQuery);

        for (String groupName : user.getGroups()) {
            int groupId = getGroupId(groupName);
            if (groupId != -1) {
                insertGroupStatement.setInt(1, user.getId());
                insertGroupStatement.setInt(2, groupId);
                insertGroupStatement.executeUpdate();
            }
        }
    }

    private int getGroupId(String groupName) throws SQLException {
        String selectGroupIdQuery = "SELECT id FROM groups WHERE groupname = ?";
        PreparedStatement selectGroupIdStatement = connection.prepareStatement(selectGroupIdQuery);
        selectGroupIdStatement.setString(1, groupName);
        ResultSet groupIdResultSet = selectGroupIdStatement.executeQuery();

        if (groupIdResultSet.next()) {
            return groupIdResultSet.getInt("id");
        } else {
            return -1; // Az adott névvel nem találtunk csoportot az adatbázisban
        }
    }
    private void updateSkillsInDatabase(ObservableList<String> skills) throws SQLException {
        // Töröljük a felhasználó korábbi készségeit az adatbázisból
        String deleteQuery = "DELETE FROM skills WHERE username = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setString(1, user.getUsername());
        deleteStatement.executeUpdate();

        // Beszúrjuk a frissített készségeket az adatbázisba
        String insertQuery = "INSERT INTO skills (user_id,username, skill_name,skill_level) VALUES (?, ?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        for (String skill : skills) {
            insertStatement.setInt(1, user.getId());
            insertStatement.setString(2, user.getUsername());
            insertStatement.setString(3, skill);
            insertStatement.setInt(4, 1);
            insertStatement.executeUpdate();
        }
    }

    @FXML
    private void handleEditNameButton() {
        TextInputDialog dialog = new TextInputDialog(user.getUsername());
        dialog.setTitle("Edit Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new name:");
        dialog.showAndWait().ifPresent(newName -> {
            user.setName(newName);
            updateProfile();
        });
    }

    @FXML
    private void handleEditGroupButton() {
        TextInputDialog dialog = new TextInputDialog(user.getGroupName());
        dialog.setTitle("Edit Group");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new group(s), separated by commas:");

        dialog.showAndWait().ifPresent(newGroups -> {
            String[] groupNames = newGroups.split(",");
            for (String groupName : groupNames) {
                String trimmedGroupName = groupName.trim();
                if (!trimmedGroupName.isEmpty()) {
                    user.addGroup(trimmedGroupName);
                }
            }
            updateProfile();
        });
    }

    @FXML
    private void handleAddSkillButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Skill");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter skill name:");
        dialog.showAndWait().ifPresent(skillName -> {
            user.addSkill(skillName);
            updateProfile();
        });
    }

    @FXML
    private void handleRemoveSkillButton() {
        String selectedSkill = skillsListView.getSelectionModel().getSelectedItem();
        if (selectedSkill != null) {
            user.removeSkill(selectedSkill);
            updateProfile();
        }
    }

    @FXML
    private void handleAddSkillLevelButton() {
        String selectedSkill = skillsListView.getSelectionModel().getSelectedItem();
        if (selectedSkill != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Set Skill Level");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter skill level for " + selectedSkill + ":");
            dialog.showAndWait().ifPresent(skillLevel -> {
                user.setSkillLevel(selectedSkill, Integer.parseInt(skillLevel));
                updateProfile();
            });
        }
    }

    @FXML
    private void handleSetCostButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set Cost");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter cost (hourly rate):");
        dialog.showAndWait().ifPresent(cost -> {
            user.setCost(cost);
            updateProfile();
        });
    }
}
