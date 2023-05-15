package com.example.signup;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectMemberSearchController {
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    @FXML
    public TextField textFieldCostMin;
    @FXML
    public TextField textFieldCostMax;
    @FXML
    private TableView<User> tableViewUsers;
    @FXML
    private TableColumn<User, String> columnUsername;
    @FXML
    private TableColumn<User, Double> columnCost;
    @FXML
    private TableColumn<User, String> columnSkill;
    @FXML
    private TableColumn<User, Integer> columnSkillLevel;
    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<String> comboBoxSkill;
    @FXML
    private ComboBox<Integer> comboBoxSkillLevel;
    @FXML
    private TextField textFieldCost;

    public void initialize() {
        // Táblázat oszlopainak beállítása
        columnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        columnCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCost()).asObject());
        columnSkill.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSkill()));
        columnSkillLevel.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSkillLevel()).asObject());

        // Skill lista betöltése az adatbázisból
        comboBoxSkill.setItems(FXCollections.observableArrayList(getSkillsFromDatabase()));

        // Skill szint lista betöltése
        comboBoxSkillLevel.setItems(FXCollections.observableArrayList(getSkillLevels()));

        // Felhasználók betöltése az adatbázisból
        userList.addAll(getUsersFromDatabase());
        tableViewUsers.setItems(userList);
    }

    private List<String> getSkillsFromDatabase() {
        List<String> skills = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT skill_name FROM skills")) {

            while (resultSet.next()) {
                String skillName = resultSet.getString("skill_name");
                skills.add(skillName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return skills;
    }

    private List<Integer> getSkillLevels() {
        List<Integer> skillLevels = new ArrayList<>();

        // Előre definiált értékek
        skillLevels.addAll(Arrays.asList(1, 2, 3, 4, 5));

        return skillLevels;
    }

    private List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String cost = resultSet.getString("cost");

                // Felhasználó skill-jeinek lekérdezése
                List<Skill> skills = getUserSkills(connection, userId);

                User user = new User(userId, username, cost, skills);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return users;
    }


    @FXML
    private void handleSearchButton(ActionEvent event) {
        String name = textFieldName.getText();
        String skill = comboBoxSkill.getValue();
        Integer skillLevel = comboBoxSkillLevel.getValue();
        String costMin = textFieldCostMin.getText();
        String costMax = textFieldCostMax.getText();

        // Felhasználók szűrése a keresési feltételek alapján
        userList.clear(); // Előző eredmények törlése

        // Felhasználók lekérdezése az adatbázisból a keresési feltételek alapján
        userList.addAll(searchUsersInDatabase(name, skill, skillLevel, costMin, costMax));
        tableViewUsers.setItems(userList);
    }

    // Az adatbázisból felhasználók keresése a megadott keresési feltételek alapján
    private List<User> searchUsersInDatabase(String name, String skill, Integer skillLevel, String costMin, String costMax) {
        List<User> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123")) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM users WHERE 1=1");

            if (name != null && !name.isEmpty()) {
                queryBuilder.append(" AND username LIKE ?");
            }

            if (costMin != null && !costMin.isEmpty() && costMax != null && !costMax.isEmpty()) {
                queryBuilder.append(" AND cost BETWEEN ? AND ?");
            }

            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            int parameterIndex = 1;

            if (name != null && !name.isEmpty()) {
                statement.setString(parameterIndex++, "%" + name + "%");
            }

            if (costMin != null && !costMin.isEmpty() && costMax != null && !costMax.isEmpty()) {
                statement.setString(parameterIndex++, costMin);
                statement.setString(parameterIndex++, costMax);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String userCost = resultSet.getString("cost");

                // Felhasználó skill-jeinek lekérdezése
                List<Skill> skills = getUserSkills(connection, userId);

                // Skill szűrés
                if (skill != null && !skill.isEmpty() && skillLevel != null) {
                    boolean hasSkillWithLevel = false;
                    for (Skill userSkill : skills) {
                        if (userSkill.getSkillName().equals(skill) && userSkill.getSkillLevel() >= skillLevel) {
                            hasSkillWithLevel = true;
                            break;
                        }
                    }
                    if (!hasSkillWithLevel) {
                        continue; // Nem megfelelő skill és szint esetén kihagyjuk a felhasználót
                    }
                }

                User user = new User(userId, username, userCost, skills);
                results.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return results;
    }

    private PreparedStatement createSearchStatement(Connection connection, String name, String skill, Integer skillLevel, String cost) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM users WHERE 1=1");

        if (name != null && !name.isEmpty()) {
            queryBuilder.append(" OR username LIKE ?");
        }

        if (cost != null && !cost.isEmpty()) {
            queryBuilder.append(" OR cost = ?");
        }

        PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

        int parameterIndex = 1;

        if (name != null && !name.isEmpty()) {
            statement.setString(parameterIndex++, "%" + name + "%");
        }

        if (cost != null && !cost.isEmpty()) {
            statement.setString(parameterIndex, cost);
        }

        return statement;
    }

    private List<Skill> getUserSkills(Connection connection, int userId) throws SQLException {
        List<Skill> skills = new ArrayList<>();

        String query = "SELECT skill_name, skill_level FROM skills WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String skillName = resultSet.getString("skill_name");
            int skillLevel = resultSet.getInt("skill_level");
            Skill skill = new Skill(skillName, skillLevel);
            skills.add(skill);
        }

        return skills;
    }

    @FXML
    private void handleClearButton(ActionEvent event) {
        textFieldName.clear();
        comboBoxSkill.getSelectionModel().clearSelection();
        comboBoxSkillLevel.getSelectionModel().clearSelection();
        textFieldCostMin.clear();
        textFieldCostMax.clear();

        userList.clear();
        userList.addAll(getUsersFromDatabase());
        tableViewUsers.setItems(userList);
    }
}