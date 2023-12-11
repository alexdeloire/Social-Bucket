package com.polytech.SocialBucket.UI;

import java.util.List;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.PostFacade;
import com.polytech.SocialBucket.Logic.UserFacade;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UserPostsController {
 @FXML
 private VBox postContainer;

 private UserFacade userFacade = UserFacade.getInstance();
 private PostFacade postFacade = PostFacade.getInstance();

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
  postDetails.setSpacing(10);

  Label textLabel = new Label("Text: " + post.getText());
  Label typeLabel = new Label("Type: " + post.getType());
  Label fileNameLabel = new Label("FileName: " + post.getFileName());

  ImageView imageView = new ImageView();
  if ("image".equalsIgnoreCase(post.getType())) {
   // Si c'est une image, créer un ImageView et charger l'image
   imageView = new ImageView(new Image(post.getFile().toURI().toString()));
   imageView.setFitWidth(100); // Réglez la largeur de l'ImageView selon vos besoins
   imageView.setPreserveRatio(true);
   // postDetails.getChildren().add(imageView);
  } else {
   imageView.setImage(null);
  }
  Button deleteButton = new Button("Delete");
  deleteButton.setOnAction(event -> handleDeletePost(post.getId())); // Définir l'action du bouton

  postDetails.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
  VBox.setMargin(postDetails, new Insets(5, 0, 5, 0));

  // Ajouter d'autres détails du post au besoin

  postDetails.getChildren().addAll(textLabel, typeLabel, fileNameLabel, imageView, deleteButton);

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
}
