package com.example.signup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable{

    @FXML
    private Button Button_Logout;

    @FXML
    private Label Label_Welcome;

    @FXML
    private Label Label_Status;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button_Logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUtils.changeScene(event, "log-in.fxml", "Log in!", null, null);
            } 
        });
    }

    public void setStatusInformation(String username, String status){
        Label_Welcome.setText("Üdv "+username+"!");
        Label_Status.setText("Sikeresen beléptél, most merre tovább?");
    }
    
}
