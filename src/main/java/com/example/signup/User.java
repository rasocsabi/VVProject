package com.example.signup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class User {
    private int id;
    private String username;
    private String groupName;
    private String role;
    private ObservableList<String> skills;

    private ObservableList<String> groups;

    public User(int id, String username, String groupName) {
        this.id = id;
        this.username = username;
        this.groupName = groupName;
        this.role = "";
        this.skills = FXCollections.observableArrayList();
        this.groups = FXCollections.observableArrayList();
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
        // Implementáld a költség beállítását
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
}