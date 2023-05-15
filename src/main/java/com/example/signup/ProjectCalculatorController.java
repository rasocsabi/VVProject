package com.example.signup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProjectCalculatorController {

    @FXML
    private TextField projectNameField;

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<TextField> hoursWorkedListView;

    @FXML
    private Button calculateButton;

    @FXML
    private TextArea resultTextArea;

    private ObservableList<String> users;
    private Map<String, TextField> hoursWorkedMap;

    @FXML
    private void initialize() {
        // Inicializálás
        users = FXCollections.observableArrayList();
        hoursWorkedMap = new HashMap<>();
        userList.setItems(users);
        userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleCalculateButton() {
        String projectName = projectNameField.getText();

        StringBuilder result = new StringBuilder();
        int totalHours = 0;
        double totalCost = 0.0;

        ObservableList<String> selectedUsers = userList.getSelectionModel().getSelectedItems();
        for (String user : selectedUsers) {
            TextField hoursWorkedField = hoursWorkedMap.get(user);
            int hoursWorked = Integer.parseInt(hoursWorkedField.getText());

            // Számítások
            totalHours += hoursWorked;
            double hourlyRate = calculateHourlyRate(user); // Felhasználó órabére
            double userCost = hoursWorked * hourlyRate; // Felhasználó költsége
            totalCost += userCost;

            // Eredmények összeállítása
            result.append("User: ").append(user).append("\n");
            result.append("Worked Hours: ").append(hoursWorked).append("\n");
            result.append("User Cost: ").append(userCost).append("\n\n");
        }

        // Összesített eredmények hozzáadása
        result.append("Total Hours: ").append(totalHours).append("\n");
        result.append("Total Cost: ").append(totalCost);

        resultTextArea.setText(result.toString());
    }

    private double calculateHourlyRate(String user) {
        double hourlyRate = 0.0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            String query = "SELECT cost FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                hourlyRate = resultSet.getDouble("cost");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hourlyRate;
    }

    @FXML
    private void handleAddUserButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter user name:");
        dialog.showAndWait().ifPresent(userName -> {
            users.add(userName);
            TextField hoursWorkedField = new TextField();
            hoursWorkedMap.put(userName, hoursWorkedField);
            hoursWorkedListView.getItems().add(hoursWorkedField);
        });
    }

    @FXML
    private void handleRemoveUserButton() {
        int selectedIndex = userList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String removedUser = userList.getSelectionModel().getSelectedItem();
            users.remove(removedUser);
            TextField removedField = hoursWorkedMap.remove(removedUser);
            hoursWorkedListView.getItems().remove(removedField);
        }
    }

}
