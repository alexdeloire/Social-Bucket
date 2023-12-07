package com.polytech.SocialBucket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class LoginController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private UserFacade userFacade = UserFacade.getInstance();

    @FXML
    private void initialize() {
        // Initialize your UI components or add event handlers if needed
        // For example, you can set default values, styles, or add listeners here
    }

    @FXML
    private void handleLogin() {
        // This method is called when the login button is clicked

        // Get the entered username and password
        String username = nameField.getText();
        String password = passwordField.getText();

        // Call the login method in UserFacade to validate the user
        User user = userFacade.login(username, password);

        // Check if the login was successful
        if (user != null) {
            // If successful, update the status label
            statusLabel.setText("Hello " + user.getUsername() + "!");
        } else {
            // If failed, update the status label
            statusLabel.setText("Login failed, try again!");
        }
    }
}
