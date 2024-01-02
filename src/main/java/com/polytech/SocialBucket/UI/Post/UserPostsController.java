package com.polytech.SocialBucket.UI.Post;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.Comment.CommentController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserPostsController {

    @FXML
    private VBox postContainer;

    private UserFacade userFacade = UserFacade.getInstance();
    private PostFacade postFacade = PostFacade.getInstance();

    // private int numberOfLikes = 0;
    // private int numberOfHearts = 0;

    public void initialize() {
        // Charger les posts lors de l'initialisation
        loadPosts();
    }

    @FXML
    private void goToProfile() {
        try {
            FXRouter.goTo("profile");
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

    // Méthode pour gérer le téléchargement du fichier
    private void handleFileDownload(Post post, byte[] fileBytes) {

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

    private VBox createPostDetails(Post post) {

        VBox postDetails = new VBox();

        // Créer une grille pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Espacement horizontal

        // Si c'est une image, créer un ImageView et charger l'image
        byte[] fileBytes = post.getBytes();
        if ("image".equalsIgnoreCase(post.getType()) && fileBytes != null) {
            // Convertir le tableau de bytes en Image
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
            Image image = new Image(byteArrayInputStream);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);
            imageView.setPreserveRatio(true);

            gridPane.add(imageView, 4, 1);
        }
        if ("file".equalsIgnoreCase(post.getType()) && fileBytes != null) {
            Label fileNameLabel = new Label("");

            fileNameLabel.setText("" + post.getFileName());
            fileNameLabel.setWrapText(true);
            fileNameLabel.setMinWidth(250);
            fileNameLabel.setMaxWidth(250);
            fileNameLabel.setMinHeight(Region.USE_PREF_SIZE);

            fileNameLabel.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Appeler la méthode handleFileDownload
                    handleFileDownload(post, post.getBytes());
                }
            });

            gridPane.add(fileNameLabel, 4, 1);
        }

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeletePost(post.getId()));

        Button likeButton = new Button("Like");
        /*
         * ImageView likeImageView = new ImageView(
         * new
         * Image(getClass().getResource("/com/polytech/SocialBucket/UI/Icones/heart.png"
         * ).toExternalForm()));
         * likeImageView.setFitWidth(20); // Définir la largeur souhaitée
         * likeImageView.setFitHeight(20); // Définir la hauteur souhaitée
         * likeButton.setGraphic(likeImageView);
         * likeButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;"
         * );
         */
        Button heartButton = new Button("Heart");
        /*
         * ImageView heartImageView = new ImageView(
         * new
         * Image(getClass().getResource("/com/polytech/SocialBucket/UI/Icones/heart.png"
         * ).toExternalForm()));
         * heartImageView.setFitWidth(20); // Définir la largeur souhaitée
         * heartImageView.setFitHeight(20); // Définir la hauteur souhaitée
         * heartButton.setGraphic(heartImageView);
         * heartButton.
         * setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
         */
        heartButton.setOnAction(event -> handleAddReaction(post, "heart"));
        likeButton.setOnAction(event -> handleAddReaction(post, "like"));

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

            if (reaction.getIduser() == userFacade.getCurrentUser().getId()) {
                if ("like".equalsIgnoreCase(reaction.getType())) {
                    likeButton.setOnAction(event -> handleDeleteReaction(post, "like"));
                }
                if ("heart".equalsIgnoreCase(reaction.getType())) {
                    heartButton.setOnAction(event -> handleDeleteReaction(post, "heart"));
                }
            }
        }

        Button commentButton = new Button("Comments");
        commentButton.setOnAction(event -> handleCommentPopUp(post));

        Label textLabel = new Label("" + post.getText());
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(250);
        textLabel.setMinWidth(250);
        textLabel.setMinHeight(Region.USE_PREF_SIZE);

        // Ajouter les éléments à la grille
        gridPane.add(textLabel, 4, 2);
        gridPane.add(heartButton, 0, 3);
        gridPane.add(new Label("" + numberOfHearts), 1, 3);
        gridPane.add(likeButton, 2, 3);
        gridPane.add(new Label("" + numberOfLikes), 3, 3);
        gridPane.add(commentButton, 5, 3);

        GridPane.setHalignment(deleteButton, HPos.RIGHT); // Alignement horizontal à droite
        GridPane.setValignment(deleteButton, VPos.TOP);
        gridPane.add(deleteButton, 5, 1);

        postDetails.getChildren().add(gridPane);

        // Style
        postDetails.setStyle("-fx-background-color: #e6e6e6; -fx-border-width: 0;");

        VBox.setMargin(postDetails, new Insets(5, 0, 5, 0));

        return postDetails;
    }

    private void handleDeletePost(int postId) {
        // Mettez ici votre logique de suppression du post en utilisant postId
        // Appelez la méthode de votre service ou modèle pour supprimer le post
        Boolean deleteSucces = postFacade.deletePost(postId);
        if (deleteSucces) {
            loadPosts();
        } else {
            System.out.println("Post deletion failed");
        }
    }

    private void handleAddReaction(Post post, String typeReaction) {
        Boolean addSucces = postFacade.addReaction(typeReaction, post, userFacade.getCurrentUser());
        // Mettez ici votre logique d'ajout de réaction au post en utilisant postId
        // Appelez la méthode de votre service ou modèle pour ajouter la réaction
        if (addSucces) {

            loadPosts();
            /*
             * if ("like".equalsIgnoreCase(typeReaction)) {
             * System.out.println("teste test");
             * numberOfLikes++;
             * } else if ("heart".equalsIgnoreCase(typeReaction)) {
             * numberOfHearts++;
             * }
             */
            System.out.println("Reaction added to post " + post.getId());
        } else {
            System.out.println("Reaction addition failed");
        }
    }

    private void handleDeleteReaction(Post post, String typeReaction) {
        Boolean deleteSucces = postFacade.deleteReaction(typeReaction, post, userFacade.getCurrentUser());
        if (deleteSucces) {
            loadPosts();
            /*
             * if ("like".equalsIgnoreCase(typeReaction)) {
             * numberOfLikes--;
             * } else if ("heart".equalsIgnoreCase(typeReaction)) {
             * numberOfHearts--;
             * }
             */
            System.out.println("Reaction deleted from post " + post.getId());
        } else {
            System.out.println("Reaction deletion failed");
        }
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
