package com.example.vvproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class EditUserController {

    private User user;

    private UserListController userListController;

    @FXML
    private Label usernameLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> groupComboBox;

    @FXML
    private Button saveButton;

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
    void onSaveButtonClicked() {
        String newPassword = passwordField.getText();
        String newGroup = groupComboBox.getValue();
        user.setPassword(newPassword);
        user.setGroup(newGroup);
        userListController.updateUser(user);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}