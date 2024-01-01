package com.polytech.SocialBucket.UI.Post;

import java.io.File;
import java.io.IOException;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddPostController {

 @FXML
 private GridPane gridPane;

 @FXML
 private TextField textField;

 @FXML
 private Label statusLabel;

 @FXML
 private Label fileLabel;

 @FXML
 private ImageView imageView;

 private UserFacade userFacade = UserFacade.getInstance();
 private PostFacade postFacade = PostFacade.getInstance();

 private File selectedFile;
 private Image selectedImage;

 @FXML
 private void initialize() {
  // Initialize your UI components or add event handlers if needed
  // For example, you can set default values, styles, or add listeners here
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
 private void addFile() {
  FileChooser fileChooser = new FileChooser();
  fileChooser.setTitle("Select a file");
  selectedFile = fileChooser.showOpenDialog(new Stage());
  if (selectedFile != null) {
   // Do something with the selected file
   imageView.setImage(null);
   selectedImage = null;
   fileLabel.setText("File selected: " + selectedFile.getName());
  } else {
   fileLabel.setText("File selection canceled.");
  }
 }

 @FXML
 private void addImage() {
  FileChooser fileChooser = new FileChooser();
  fileChooser.setTitle("Select an Image");
  fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
  selectedFile = fileChooser.showOpenDialog(new Stage());
  if (selectedFile != null) {
   // Load the selected image into the ImageView
   selectedImage = new Image(selectedFile.toURI().toString());
   imageView.setImage(selectedImage);
   fileLabel.setText("Image selected: " + selectedFile.getName());
  } else {
   fileLabel.setText("Image selection canceled.");
  }
 }

 @FXML
 private void handleCreatePost() throws IOException {
  // This method is called when the login button is clicked

  // Get the entered username and password
  String text = textField.getText();
  User user = userFacade.getCurrentUser();
  // Call the login method in UserFacade to validate the user

  // Check if the login was successful
  if (user != null) {
   // If successful, update the status label
   // statusLabel.setText("Successfully logged in!");
   // goToProfile();
   if (text == null || text.isEmpty()) {
    statusLabel.setText("please enter text");
    return;
   }
   if (selectedImage == null && selectedFile == null) {
    postFacade.createPost(text, "texte", null, user);
    statusLabel.setText("Post created!");
    return;
   } else if (selectedImage != null && selectedFile != null) {
    postFacade.createPost(text, "image", selectedFile, user);
    statusLabel.setText("Post created!");
    return;
   } else if (selectedImage == null && selectedFile != null) {
    postFacade.createPost(text, "file", selectedFile, user);
    statusLabel.setText("Post created!");
    return;
   } else {
    statusLabel.setText("Error while creating post, try again!");
    return;
   }
  } else {
   // If failed, update the status label
   statusLabel.setText("Login failed, try again!");
  }

 }

}
