package com.example.signup;

class User {
    private int id;
    private String username;
    private String groupName;
    private String role;

    public User(String username, String groupName) {
        this.id = id;
        this.username = username;
        this.groupName = groupName;
        this.role = role;
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
}