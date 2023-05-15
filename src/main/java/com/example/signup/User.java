package com.example.signup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class User {
    private int id;
    private String username;
    private String groupName;
    private String role;
    private double cost;
    private ObservableList<String> skills;

    private final ObservableList<String> groups;

    public User(int id, String username, String groupName) {
        this.id = id;
        this.username = username;
        this.groupName = groupName;
        this.role = "";
        this.skills = FXCollections.observableArrayList();
        this.groups = FXCollections.observableArrayList();
    }

    public User(int userId, String username, String userCost, List<Skill> skills) {
        this.id = userId;
        this.username = username;
        this.groupName = userCost;
        this.skills = FXCollections.observableArrayList();
        this.groups = FXCollections.observableArrayList();
        for (Skill skill : skills) {
            this.skills.add(skill.getSkillName());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getRole() {
        return role;
    }

    public void addSkill(String skill) {
        skills.add(skill);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String insertQuery = "INSERT INTO skills (user_id, username, skill_name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, getId());
            statement.setString(2, getUsername());
            statement.setString(3, skill);
            statement.executeUpdate();
            connection.close();

            // Logolás
            System.out.println("Skill added successfully: " + skill);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setSkillLevel(String skill, int level) {
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).equals(skill)) {
                String updatedSkill = skill;
                skills.set(i, updatedSkill);

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
                    String updateQuery = "UPDATE skills SET skill_level = ? WHERE username = ? AND skill_name = ?";
                    PreparedStatement statement = connection.prepareStatement(updateQuery);
                    statement.setInt(1, level);
                    statement.setString(2, username);
                    statement.setString(3, skill);
                    int rowsAffected = statement.executeUpdate();
                    System.out.println(statement.executeUpdate());
                    connection.close();

                    // Logolás
                    if (rowsAffected > 0) {
                        System.out.println("Skill level updated successfully for skill: " + skill);
                    } else {
                        System.out.println("Skill level update failed for skill: " + skill);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    public String getName() {
        return username;
    }

    public String getGroup(int userId) {
        String groupName = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String query = "SELECT groups.groupname FROM groupuser INNER JOIN groups ON groupuser.groupid = groups.id WHERE groupuser.userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                groupName = resultSet.getString("groupname");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupName;
    }

    public ObservableList<String> getSkills() {
        ObservableList<String> loadedSkills = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String query = "SELECT skill_name FROM skills WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String skillName = resultSet.getString("skill_name");
                loadedSkills.add(skillName);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        skills = loadedSkills;
        return skills;
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setGroup(String group) {
        this.groupName = group;
    }

    public void removeSkill(String selectedSkill) {
        skills.remove(selectedSkill);
    }

    public void setCost(String cost) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String updateQuery = "UPDATE users SET cost = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, cost);
            updateStatement.setInt(2, id);
            updateStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getIdFromDatabase() {
        int userId = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String query = "SELECT id FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public void addGroup(String trimmedGroupName) {
        groups.add(trimmedGroupName);
    }

    public String[] getGroups() {
        String[] groupArray = new String[groups.size()];
        groupArray = groups.toArray(groupArray);
        return groupArray;
    }

    public double getCost() {
        double cost = 0.0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("SELECT cost FROM users WHERE id = ?")
        ) {
            statement.setInt(1, id); // Az adott felhasználó id-je
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                cost = resultSet.getDouble("cost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return cost;
    }



    public String getSkill() {


        List<String> skills = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("SELECT skill_name FROM skills WHERE user_id = ?")
        ) {
            statement.setInt(1, id); // Az adott felhasználó id-je
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String skillName = resultSet.getString("skill_name");
                skills.add(skillName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return String.join(", ", skills); // Skill-ök vesszővel elválasztott listája

}

    public int getSkillLevel() {
        // Itt kell lekérdezni a felhasználó skill-szintjét az adatbázisból és visszaadni
        // Az alábbi példa azt feltételezi, hogy a skill-szintek a skills táblában vannak és a user_id alapján lekérdezhetők
        int maxSkillLevel = getMaxSkillLevelFromDatabase(); // Függvény, ami lekérdezi a felhasználó legnagyobb skill-szintjét
        return maxSkillLevel;
    }

    // Adatbázisból skill-ök lekérdezése user_id alapján
    private List<String> getSkillsFromDatabase() {
        List<String> skills = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("SELECT skill_name FROM skills WHERE user_id = ?")) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

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

    // Adatbázisból legnagyobb skill-szint lekérdezése user_id alapján
    private int getMaxSkillLevelFromDatabase() {
        int maxSkillLevel = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("SELECT MAX(skill_level) AS max_level FROM skills WHERE user_id = ?")) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                maxSkillLevel = resultSet.getInt("max_level");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return maxSkillLevel;
    }
}