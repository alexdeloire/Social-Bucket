package com.polytech.SocialBucket.UI.Advertising;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.polytech.SocialBucket.Logic.Advertising;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.Desktop;
import java.net.URI;

public class OtherUserAdComponent {

 @FXML
 private Label description;

 @FXML
 private ImageView imageContainer;

 private Runnable refreshNews;

 private Advertising advertising;

 @FXML
 private Pane imageBox;

 @FXML
 private void initialize() {

 }

 public void loadAdvertising(Advertising advertising, Runnable refreshNews) {
  this.advertising = advertising;
  this.refreshNews = refreshNews;
  description.setText(advertising.getText());
  byte[] fileBytes = advertising.getImage();
  ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
  Image image = new Image(bis);
  imageContainer.setImage(image);

 }

 public void openLink() {

  String url = advertising.getLink();

  try {
   // Vérifie si le bureau est pris en charge (peut ouvrir le navigateur)
   if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    // Crée une instance URI à partir de l'URL
    URI uri = new URI(url);

    // Ouvre le navigateur avec l'URL spécifié
    Desktop.getDesktop().browse(uri);
   } else {
    System.out.println("L'ouverture de liens n'est pas prise en charge sur ce système.");
   }
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

}
