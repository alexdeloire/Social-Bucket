package com.polytech.SocialBucket.UI.Comment;

import java.util.List;

import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
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

public class CommentController {

    private Post post; // Ajout d'un champ pour stocker l'ID du post actuel
    private UserFacade userFacade = UserFacade.getInstance();
    private CommentFacade commentFacade = CommentFacade.getInstance();

    @FXML
    private TextField addCommentTextField;

    @FXML
    private Label statusLabel;

    @FXML
    Button pushComment;

    @FXML
    Button addCommentButton;

    @FXML
    private VBox commentContainer;

    @FXML
    Button closeButton;

    public void setPost(Post post) {
        this.post = post;
        System.out.println("Post set in CommentController: " + post);
        loadComments();
    }

    public Post getPost() {
        System.out.println("POST NOT NULL  :" + post != null);
        return post;
    }

    public void handleShowTextField() {
        addCommentTextField.setVisible(true);
        addCommentButton.setVisible(false);
        pushComment.setVisible(true);
    }

    @FXML
    private VBox createCommentDetails(Comment comment) {
        VBox commentDetails = new VBox();

        // Créer une grille pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Espacement horizontal

        Label contentLabel = new Label("Content: " + comment.getContent());
        Label pseudo = new Label("Pseudo : " + comment.getUser().getUsername());

        Button deleteCommentButton = new Button("Delete");
        deleteCommentButton.setOnAction(event -> handleDeleteComment(comment));
        if (comment.getUser().getId() != userFacade.getCurrentUser().getId()) {
            deleteCommentButton.setVisible(false);
        } else {
            deleteCommentButton.setVisible(true);
        }

        Button dislikeButton = new Button("Dislike");
        Button likeButton = new Button("Like");

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

            }
        }

        dislikeButton.setOnAction(event -> handleAddReaction(comment, "dislike"));
        likeButton.setOnAction(event -> handleAddReaction(comment, "like"));

        // Ajouter les éléments à la grille
        gridPane.add(contentLabel, 0, 0);
        gridPane.add(pseudo, 0, 1);
        gridPane.add(deleteCommentButton, 3, 0);
        gridPane.add(new Label("Dislikes: " + numberOfDislikes), 2, 2);
        gridPane.add(new Label("Likes: " + numberOfLikes), 2, 3);
        gridPane.add(likeButton, 0, 3);
        gridPane.add(dislikeButton, 0, 2);
        commentDetails.getChildren().add(gridPane);

        // Style
        commentDetails.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        VBox.setMargin(commentDetails, new Insets(5, 0, 5, 0));

        return commentDetails;
    }

    private void handleAddReaction(Comment comment, String typeReaction) {
        CommentFacade commentFacade = CommentFacade.getInstance();
        List<Reaction> reactions = commentFacade.getReactionsByComment(comment);

        boolean userAlreadyReacted = false;

        if (reactions != null) {
            for (Reaction reaction : reactions) {
                if (reaction.getIduser() == userFacade.getCurrentUser().getId()) {
                    userAlreadyReacted = true;
                    break; // Sortir de la boucle dès que l'on trouve une réaction de l'utilisateur
                }
            }
        }

        if (userAlreadyReacted) {
            // L'utilisateur a déjà réagi, supprimer la réaction
            handleDeleteReaction(comment);
        } else {
            // L'utilisateur n'a pas encore réagi, ajouter la réaction
            commentFacade.addReaction(typeReaction, comment, userFacade.getCurrentUser());
        }

        loadComments();
    }

    private void handleDeleteReaction(Comment comment) {
        System.out.println("on delete");
        commentFacade.deleteReaction(comment, userFacade.getCurrentUser());
        loadComments();
    }

    private void loadComments() {
        List<Comment> comments = commentFacade.getComments(getPost());
        if (comments != null) {
            commentContainer.getChildren().clear();

            for (Comment comment : comments) {
                VBox commentDetails = createCommentDetails(comment);
                commentContainer.getChildren().add(commentDetails);
            }

        }

    }

    private void handleDeleteComment(Comment comment) {
        commentFacade.deleteComment(comment);
        loadComments();

    }

    @FXML
    public void handleAddComment() {
        String content = addCommentTextField.getText();
        boolean test = commentFacade.addComment(content, getPost());
        addCommentTextField.clear();
        addCommentTextField.setVisible(false);
        pushComment.setVisible(false);
        System.out.println(test);
        if (test) {
            statusLabel.setVisible(true);
            statusLabel.setText("Successfully added the comment !");
            addCommentButton.setVisible(true);

        } else {
            statusLabel.setVisible(true);
            statusLabel.setText("Error while adding the comment !");
            addCommentButton.setVisible(true);
        }
        loadComments();
    }

    public void closePopup() {
        // Récupérer la scène à partir de n'importe quel élément graphique dans la
        // fenêtre
        Scene scene = closeButton.getScene();

        // Récupérer la fenêtre à partir de la scène
        Stage stage = (Stage) scene.getWindow();

        // Fermer la fenêtre
        stage.close();
    }

}
