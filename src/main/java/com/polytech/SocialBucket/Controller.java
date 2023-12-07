package com.polytech.SocialBucket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize FXRouter
        FXRouter.bind(this, primaryStage, "Social Bucket", 600, 400);

        // Set routes for the application
        FXRouter.when("login", "/com/polytech/SocialBucket/login.fxml");
        FXRouter.when("register", "/com/polytech/SocialBucket/register.fxml");

        // Start with the login page
        FXRouter.goTo("register");
    }
}
