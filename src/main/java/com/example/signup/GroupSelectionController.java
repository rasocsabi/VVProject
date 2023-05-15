package com.example.signup;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.*;

public class GroupSelectionController {
    @FXML
    private ListView<String> listViewGroups;
    @FXML
    private Button addButton;

    private User selectedUser;
    private AdminPanelController adminPanelController;

    public void setUser(User user) {
        this.selectedUser = user;
    }

    public void setAdminPanelController(AdminPanelController controller) {
        this.adminPanelController = controller;
    }

    public void initialize() {
        try {
            // Adatbáziskapcsolat létrehozása
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");

            // SQL lekérdezés az összes csoport nevének lekérdezésére
            String query = "SELECT groupname FROM groups";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Lekérdezés végrehajtása és eredmények feldolgozása
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String groupName = rs.getString("groupname");
                listViewGroups.getItems().add(groupName);
            }

            // Kapcsolat bezárása
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Hiba kezelése
        }
    }

    @FXML
    private void handleAddButton() {
        String selectedGroup = listViewGroups.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            adminPanelController.assignUserToGroup(selectedUser, selectedGroup);
            closeWindow();
        } else {
            showWarningAlert("Invalid Selection", "Please select a group.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}