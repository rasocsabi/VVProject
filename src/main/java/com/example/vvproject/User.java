package com.example.vvproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private String group;

    public User(String username, String password, String group) {
        this.username = username;
        this.password = password;
        this.group = String.valueOf(group);
    }

    public static User getUser(String username, String password) {
        List<User> users = getUsersFromCsv();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getUsersFromCsv() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String username = values[0];
                String password = values[1];
                String group = values[2];
                User user = new User(username, password, group);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUserToCsv(User user) {
        try (FileWriter csvWriter = new FileWriter("users.csv", true)) {
            csvWriter.append(user.getUsername());
            csvWriter.append(",");
            csvWriter.append(user.getPassword());
            csvWriter.append(",");
            csvWriter.append(user.getGroup());
            csvWriter.append("\n");
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateUserInCsv(User user) {
        List<User> users = getUsersFromCsv();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
        saveUsersToCsv(users);
    }

    public static void saveUsersToCsv(List<User> users) {
        try (FileWriter csvWriter = new FileWriter("users.csv")) {
            for (User user : users) {
                csvWriter.append(user.getUsername());
                csvWriter.append(",");
                csvWriter.append(user.getPassword());
                csvWriter.append(",");
                csvWriter.append(user.getGroup());
                csvWriter.append("\n");
            }
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getterek és setterek
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


}