package com.polytech.SocialBucket;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import com.polytech.SocialBucket.UI.FXRouter;

public class Controller extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize FXRouter
        primaryStage.setResizable(false);
        primaryStage.setTitle("Social Bucket");
        primaryStage.getIcons().add(new Image("file:src/main/java/com/polytech/SocialBucket/UI/Icones/logo.png"));
        FXRouter.bind(this, primaryStage, "Social Bucket", 900, 600);

        // Set routes for the application
        // Profile
        FXRouter.when("login", "/com/polytech/SocialBucket/profile/login.fxml");
        FXRouter.when("register", "/com/polytech/SocialBucket/profile/register.fxml");
        FXRouter.when("profile", "/com/polytech/SocialBucket/profile/profile.fxml");
        FXRouter.when("modifyInfo", "/com/polytech/SocialBucket/profile/modifyInfo.fxml");
        FXRouter.when("infos", "/com/polytech/SocialBucket/profile/infoPage.fxml");
        FXRouter.when("searchUsers", "/com/polytech/SocialBucket/otherusers/searchUsers.fxml");
        FXRouter.when("otherUser", "/com/polytech/SocialBucket/otherusers/otherUsersProfile.fxml");

        FXRouter.when("follow", "/com/polytech/SocialBucket/otherusers/follow.fxml");

        // Post
        FXRouter.when("addPost", "/com/polytech/SocialBucket/post/addPost.fxml");
        FXRouter.when("userPosts", "/com/polytech/SocialBucket/post/userPosts.fxml");

        FXRouter.when("news", "/com/polytech/SocialBucket/post/news.fxml");

        FXRouter.when("wallet", "/com/polytech/SocialBucket/wallet/walletPage.fxml");

        FXRouter.when("userAdvertisings", "/com/polytech/SocialBucket/advertising/userAdvertisings.fxml");
        FXRouter.when("addAdvertising", "/com/polytech/SocialBucket/advertising/addAdvertising.fxml");

        FXRouter.when("sidebarPage", "/com/polytech/SocialBucket/sidebarPage.fxml");
        FXRouter.when("clientConsole", "/com/polytech/SocialBucket/clientConsole.fxml");

        // Start with the login page
        FXRouter.goTo("login");
    }
}
