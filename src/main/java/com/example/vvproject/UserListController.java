package com.example.vvproject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class UserListController {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> groupColumn;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private ObservableList<User> users;

    private MainController mainController;

    public void setUsers(ObservableList<User> users) {
        this.users = users;
        tableView.setItems(users);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
    }

    @FXML
    void onEditButtonClicked(ActionEvent event) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            mainController.showEditUser(user);
        }
    }

    @FXML
    void onDeleteButtonClicked(ActionEvent event) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            users.remove(user);
        }
    }

    @FXML
    void onTableClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            User user = tableView.getSelectionModel().getSelectedItem();
            if (user != null) {
                mainController.showEditUser(user);
            }
        }
    }

    public void updateUser(User user) {
        int index = users.indexOf(user);
        if (index != -1) {
            users.set(index, user);
        }
    }
}





