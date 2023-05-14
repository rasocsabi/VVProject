package com.example.signup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class User {
    private int id;
    private String username;
    private String groupName;
    private String role;
    private ObservableList<String> skills;

    public User(String username, String groupName) {
        this.id = 0; // Kezdetben az azonosító értéke 0 lehet
        this.username = username;
        this.groupName = groupName;
        this.role = ""; // Kezdetben a szerep üres string lehet
        this.skills = FXCollections.observableArrayList(); // Üres ObservableList létrehozása a képességekhez
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
    }

    public void setSkillLevel(String skill, String level) {
        // Implementáld a képesség szintjének beállítását
        // Ehhez meg kell keresni a képességet az ObservableList-ben és módosítani a szintjét
    }

    public String getName() {
        return username; // A név megegyezik a felhasználónévvel a példában
    }

    public String getGroup() {
        return groupName;
    }

    public ObservableList<String> getSkills() {
        return skills;
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setGroup(String group) {
        this.groupName = group;
    }

    public void removeSkill(String selectedSkill) {
    }

    public void setCost(String cost) {
    }
}
