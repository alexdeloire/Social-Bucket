package com.polytech.SocialBucket.UI.Profile;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.Comment.CommentController;
import com.polytech.SocialBucket.UI.Post.PostComponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label followingLabel;

    @FXML
    private Label followersLabel;

    @FXML
    private ScrollPane mainContent;

    @FXML
    private Pane navbar;

    @FXML
    private Button openButton;

    @FXML
    private Button closeButton;

    @FXML
    private Pane postContainer;



    private UserFacade userFacade = UserFacade.getInstance();
    private PostFacade postFacade = PostFacade.getInstance();

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
        int nbFollowing = user.getNbFollowing();
        int nbFollowers = user.getNbFollowers();
        followingLabel.setText("Following: " + nbFollowing);
        followersLabel.setText("Followers: " + nbFollowers);

        loadPosts();

        openNavbar();
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
    private void openNavbar() {
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
            Pane navbarContent = loader.load();

            navbar.setPrefWidth(125);
            mainContent.setPrefWidth(720);

            navbar.getChildren().add(navbarContent);

            handleButtonNavbar(true);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void closeNavbar() {
            navbar.setPrefWidth(0);
            mainContent.setPrefWidth(900);

            handleButtonNavbar(false);

            navbar.getChildren().clear();
    }

    private void handleButtonNavbar(boolean open) {
        openButton.setVisible(!open);
        openButton.setManaged(!open);
        closeButton.setVisible(open);
        closeButton.setManaged(open);
        if (open) {
            closeButton.toFront();
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


    private void loadPosts() {
        // Appeler la méthode getAllPosts() du modèle ou du service

        List<Post> posts = postFacade.getPostsByUser(userFacade.getCurrentUser());
        postContainer.getChildren().clear();
        // Ajouter les détails de chaque post dans le postContainer
        if (posts == null || posts.isEmpty()) {
            postContainer.getChildren().add(new Label("No posts to display, add a new post!"));
        } else {

            for (Post post : posts) {
                VBox postDetails = createPostDetails(post);
                postContainer.getChildren().add(postDetails);
            }

        }
    }


    private VBox createPostDetails(Post post) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/post/postComponent.fxml"));

            VBox postContainer = loader.load();
            PostComponent controller = loader.getController();
            controller.loadPost(post, this::loadPosts, true);

            return postContainer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        VBox postContainer= new VBox();
        return postContainer;
    }


    // COMMENTS

    @FXML
    private void handleCommentPopUp(Post post) {
        openCommentsPopup(post);
    }

    private void openCommentsPopup(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/comment/commentsPopup.fxml"));
            Parent root = loader.load();

            CommentController commentController = loader.getController();
            commentController.setPost(post);

            Stage commentsPopupStage = new Stage();
            commentsPopupStage.initModality(Modality.APPLICATION_MODAL);
            commentsPopupStage.setTitle("Comments Popup for Post #" + post.getId());

            Scene scene = new Scene(root);
            commentsPopupStage.setScene(scene);

            commentsPopupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion de l'exception
        }
    }

}
