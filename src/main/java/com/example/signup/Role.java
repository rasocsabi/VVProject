package com.example.signup;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Role {
    private int id;
    private String username;
    private String role;

    public Role(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StringProperty idProperty() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    public StringProperty usernameProperty() {
        return new SimpleStringProperty(username);
    }

    public StringProperty roleProperty() {
        return new SimpleStringProperty(role);
    }
}