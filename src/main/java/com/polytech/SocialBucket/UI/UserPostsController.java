package com.polytech.SocialBucket.UI;

import java.io.Console;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.PostFacade;
import com.polytech.SocialBucket.Logic.UserFacade;
import com.polytech.SocialBucket.Logic.Reaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
  for (Post post : posts) {
   VBox postDetails = createPostDetails(post);
   postContainer.getChildren().add(postDetails);
  }
 }

 private VBox createPostDetails(Post post) {

  VBox postDetails = new VBox();

  // Créer une grille pour organiser les éléments
  GridPane gridPane = new GridPane();
  gridPane.setHgap(10); // Espacement horizontal

  Label textLabel = new Label("Text: " + post.getText());
  Label typeLabel = new Label("Type: " + post.getType());

  Label fileNameLabel = new Label("");
  if (post.getFileName() != null) {
   fileNameLabel.setText("FileName: " + post.getFileName());
  }

  // Si c'est une image, créer un ImageView et charger l'image
  ImageView imageView = new ImageView();
  if ("image".equalsIgnoreCase(post.getType())) {
   imageView = new ImageView(new Image(post.getFile().toURI().toString()));
   imageView.setFitWidth(100);
   imageView.setPreserveRatio(true);
  } else {
   imageView.setImage(null);
  }

  Button deleteButton = new Button("Delete");
  deleteButton.setOnAction(event -> handleDeletePost(post.getId()));

  Button heartButton = new Button("Heart");
  Button likeButton = new Button("Like");
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


  // Ajouter les éléments à la grille
  gridPane.add(textLabel, 0, 0);
  gridPane.add(typeLabel, 0, 1);
  gridPane.add(fileNameLabel, 0, 2);
  gridPane.add(imageView, 0, 3);
  gridPane.add(deleteButton, 1, 0);
  gridPane.add(heartButton, 1, 1);
  gridPane.add(likeButton, 1, 2);
  gridPane.add(commentButton, 4, 3);
  gridPane.add(new Label("Hearts: " + numberOfHearts), 2, 1);
  gridPane.add(new Label("Likes: " + numberOfLikes), 2, 2);

  postDetails.getChildren().add(gridPane);

  // Style
  postDetails.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/commentsPopup.fxml"));
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
