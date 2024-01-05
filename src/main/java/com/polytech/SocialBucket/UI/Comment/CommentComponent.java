package com.polytech.SocialBucket.UI.Comment;

import java.util.List;

import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.Facade.CommentFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CommentComponent {

    private Post post;
    private Comment comment;
    private User user;
    private UserFacade userFacade = UserFacade.getInstance();
    private CommentFacade commentFacade = CommentFacade.getInstance();

    @FXML
    private Label contentLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label likeLabel;

    @FXML
    private Label dislikeLabel;

    @FXML
    private Button deleteButton;

    private boolean isLiked = false;
    private boolean isDisliked = false;
    private int numberOfLikes = 0;
    private int numberOfDislikes = 0;
    private boolean isDeleted = false;

    @FXML
    private void initialize() {

    }


    public void loadComment(Comment comment) {
        this.comment = comment;
        this.post = comment.getPost();
        this.user = comment.getUser();
        if (comment != null) {
            contentLabel.setText(comment.getContent());
        }
        if (user != null) {
            usernameLabel.setText(user.getUsername());
        }

        int numberOfLikes = 0;
        int numberOfDislikes = 0;

        List<Reaction> reactions = comment.getReactions();
        if (reactions != null) {
            for (Reaction reaction : reactions) {

                if ("like".equalsIgnoreCase(reaction.getType())) {
                    numberOfLikes++;
                }
                if ("dislike".equalsIgnoreCase(reaction.getType())) {
                    numberOfDislikes++;
                }

                if (reaction.getIduser() == userFacade.getCurrentUser().getId()) {
                    if ("like".equalsIgnoreCase(reaction.getType())) {
                        isLiked = true;
                    }
                    if ("dislike".equalsIgnoreCase(reaction.getType())) {
                        isDisliked = true;
                    }
                }

            }
        }

        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;

        updateReactionLabels();

        if (comment.getUser().getId() != userFacade.getCurrentUser().getId()) {
            deleteButton.setVisible(false);
        } else {
            deleteButton.setVisible(true);
        }
    }

    @FXML
    private void handleLike(){
        if (isDeleted) {
            return;
        }
        if (isLiked) {
            commentFacade.deleteReaction(comment, userFacade.getCurrentUser());
            isLiked = false;
            numberOfLikes--;
        } else if (isDisliked) {
            commentFacade.deleteReaction(comment, userFacade.getCurrentUser());
            commentFacade.addReaction("like", comment, userFacade.getCurrentUser());
            isLiked = true;
            isDisliked = false;
            numberOfLikes++;
            numberOfDislikes--;
        } else {
            commentFacade.addReaction("like", comment, userFacade.getCurrentUser());
            isLiked = true;
            numberOfLikes++;
        }
        updateReactionLabels();
    }

    @FXML
    private void handleDislike(){
        if (isDeleted) {
            return;
        }
        if (isDisliked) {
            commentFacade.deleteReaction(comment, userFacade.getCurrentUser());
            isDisliked = false;
            numberOfDislikes--;
        } else if (isLiked) {
            commentFacade.deleteReaction(comment, userFacade.getCurrentUser());
            commentFacade.addReaction("dislike", comment, userFacade.getCurrentUser());
            isDisliked = true;
            isLiked = false;
            numberOfDislikes++;
            numberOfLikes--;
        } else {
            commentFacade.addReaction("dislike", comment, userFacade.getCurrentUser());
            isDisliked = true;
            numberOfDislikes++;
        }
        updateReactionLabels();
    }

    private void updateReactionLabels() {
        likeLabel.setText("" + numberOfLikes);
        dislikeLabel.setText("" + numberOfDislikes);
    }
    
    @FXML
    private void handleDeleteComment() {
        commentFacade.deleteComment(comment);
        this.isDeleted = true;
        contentLabel.setText("Comment deleted");
        contentLabel.setStyle("-fx-font-style: italic");
        usernameLabel.setText("");
        likeLabel.setText("");
        dislikeLabel.setText("");
        deleteButton.setVisible(false);
    }
    

}
