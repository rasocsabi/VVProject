package com.example.vvproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.vvproject.LoginController.usernameField;

public class EditUsersController {

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

    public void setUser(User user) {
        this.user = user;
        usernameLabel.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        groupComboBox.setValue(user.getGroup());
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

@FXML
void onAddButtonClicked() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    String group = groupField.getText();
    if (username.trim().isEmpty() || password.trim().isEmpty() || group.trim().isEmpty()) {
        showAlert("Minden mezőt ki kell tölteni!", Alert.AlertType.ERROR);
        return;
    }
    if (isEditMode) {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        selectedUser.setUsername(username);
        selectedUser.setPassword(password);
        selectedUser.setGroup(group);
        observableUsers.set(selectedIndex, selectedUser);
        users.set(selectedIndex, selectedUser);
        writeUsersToFile(users);
        isEditMode = false;
        selectedIndex = -1;
        messageLabel.setText("Felhasználó módosítva!");
    } else {
        User newUser = new User(username, password, group);
        observableUsers.add(newUser);
        users.add(newUser);
        writeUsersToFile(users);
        messageLabel.setText("Új felhasználó hozzáadva!");
    }
    clearFields();
}
}