package com.polytech.SocialBucket.UI.Profile;

import java.io.IOException;
import java.util.List;

import java.lang.Thread;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.Post.PostComponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


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
    private Pane postsContainer;

    @FXML
    private Pane loading;

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

            navbar.setPrefWidth(140);
            mainContent.setPrefWidth(705);

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

        loading.setVisible(true);
        loading.setManaged(true);
        loading.setPrefHeight(100);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("debut requete");
                try {
                    List<Post> posts = postFacade.getPostsByUser(userFacade.getCurrentUser());
                    System.out.println("fin requete");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                System.out.println("debut affichage");
                                loading.setVisible(false);
                                loading.setManaged(false);
                                //loading.setPrefHeight(0);
                                System.out.println("milieu affichage");
                                displayPosts(posts);
                                System.out.println("fin affichage");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        System.out.println("page affiché");
    }


    private void displayPosts(List<Post> posts) {
        postsContainer.getChildren().clear();
        // Ajouter les détails de chaque post dans le postsContainer
        if (posts == null || posts.isEmpty()) {
            postsContainer.getChildren().add(new Label("No posts to display, add a new post!"));
        } else {
            System.out.println("debut boucle");
            for (Post post : posts) {
                VBox postDetails = createPostDetails(post);
                postsContainer.getChildren().add(postDetails);
            }
            System.out.println("fin boucle");

        }
    }

    private VBox createPostDetails(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/post/postComponent.fxml"));

            VBox postContainer = loader.load();
            PostComponent controller = loader.getController();
            controller.loadPost(post, this::loadPosts, true);

            return postContainer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        VBox postContainer = new VBox();
        return postContainer;
    }

}
