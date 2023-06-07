package com.example.employeemanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
    @FXML
    private AnchorPane main_form;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Button close;

    // Database Tools
    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;
    public void loginAdmin() {
        try {
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM admins WHERE username = ? and password = ?");
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();
            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the blank fields.");
                alert.showAndWait();
            }
            else {
                if (result.next()) {
                    GetData.username = username.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully logged in!");
                    alert.showAndWait();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("You have entered the wrong Username/Password.");
                    alert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loginEmployee() {
        try {
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM employee_login WHERE username = ? and password = ?");
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();
            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the blank fields.");
                alert.showAndWait();
            }
            else {
                if (result.next()) {
                    GetData.username = username.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully logged in!");
                    alert.showAndWait();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("employeedashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("You have entered the wrong Username/Password.");
                    alert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(ActionEvent actionEvent) {
        System.out.println(username.getText());
        System.out.println(password.getText());
        ConnectionUtil.writeToDatabase(username.getText(), password.getText());
    }

    public void close() {
        System.exit(0);
    }

    @FXML
    void addUser(ActionEvent event) {
        User user = new User(null, username.getText(), password.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        usersListView.getItems().addAll(persistenceHandler.getUsers());
    }
}