package com.polytech.SocialBucket.UI.Post;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.CommentFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.Comment.CommentComponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.Region;


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

    @FXML
    private Pane commentsBox;

    @FXML
    private Pane commentsContainer;

    @FXML
    private TextField commentTextfield;

    @FXML
    private ImageView likeIcon;

    @FXML
    private ImageView heartIcon;


    private Post post;
    private UserFacade userFacade = UserFacade.getInstance();
    private PostFacade postFacade = PostFacade.getInstance();
    private CommentFacade commentFacade = CommentFacade.getInstance();


    private boolean hasLiked = false;
    private boolean hasHearted = false;

    private boolean isCurrentUser = false;
    private boolean isCommentOpened = false;

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

        openCommentBox(false);
    }

    private void actualizeReaction() {
        int numberOfLikes = 0;
        int numberOfHearts = 0;

        List<Reaction> reactions = post.getReactions();

        for (Reaction reaction : reactions) {
            if ("like".equalsIgnoreCase(reaction.getType())) {
                numberOfLikes++;
            } else if ("heart".equalsIgnoreCase(reaction.getType())) {
                numberOfHearts++;
            }

            if (reaction.getIduser() == userFacade.getCurrentUser().getId()) {
                if ("like".equalsIgnoreCase(reaction.getType())) {
                    setHasLiked(true);
                }
                if ("heart".equalsIgnoreCase(reaction.getType())) {
                    setHasHearted(true);
               }
            }
        }

        likeNumber.setText(String.valueOf(numberOfLikes));
        heartNumber.setText(String.valueOf(numberOfHearts));

        User currentUser = userFacade.getCurrentUser();
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

    private void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
        if (hasLiked) {
            likeIcon.setImage(new Image("file:src/main/java/com/polytech/SocialBucket/UI/Icones/like-filled.png"));
        } else {
            likeIcon.setImage(new Image("file:src/main/java/com/polytech/SocialBucket/UI/Icones/like.png"));
        }
    }

    private void setHasHearted(boolean hasHearted) {
        this.hasHearted = hasHearted;
        if (hasHearted) {
            heartIcon.setImage(new Image("file:src/main/java/com/polytech/SocialBucket/UI/Icones/heart-filled.png"));
        } else {
            heartIcon.setImage(new Image("file:src/main/java/com/polytech/SocialBucket/UI/Icones/heart.png"));
        }
    }

    @FXML
    private void handleAddHeart() {
        if (hasHearted) {
            Boolean deleteSucces = deleteReaction("heart");
            if (deleteSucces) {
                setHasHearted(false);
                heartNumber.setText(String.valueOf(Integer.parseInt(heartNumber.getText()) - 1));
            }
        } else {
            Boolean addSucces = addReaction("heart");
            if (addSucces) {
                setHasHearted(true);
                heartNumber.setText(String.valueOf(Integer.parseInt(heartNumber.getText()) + 1));
            }
        }
    }

    @FXML
    private void handleAddLike() {
        if (hasLiked) {
            Boolean deleteSucces = deleteReaction("like");
            if (deleteSucces) {
                setHasLiked(false);
                likeNumber.setText(String.valueOf(Integer.parseInt(likeNumber.getText()) - 1));
            }
        } else {
            Boolean addSucces = addReaction("like");
            if (addSucces) {
                setHasLiked(true);
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
    private void handleDeletePost() {
        Boolean deleteSucces = postFacade.deletePost(post.getId());
        if (deleteSucces) {
            System.out.println("Post deleted");
            refreshPosts.run();
        } else {
            System.out.println("Post deletion failed");
        }
    }


    @FXML
    private void handleCommentBox() {

        openCommentBox(!isCommentOpened);

        if (isCommentOpened) {
            loadComments();
        }
        
    }

    private void openCommentBox(boolean open) {
        System.out.println("openCommentBox " + open);
        this.isCommentOpened = open;
        commentsBox.setVisible(open);
        if (isCommentOpened) {
            commentsBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        } else {
            commentsBox.setPrefHeight(0);
        }
        commentsBox.setManaged(open);
    }

    private void loadComments() {
        List<Comment> comments = commentFacade.getComments(post);
        if (comments != null) {
            commentsContainer.getChildren().clear();

            for (Comment comment : comments) {
                Pane commentDetails = createCommentDetails(comment);
                commentsContainer.getChildren().add(commentDetails);
            }

        }
    }

    @FXML
    private Pane createCommentDetails(Comment comment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/comment/commentComponent.fxml"));
            Pane commentDetails = loader.load();

            CommentComponent commentComponent = loader.getController();
            commentComponent.loadComment(comment);

            return commentDetails;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @FXML
    public void handleAddComment() {

        String content = commentTextfield.getText();
        if (content.isEmpty()) {
            return;
        }
        boolean success = commentFacade.addComment(content, post);
        commentTextfield.clear();
        if (success) {
            loadComments();
        }
    }

}