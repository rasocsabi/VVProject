package com.example.signup;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DatabaseUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String status) {
        Parent root = null;
        if (username != null && status != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DatabaseUtils.class.getResource(fxmlFile));
                root = loader.load();
                if (fxmlFile.equals("mygroups.fxml")) {
                    MyGroupsController myGroupsController = loader.getController();
                    myGroupsController.initialize();
                } else {
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.setStatusInformation(username, status);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                root = FXMLLoader.load(DatabaseUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String status){
        Connection connection = null;
        PreparedStatement preparedStatementInsert = null;
        PreparedStatement preparedStatementCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            preparedStatementCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatementCheckUserExists.setString(1, username);
            resultSet = preparedStatementCheckUserExists.executeQuery();
            if(resultSet.isBeforeFirst()){
                System.out.println("Username is already taken");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username is already taken");
                alert.show();
            }else{
                preparedStatementInsert = connection.prepareStatement("INSERT INTO users (username, password, status) VALUES (?, ?, ?)");
                preparedStatementInsert.setString(1, username);
                preparedStatementInsert.setString(2, password);
                preparedStatementInsert.setString(3, status);
                preparedStatementInsert.executeUpdate();
                changeScene(event, "logged-in.fxml", "Welcome!", username, status);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }    
            if(preparedStatementCheckUserExists != null){
                try{
                    preparedStatementCheckUserExists.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatementInsert !=null){
                try{
                    preparedStatementInsert.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection !=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
            preparedStatement = connection.prepareStatement("SELECT id,password, status, role FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                System.out.println("Username don't exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username don't exist!");
                alert.show();
            } else {
                String retrievedPassword = resultSet.getString("password");
                String retrievedStatus = resultSet.getString("status");
                if(retrievedPassword.equals(password)){
                    LoggedInController.setLoggedInUser(String.valueOf(resultSet.getInt("id")));
                    changeScene(event, "logged-in.fxml", "Welcome", username, retrievedStatus);
                } else {
                    System.out.println("Password is incorrect!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Password is incorrect!");
                    alert.show();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }    
            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection !=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
