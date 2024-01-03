package com.polytech.SocialBucket.UI.Advertising;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.SidebarController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddAdvertisingController extends Observable {

 private File selectedFile;
 private Image selectedImage;

 @FXML
 private GridPane gridPane;

 @FXML
 private ImageView imageView;

 @FXML
 private TextField textField;
 @FXML
 private TextField linkField;
 @FXML
 private Label statusLabel;

 @FXML
 private ComboBox durationComboBox;

 private UserFacade userFacade = UserFacade.getInstance();
 private AdvertisingFacade advertisingFacade = AdvertisingFacade.getInstance();

 private boolean advertisingCreated = false;

 @FXML
 private void initialize() {

  if (!advertisingCreated) {
   Button createAdvertisingButton = new Button("Create Advertising");

   createAdvertisingButton.setOnAction(event -> {
    try {
     handleCreateAdvertising();
    } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
   });
   gridPane.add(createAdvertisingButton, 3, 5);

  }
  Button createAdvertisingButton = new Button("Close");

  createAdvertisingButton.setOnAction(event -> {
   closeWindow();
  });
  gridPane.add(createAdvertisingButton, 3, 6);

 }

 @FXML
 private void closeWindow() {
  Stage stage = (Stage) gridPane.getScene().getWindow();
  stage.close();
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
  }
 }

 @FXML
 private void handleCreateAdvertising() throws IOException {
  // This method is called when the login button is clicked

  // Get the entered username and password
  String text = textField.getText();
  User user = userFacade.getCurrentUser();
  // Call the login method in UserFacade to validate the user

  // Check if the login was successful
  if (user != null) {

   if (text == null || text.isEmpty()) {
    statusLabel.setText("please enter text");
    return;
   }
   if (linkField.getText() == null || linkField.getText().isEmpty()) {
    statusLabel.setText("please enter link");
    return;
   }
   if (selectedImage == null && selectedFile == null) {
    statusLabel.setText("please select image");
    return;
   }
   if (durationComboBox.getValue() == null) {
    statusLabel.setText("please select duration");
    return;
   }
   int selectedDuration = Integer.parseInt(durationComboBox.getValue().toString());
   // advertisingFacade.createAdvertising(user, text, linkField.getText(),
   // selectedFile);
   advertisingFacade.createAdvertising(text, linkField.getText(), selectedFile, user, selectedDuration);
   statusLabel.setText("Advertising created successfully !");
   this.advertisingCreated = true;

   // test
   setChanged();
   notifyObservers("advertisingCreated");
  } else {
   // If failed, update the status label
   statusLabel.setText("Login failed, try again!");
  }

 }

}
