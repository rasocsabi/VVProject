package com.example.signup;

class User {
    private  String username;
    private  String groupName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

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