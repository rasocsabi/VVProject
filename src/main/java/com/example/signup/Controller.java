package com.example.signup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button Button_LogIn;

    @FXML
    private Button Button_SignUp;

    @FXML
    private TextField TextField_Username;

    @FXML
    private TextField TextField_Password;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button_LogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.logInUser(event, TextField_Username.getText(), TextField_Password.getText());
            }
        });

        Button_SignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.changeScene(event, "sign-up.fxml", "Sign Up!", null, null);
            }
        });
    }

}
