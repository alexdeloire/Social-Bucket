package com.polytech.SocialBucket.UI.Advertising;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Observable;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.SidebarController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddAdvertisingController {

 private File selectedFile;

 private Image selectedImage;

 @FXML
 private ScrollPane mainContent;

 @FXML
 private Pane navbar;

 @FXML
 private Button openButton;

 @FXML
 private Button closeButton;

 @FXML
 private GridPane gridPane;

 @FXML
 private ImageView imageView;

 @FXML
 private Pane imageBox;

 @FXML
 private Button imageButton;

 @FXML
 private Button removeButton;

 @FXML
 private Button createButton;

 @FXML
 private TextField textField;

 @FXML
 private TextField linkField;

 @FXML
 private Label statusLabel;

 @FXML
 private ComboBox durationComboBox;

 @FXML
 private VBox mainBox;

 @FXML
 private VBox fieldsBox;

 @FXML
 private HBox orderBox;

 @FXML
 private HBox paymentBox;

 @FXML
 private Label date;

 @FXML
 private Label service;

 @FXML
 private Label price;

 @FXML
 private TextField creditCardField;

 private UserFacade userFacade = UserFacade.getInstance();
 private AdvertisingFacade advertisingFacade = AdvertisingFacade.getInstance();
 private WalletFacade walletFacade = WalletFacade.getInstance();

 @FXML
 private void initialize() {
  orderBox.setVisible(false);
  orderBox.setPrefHeight(0);
  orderBox.setManaged(false);
  paymentBox.setVisible(false);
  paymentBox.setPrefHeight(0);
  paymentBox.setManaged(false);

  openNavbar();
  handleButton(false);
 }

 @FXML
 private void openNavbar() {
  try {
   FXMLLoader loader = new FXMLLoader(
     getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
   Pane navbarContent = loader.load();

   navbar.setPrefWidth(140);
   mainContent.setPrefWidth(705);

   navbar.getChildren().add(navbarContent);

   handleButtonNavbar(true);

  } catch (IOException e) {
   e.printStackTrace();
  }

 }

 @FXML
 private void closeNavbar() {
  navbar.setPrefWidth(0);
  mainContent.setPrefWidth(900);

  handleButtonNavbar(false);

  navbar.getChildren().clear();
 }

 private void handleButtonNavbar(boolean open) {
  openButton.setVisible(!open);
  openButton.setManaged(!open);
  closeButton.setVisible(open);
  closeButton.setManaged(open);
  if (open) {
   closeButton.toFront();
  }
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
 private void goToMyAdvertisings() {
  try {
   FXRouter.goTo("userAdvertisings");
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 @FXML
 private void removeImage() {
  selectedImage = null;
  imageView.setImage(null);
  imageBox.setVisible(false);
  imageBox.setManaged(false);
  imageBox.setPrefHeight(0);
  handleButton(false);
 }

 private void handleButton(boolean hasFile) {
  imageButton.setVisible(!hasFile);
  imageButton.setManaged(!hasFile);
  imageBox.setVisible(hasFile);
  imageBox.setManaged(hasFile);
  removeButton.setVisible(hasFile);
  removeButton.setManaged(hasFile);
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
    int width = (int) selectedImage.getWidth();
    int height = (int) selectedImage.getHeight();
    if (width > height && width > 0) {
     imageBox.setPrefHeight(height * 400 / width);
    } else {
     imageBox.setPrefHeight(400);
    }
    imageBox.setVisible(true);
    imageBox.setManaged(true);
  }
  handleButton(true);
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
   // closeWindow();

   // fieldsBox.setVisible(false);
   // fieldsBox.setPrefHeight(0);
   mainBox.getChildren().remove(fieldsBox);

   Advertising advertising = new Advertising();
   advertising.setText(text);
   advertising.setLink(linkField.getText());
   byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());
   advertising.setImage(fileBytes);
   advertising.setUser(user);

   try {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/com/polytech/SocialBucket/advertising/otherUserAdComponent.fxml"));

    VBox adContainer = loader.load();
    OtherUserAdComponent controller = loader.getController();
    controller.loadAdvertising(advertising, null);

    mainBox.getChildren().add(0, adContainer);
   } catch (IOException e) {
    e.printStackTrace();
   }

   createButton.setVisible(false);
   createButton.setManaged(false);

   openOrderBox(selectedDuration);

  } else {
   // If failed, update the status label
   statusLabel.setText("Login failed, try again!");
  }

 }

 @FXML
 public void openOrderBox(int duration) {
  orderBox.setVisible(true);
  orderBox.setPrefHeight(200);
  orderBox.setManaged(true);

  String today = java.time.LocalDate.now().toString();
  date.setText("Date : " + today);
  service.setText("Service : Advertising");
  price.setText("Price : " + duration * 100 + " euros");
 }

 @FXML
 public void openPaymentBox() {
  orderBox.setVisible(false);
  orderBox.setPrefHeight(0);
  orderBox.setManaged(false);
  paymentBox.setVisible(true);
  paymentBox.setPrefHeight(200);
  paymentBox.setManaged(true);

 }

 @FXML
 public void pay() {
  int duration = Integer.parseInt(durationComboBox.getValue().toString());
  User user = userFacade.getCurrentUser();
  File file = selectedFile;
  String link = linkField.getText();
  String text = textField.getText();

  statusLabel.setText("");
  if (walletFacade.checkSecretCode(user.getId(), Integer.parseInt(creditCardField.getText()))) {
   if (walletFacade.pay(user.getId(), duration * 100)) {
    try {
     advertisingFacade.createAdvertising(text, link, file, user, duration);
    } catch (IOException e) {
     e.printStackTrace();
    }
    statusLabel.setText("Advertising created successfully !");
    goToMyAdvertisings();

   } else {
    statusLabel.setText("Not enough money on your wallet");
   }
  } else {
   statusLabel.setText("Wrong secret code");
  }

 }

}