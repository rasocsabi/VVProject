package com.example.vvproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;




public class EditUsersController {
    @FXML
    private TableView<User> usersTable;

    private List<User> users;
    private Label groupField;
    private List<User> userList;
    private Label usernameField;

    public EditUsersController(List<User> users) {
        this.users = users;
    }


    @FXML
    private Label usernameLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> groupComboBox;

    @FXML
    private Button saveButton;

    private User user;

    private UserListController userListController;

    @FXML
    private Label messageLabel;

    public void initialize() {
        messageLabel.setText("");
    }
    private ObservableList<User> observableUsers;

    public void setUsers(List<User> userList) {
        this.userList = userList;
        observableUsers = FXCollections.observableArrayList(userList);
        usersTable.setItems(observableUsers);
    }


    public void setUserListController(UserListController userListController) {
        this.userListController = userListController;
    }

    @FXML
    void onSaveButtonClicked(ActionEvent event) {
        String newPassword = passwordField.getText();
        String newGroup = groupComboBox.getValue();
        user.setPassword(newPassword);
        user.setGroup(newGroup);
        userListController.updateUser(user);
        saveButton.getScene().getWindow().hide();
    }

    @FXML
    void onCancelButtonClicked(ActionEvent event) {
        saveButton.getScene().getWindow().hide();
    }

    private boolean isEditMode = false;
    private int selectedIndex = -1;

    @FXML
    void onEditButtonClicked(ActionEvent event) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Nincs kiválasztott felhasználó!", Alert.AlertType.ERROR);
            return;
        }
        isEditMode = true;
        selectedIndex = usersTable.getSelectionModel().getSelectedIndex();
        setUsers((List<User>) selectedUser);
    }
    public void writeUsersToFile(List<User> userList) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("users.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onAddButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String group = groupComboBox.getValue();
        if (username.trim().isEmpty() || password.trim().isEmpty() || group == null) {
            showAlert("Minden mezőt ki kell tölteni!", Alert.AlertType.ERROR);
            return;
        }
        if (isEditMode) {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            selectedUser.setUsername(username);
            selectedUser.setPassword(password);
            selectedUser.setGroup(group);
            observableUsers.set(selectedIndex, selectedUser);
            userList.set(selectedIndex, selectedUser);
            writeUsersToFile(userList);
            isEditMode = false;
            selectedIndex = -1;
            messageLabel.setText("Felhasználó módosítva!");
        } else {
            User newUser = new User(username, password, group);
            observableUsers.add(newUser);
            userList.add(newUser);
            writeUsersToFile(userList);
            messageLabel.setText("Új felhasználó hozzáadva!");
        }

    }

    public static void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}