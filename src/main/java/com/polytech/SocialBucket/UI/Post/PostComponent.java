package com.polytech.SocialBucket.UI.Post;

import java.io.ByteArrayInputStream;
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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PostComponent {

    @FXML
    private Label likeNumber;

    @FXML
    private Label heartNumber;

    @FXML
    private Label username;

    @FXML
    private Label description;

    @FXML
    private ImageView imageContainer;

    @FXML
    private Pane imageBox;

    @FXML
    private Label filenameLabel;

    @FXML
    private Button downloadButton;

    @FXML
    private Pane fileBox;

    @FXML
    private Button deleteButton;

    private Post post;
    private UserFacade userFacade = UserFacade.getInstance();
    private PostFacade postFacade = PostFacade.getInstance();

    private boolean hasLiked = false;
    private boolean hasHearted = false;

    private boolean isCurrentUser = false;

    private Runnable refreshPosts;

    @FXML
    private void initialize() {

    }

    // take a post and a function to refresh the posts
    public void loadPost(Post post, Runnable refreshPosts, boolean isCurrentUser) {

        this.post = post;
        // refreshPosts is a function that refresh the posts
        this.refreshPosts = refreshPosts;
        this.isCurrentUser = isCurrentUser;

        actualizeReaction();

        username.setText(post.getUser().getUsername());
        description.setText(post.getText());

        if (isCurrentUser) {
            deleteButton.setVisible(true);
        }

        byte[] fileBytes = post.getBytes();

        if ("image".equalsIgnoreCase(post.getType()) && fileBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
            Image image = new Image(bis);
            imageContainer.setImage(image);
            imageBox.setVisible(true);
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            if (width > height && width > 0) {
                imageBox.setPrefHeight(height * 400 / width);
            } else {
                imageBox.setPrefHeight(400);
            }
        } else if ("file".equalsIgnoreCase(post.getType()) && fileBytes != null) {
            fileBox.setVisible(true);
            fileBox.setPrefHeight(50);
            filenameLabel.setText(post.getFileName());
        }

    }

    private void actualizeReaction() {
        int numberOfLikes = 0;
        int numberOfHearts = 0;

        List<Reaction> reactions = post.getReactions();

        for (Reaction reaction : reactions) {
            if ("like".equalsIgnoreCase(reaction.getType())) {
                numberOfLikes++;
            }
            if ("heart".equalsIgnoreCase(reaction.getType())) {
                numberOfHearts++;
            }
        }

        likeNumber.setText(String.valueOf(numberOfLikes));
        heartNumber.setText(String.valueOf(numberOfHearts));

        User currentUser = userFacade.getCurrentUser();

        if (reactions.stream().anyMatch(reaction -> "like".equalsIgnoreCase(reaction.getType())
                && currentUser.getId() == reaction.getIduser())) {
            hasLiked = true;
        }
        if (reactions.stream().anyMatch(reaction -> "heart".equalsIgnoreCase(reaction.getType())
                && currentUser.getId() == reaction.getIduser())) {
            hasHearted = true;
        }
    }

    @FXML
    private void handleFileDownload() {

        byte[] fileBytes = post.getBytes();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload file");

        // Définir le nom de fichier suggéré
        fileChooser.setInitialFileName(post.getFileName());

        File fileToSave = fileChooser.showSaveDialog(new Stage());

        if (fileToSave != null) {
            // Écrire le contenu du fichier dans le fichier sélectionné
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                fos.write(fileBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddHeart() {
        if (hasHearted) {
            Boolean deleteSucces = deleteReaction("heart");
            if (deleteSucces) {
                hasHearted = false;
                heartNumber.setText(String.valueOf(Integer.parseInt(heartNumber.getText()) - 1));
            }
        } else {
            Boolean addSucces = addReaction("heart");
            if (addSucces) {
                hasHearted = true;
                heartNumber.setText(String.valueOf(Integer.parseInt(heartNumber.getText()) + 1));
            }
        }
    }

    @FXML
    private void handleAddLike() {
        if (hasLiked) {
            Boolean deleteSucces = deleteReaction("like");
            if (deleteSucces) {
                hasLiked = false;
                likeNumber.setText(String.valueOf(Integer.parseInt(likeNumber.getText()) - 1));
            }
        } else {
            Boolean addSucces = addReaction("like");
            if (addSucces) {
                hasLiked = true;
                likeNumber.setText(String.valueOf(Integer.parseInt(likeNumber.getText()) + 1));
            }
        }
    }

    private boolean addReaction(String typeReaction) {
        Boolean addSucces = postFacade.addReaction(typeReaction, post, userFacade.getCurrentUser());
        return addSucces;
    }

    private boolean deleteReaction(String typeReaction) {
        Boolean deleteSucces = postFacade.deleteReaction(typeReaction, post, userFacade.getCurrentUser());
        return deleteSucces;
    }

    @FXML
    private void openComment() {
        // a modifier
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
        }
    }

    @FXML
    private void handleDeletePost() {
        Boolean deleteSucces = postFacade.deletePost(post.getId());
        if (deleteSucces) {
            System.out.println("Post deleted");
            refreshPosts.run();
        } else {
            System.out.println("Post deletion failed");
        }
    }
}