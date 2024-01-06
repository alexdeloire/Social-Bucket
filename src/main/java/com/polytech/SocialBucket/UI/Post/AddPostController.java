package com.polytech.SocialBucket.UI.Post;

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

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class AddPostController {

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
    private TextField descriptionField;

    @FXML
    private Button imageButton;

    @FXML
    private Button fileButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ImageView imageContainer;

    @FXML
    private Pane imageBox;

    @FXML
    private Pane fileBox;


    @FXML
    private Label filenameLabel;


    private UserFacade userFacade = UserFacade.getInstance();
    private PostFacade postFacade = PostFacade.getInstance();

    private Image selectedImage;
    private File selectedFile;

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

        removeButton.setVisible(false);
        removeButton.setManaged(false);

        handleButton(false);

        openNavbar();
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
    private void openNavbar() {
        try {
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
    private void handleCreatePost() {
        String description = descriptionField.getText();
        User user = UserFacade.getInstance().getCurrentUser();

        if (user == null) {
            statusLabel.setText("You must be logged in to post");
            return;
        }

        if (description == null || description.isEmpty()) {
            statusLabel.setText("Description cannot be empty");
            return;
        }
        try {
            if (selectedImage == null && selectedFile == null) {
                postFacade.createPost(description, "texte", null, user);
            } else if (selectedImage != null && selectedFile != null) {
                postFacade.createPost(description, "image", selectedFile, user);
            } else if (selectedImage == null && selectedFile != null) {
                postFacade.createPost(description, "file", selectedFile, user);
                statusLabel.setText("Post created!");
            }
            statusLabel.setText("Post created!");

            Thread.sleep(1000);

            goToProfile();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error while creating post");
        }

    }

    @FXML
    private void addImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            imageContainer.setImage(selectedImage);
            imageBox.setVisible(true);
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();
            if (width > height && width > 0) {
                imageBox.setPrefHeight(height * 400 / width);
            } else {
                imageBox.setPrefHeight(400);
            }
            filenameLabel.setText("");
            fileBox.setVisible(false);
            fileBox.setPrefHeight(0);
            handleButton(true);
        } else {
            filenameLabel.setText("");
        }
    }

    private void handleButton(boolean hasFile) {
        imageButton.setVisible(!hasFile);
        imageButton.setManaged(!hasFile);
        fileButton.setVisible(!hasFile);
        fileButton.setManaged(!hasFile);
        removeButton.setVisible(hasFile);
        removeButton.setManaged(hasFile);
    }

    @FXML
    private void addFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imageContainer.setImage(null);
            selectedImage = null;
            filenameLabel.setText(selectedFile.getName());
            fileBox.setVisible(true);
            fileBox.setPrefHeight(50);
            imageBox.setVisible(false);
            imageBox.setPrefHeight(0);
            handleButton(true);
        } else {
            filenameLabel.setText("File selection canceled.");
        }
    }


    @FXML
    private void removeImageOrFile(){
        selectedImage = null;
        selectedFile = null;
        imageContainer.setImage(null);
        filenameLabel.setText("");
        imageBox.setVisible(false);
        imageBox.setPrefHeight(0);
        fileBox.setVisible(false);
        fileBox.setPrefHeight(0);
        handleButton(false);
    }
}
