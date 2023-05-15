package com.example.signup;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class ProfileController implements Initializable {

    public Button Button_InsertToList;
    @FXML
    public Button Button_Back;
    @FXML
    private Button Button_AddSkill;

    @FXML
    private Button Button_EditProfile;

    @FXML
    private Label Label_Name;

    @FXML
    private Label Label_Skills;

    @FXML
    private ListView<String> ListView_Skills;

    @FXML
    private TextField TextField_NewSkill;


    private String username;
    private ObservableList<String> skills;

    @FXML
    private Button Button_DeleteSkill;

    @FXML
    private void handleBackButton(ActionEvent event) {
        DatabaseUtils.changeScene(event, "logged-in.fxml", "Logged in!", null, null);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Adatbáziskapcsolat létrehozása
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");

            // SQL lekérdezés előkészítése
            PreparedStatement stmt = conn.prepareStatement("SELECT skill_name FROM skills WHERE username = ?");
            stmt.setString(1, LoggedInController.getLoggedInUser());

            // Lekérdezés végrehajtása és eredmények feldolgozása
            ResultSet rs = stmt.executeQuery();
            skills = FXCollections.observableArrayList();
            while (rs.next()) {
                skills.add(rs.getString("skill_name"));
            }

            // Kapcsolat bezárása
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        username= LoggedInController.getLoggedInUser();

        // A név beállítása a Label_Name-en
        Label_Name.setText(username);

        // A ListView_Skills feltöltése a skills lista elemeivel
        ListView_Skills.setItems(skills);

        // A Button_AddSkill onClick eseményének beállítása
        Button_AddSkill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Új skill hozzáadása a skills listához
                String newSkill = TextField_NewSkill.getText();
                if (!newSkill.isEmpty()) {
                    skills.add(newSkill);
                    TextField_NewSkill.clear();
                }
            }
        });
        // A Button_DeleteSkill onClick eseményének beállítása
        Button_DeleteSkill.setOnAction(this::handleDeleteSkillAction);
        // A Button_InsertToList onClick eseményének beállítása
        Button_InsertToList.setOnAction(this::handleAddSkillAction);
        // A Button_EditProfile onClick eseményének beállítása
        Button_EditProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO: Megnyitni a profil szerkesztése ablakot
            }
        });
    }


    public void setUsername(String username) {
        this.username = username;
        Label_Name.setText(username);
        updateSkillsList();
    }

    @FXML
    void addSkill(ActionEvent event) {
        String skillName = TextField_NewSkill.getText();
        if (skillName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Skill name cannot be empty!");
            alert.showAndWait();
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            statement = connection.prepareStatement("INSERT INTO skills (username, skill_name) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, skillName);
            statement.executeUpdate();
            updateSkillsList();
            TextField_NewSkill.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateSkillsList() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            statement = connection.prepareStatement("SELECT * FROM skills WHERE username = ?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            List<String> skillsList = new ArrayList<>();
            while (resultSet.next()) {
                skillsList.add(resultSet.getString("skill_name"));

            }
            ObservableList<String> observableList = FXCollections.observableList(skillsList);
            ListView_Skills.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void editProfile(ActionEvent event) {
        // TODO: Implement profile editing functionality
    }

    @FXML
    private void handleAddSkillAction(ActionEvent event) {
        String newSkillName = TextField_NewSkill.getText().trim();
        if (!newSkillName.isEmpty()) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO skills (user_id, username, skill_name, skill_level) VALUES (?, ?, ?, ?)")) {

                // Felhasználó ID lekérdezése
                String query = "SELECT id FROM users WHERE username = ?";
                PreparedStatement getUserIdStatement = connection.prepareStatement(query);
                getUserIdStatement.setString(1, username);
                ResultSet resultSet = getUserIdStatement.executeQuery();
                int userId = 0;
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
                resultSet.close();
                getUserIdStatement.close();

                // Paraméterek beállítása
                statement.setInt(1, userId);
                statement.setString(2, username);
                statement.setString(3, newSkillName);
                statement.setInt(4, 1);

                // Beszúrás végrehajtása
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    // Sikeres hozzáadás esetén frissítjük a ListView-t
                    updateSkillsList();
                    TextField_NewSkill.clear();
                } else {
                    // Hibaüzenet
                    System.out.println("Failed to add skill.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleInsertToListAction(ActionEvent event) {

        handleAddSkillAction(null);
    }
    @FXML
    void handleDeleteSkillAction(ActionEvent event) {
        String selectedSkill = ListView_Skills.getSelectionModel().getSelectedItem();
        if (selectedSkill != null) {
            // törölni az adatbázisból a kijelölt skilleket
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM skills WHERE username = ? AND skill_name = ?");
                stmt.setString(1, username);
                stmt.setString(2, selectedSkill);
                stmt.executeUpdate();
                connection.close();

                // frissíteni a listát a törölt skillek nélkül
                skills.remove(selectedSkill);
                ListView_Skills.setItems(skills);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @FXML
    private void handleEditNameButton(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(username);
        dialog.setTitle("Edit Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your new name:");

        // Megjeleníti a dialógust és bekéri az új nevet
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = result.get();

            // Ellenőrizzük, hogy az új név nem üres
            if (!newName.isEmpty()) {
                // Frissítjük a felhasználó nevét az adatbázisban
                updateUsername(newName);
                Label_Name.setText(newName);
                username = newName;
            } else {
                // Hibaüzenet, ha az új név üres
                showErrorMessage("Name cannot be empty!");
            }
        }
    }
    private void updateUsername(String newName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = ? WHERE username = ?")) {
            statement.setString(1, newName);
            statement.setString(2, username);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Sikeres frissítés
                showInformationMessage("Name updated successfully!");
            } else {
                // Hibaüzenet, ha a frissítés nem sikerült
                showErrorMessage("Failed to update name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("An error occurred while updating name.");
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}