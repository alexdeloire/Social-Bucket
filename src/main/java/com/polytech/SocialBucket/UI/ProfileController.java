package com.polytech.SocialBucket.UI;

import com.polytech.SocialBucket.Logic.UserFacade;
import com.polytech.SocialBucket.Logic.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ProfileController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private void initialize() {
        // init name
        User user = UserFacade.getInstance().getCurrentUser();
        if (user == null) {
            usernameLabel.setText("Anonymous");
            return;
        }
        String name = user.getUsername();
        System.out.println(name);
        usernameLabel.setText(name);
    }

    @FXML
    private void goToAddPost() {
        try {
            FXRouter.goTo("addPost");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void goToUserPosts() {
        try {
            FXRouter.goTo("userPosts");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
