package com.polytech.SocialBucket.UI.Advertising;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;
import com.polytech.SocialBucket.UI.SidebarController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddAdvertisingController {

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
 private WalletFacade walletFacade = WalletFacade.getInstance();

 private boolean advertisingCreated = false;

 private Runnable refreshAd;

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
 public void setRefreshAd(Runnable refreshAD) {
  this.refreshAd = refreshAd;
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

   // advertisingFacade.createAdvertising(text, linkField.getText(), selectedFile,
   // user, selectedDuration);
   closeWindow();
   openOrderPopup(text, linkField.getText(), selectedFile, user, selectedDuration);

   // statusLabel.setText("Advertising created successfully !");
   // this.advertisingCreated = true;

  } else {
   // If failed, update the status label
   statusLabel.setText("Login failed, try again!");
  }

 }

 @FXML
 public void openOrderPopup(String text, String link, File file, User user, int duration) {
  GridPane gridPane = new GridPane();
  Label title = new Label("Order Details");
  String today = java.time.LocalDate.now().toString();
  Label date = new Label("Date : " + today);
  Label service = new Label("Service : Advertising");
  Label price = new Label("Price : " + duration * 100 + " euros");

  Button continueButton = new Button("Continue");
  continueButton.setOnAction(event -> {
   Stage stage = (Stage) continueButton.getScene().getWindow();
   stage.close();
   openPaymentPopup(text, link, file, user, duration);
  });

  Button cancelButton = new Button("Cancel");
  cancelButton.setOnAction(event -> {
   Stage stage = (Stage) cancelButton.getScene().getWindow();
   stage.close();
  });

  gridPane.add(title, 0, 0);
  gridPane.add(date, 0, 1);
  gridPane.add(service, 0, 2);
  gridPane.add(price, 1, 3);
  gridPane.add(continueButton, 0, 4);
  gridPane.add(cancelButton, 0, 5);

  Scene newADScene = new Scene(gridPane);
  Stage newADStage = new Stage();
  newADStage.initModality(Modality.APPLICATION_MODAL);
  newADStage.setScene(newADScene);
  newADStage.show();
 }

 @FXML
 public void openPaymentPopup(String text, String link, File file, User user, int duration) {

  GridPane gridPane = new GridPane();
  Label title = new Label("Payment");
  Label textFieldLabel = new Label("Please enter your secret code :");
  TextField creditCardField = new TextField();
  Label checkCode = new Label();
  Label checkbalance = new Label();

  Button payButton = new Button("Pay");
  payButton.setOnAction(event -> {
   checkCode.setText("");
   checkbalance.setText("");
   if (walletFacade.checkSecretCode(user.getId(), Integer.parseInt(creditCardField.getText()))) {
    if (walletFacade.pay(user.getId(), duration * 100)) {
     try {
      advertisingFacade.createAdvertising(text, link, file, user, duration);
     } catch (IOException e) {
      e.printStackTrace();
     }
     statusLabel.setText("Advertising created successfully !");
     this.advertisingCreated = true;

     // TODO
     // refresh
     // refreshAd.run();

     Stage stage = (Stage) payButton.getScene().getWindow();
     stage.close();
    } else {
     checkbalance.setText("Not enough money on your wallet");
    }
   } else {
    checkCode.setText("Wrong secret code");
   }

  });

  Button cancelButton = new Button("Cancel");
  cancelButton.setOnAction(event -> {
   Stage stage = (Stage) cancelButton.getScene().getWindow();
   stage.close();
  });

  gridPane.add(title, 0, 0);
  gridPane.add(textFieldLabel, 0, 1);
  gridPane.add(creditCardField, 0, 2);
  gridPane.add(checkCode, 0, 3);
  gridPane.add(checkbalance, 0, 4);
  gridPane.add(payButton, 0, 5);
  gridPane.add(cancelButton, 0, 6);

  Scene newADScene = new Scene(gridPane);
  Stage newADStage = new Stage();
  newADStage.initModality(Modality.APPLICATION_MODAL);
  newADStage.setScene(newADScene);
  newADStage.show();
 }

}
