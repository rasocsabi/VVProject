package com.example.signup;

class User {
    private final String username;
    private final String groupName;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;


    public User(String username, String groupName) {
        this.username = username;
        this.groupName = groupName;

    }

    public String getUsername() {
        return username;
    }

    public String getGroupName() {
        return groupName;
    }



}