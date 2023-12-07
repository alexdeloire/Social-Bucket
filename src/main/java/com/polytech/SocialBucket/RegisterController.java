package com.polytech.SocialBucket;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class RegisterController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label statusLabel;

    private UserFacade userFacade = UserFacade.getInstance();

    @FXML
    private void initialize() {
        // Initialize your UI components or add event handlers if needed
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try{
            FXRouter.goTo("login");
        }
        catch(Exception e){
            System.out.println("Error");
        }
        // String confirmPassword = confirmPasswordField.getText();

        // // Check if passwords match
        // if (!password.equals(confirmPassword)) {
        //     statusLabel.setText("Passwords do not match!");
        //     return;
        // }

        // // Perform registration
        // User registrationSuccess = userFacade.login(username, password);

        // System.out.println("registrationSuccess: " + registrationSuccess);

        // if (registrationSuccess) {
        //     statusLabel.setText("Registration successful!");
        // } else {
        //     statusLabel.setText("Registration failed, please try again.");
        // }
    }
}
