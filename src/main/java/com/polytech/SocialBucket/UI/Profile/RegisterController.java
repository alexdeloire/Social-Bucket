package com.polytech.SocialBucket.UI.Profile;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class RegisterController {
    
    @FXML
    private TextField mailField;

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
    private void goToLogin() {
        // Use FXRouter to navigate to the login page
        try {
            FXRouter.goTo("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile() {
        try {
            FXRouter.goTo("profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String mail = mailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields!");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match!");
            return;
        }

        // Perform registration
        boolean success = userFacade.register(username, mail, password);

        // Check if registration was successful
        if (success) {
            statusLabel.setText("Registration successful!");

            User user = userFacade.login(username, password);

            // Check if the login was successful
            if (user != null) {
                // If successful, update the status label
                statusLabel.setText("Successfully logged in!");
                goToProfile();
            } else {
                // If failed, update the status label
                statusLabel.setText("Login failed, try again!");
                goToLogin();
            }
        } else {
            statusLabel.setText("Registration failed, try again!");
        }
    }
}
