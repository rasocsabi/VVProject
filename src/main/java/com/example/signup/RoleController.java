package com.example.signup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleController {
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldRole;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;
    @FXML
    private TableView<Role> tableViewRoles;
    @FXML
    private TableColumn<Role, String> columnId;
    @FXML
    private TableColumn<Role, String> columnUsername;
    @FXML
    private TableColumn<Role, String> columnRole;

    private ObservableList<Role> roles;

    public void initialize() {
        // Roles lista inicializálása és feltöltése tesztadatokkal
        roles = FXCollections.observableArrayList();
        roles.addAll(getSampleRoles());

        // TableView oszlopok beállítása
        columnId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        columnUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        columnRole.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        // TableView adatok beállítása
        tableViewRoles.setItems(roles);
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        int id = Integer.parseInt(textFieldId.getText());
        String username = textFieldUsername.getText();
        String role = textFieldRole.getText();

        // Új Role objektum létrehozása
        Role newRole = new Role(id, username, role);

        // Roles listához hozzáadása
        roles.add(newRole);

        // Mezők törlése
        textFieldId.clear();
        textFieldUsername.clear();
        textFieldRole.clear();

        // Adatok hozzáadása az adatbázishoz
        insertRoleToDatabase(newRole);
    }
    private void insertRoleToDatabase(Role role) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO roles (id, username, role) VALUES (?, ?, ?)")) {

            // Paraméterek beállítása
            statement.setInt(1, role.getId());
            statement.setString(2, role.getUsername());
            statement.setString(3, role.getRole());

            // Beszúrás végrehajtása
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Role added to database.");
            } else {
                System.out.println("Failed to add role to database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        // Kijelölt elemek törlése a TableView-ből
        Role selectedRole = tableViewRoles.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            roles.remove(selectedRole);

            // Adatok törlése az adatbázisból
            deleteRoleFromDatabase(selectedRole);
        }
    }

    private void deleteRoleFromDatabase(Role role) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM roles WHERE id = ?")) {

            // Paraméter beállítása
            statement.setInt(1, role.getId());

            // Törlés végrehajtása
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Role deleted from database.");
            } else {
                System.out.println("Failed to delete role from database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }
    }


    private List<Role> getSampleRoles() {

        List<Role> roles = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, username, role FROM roles")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");

                Role userRole = new Role(id, username, role);
                roles.add(userRole);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return roles;

    }
}