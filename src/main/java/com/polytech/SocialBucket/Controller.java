package com.polytech.SocialBucket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.polytech.SocialBucket.UI.FXRouter;

public class Controller extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize FXRouter
        primaryStage.setResizable(false);
        FXRouter.bind(this, primaryStage, "Social Bucket", 900, 600);

        // Set routes for the application
        // Profile
        FXRouter.when("login", "/com/polytech/SocialBucket/profile/login.fxml");
        FXRouter.when("register", "/com/polytech/SocialBucket/profile/register.fxml");
        FXRouter.when("profile", "/com/polytech/SocialBucket/profile/profile.fxml");
        FXRouter.when("modify-information", "/com/polytech/SocialBucket/modify-information.fxml");
        FXRouter.when("infos", "/com/polytech/SocialBucket/profile/infosPage.fxml");
        FXRouter.when("searchUsers", "/com/polytech/SocialBucket/otherusers/searchUsers.fxml");
        FXRouter.when("otherUser", "/com/polytech/SocialBucket/otherusers/otherUsersProfile.fxml");

        // Post
        FXRouter.when("addPost", "/com/polytech/SocialBucket/post/addPost.fxml");
        FXRouter.when("userPosts", "/com/polytech/SocialBucket/post/userPosts.fxml");

        FXRouter.when("wallet", "/com/polytech/SocialBucket/wallet/walletPage.fxml");
        FXRouter.when("userAdvertisings", "/com/polytech/SocialBucket/advertising/userAdvertisings.fxml");

        FXRouter.when("sidebarPage", "/com/polytech/SocialBucket/sidebarPage.fxml");
        FXRouter.when("clientConsole", "/com/polytech/SocialBucket/clientConsole.fxml");

        // Start with the login page
        FXRouter.goTo("login");
    }
}
