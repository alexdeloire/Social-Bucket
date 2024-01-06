package com.polytech.SocialBucket.UI.Advertising;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;
import com.polytech.SocialBucket.UI.Post.PostComponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserAdvertisingsController {
  @FXML
  private VBox advertisingContainer;

  @FXML
  private ScrollPane mainContent;

  @FXML
  private Pane navbar;

  @FXML
  private Button openButton;

  @FXML
  private Button closeButton;

  private UserFacade userFacade = UserFacade.getInstance();
  private AdvertisingFacade advertisingFacade = AdvertisingFacade.getInstance();

  public void initialize() {
    // init name
    User user = UserFacade.getInstance().getCurrentUser();
    if (user == null) {
      return;
    }

    // Charger les posts lors de l'initialisation
    loadAdvertisings();

    openNavbar();
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
  private void openNavbar() {
    try {
      // Charger la page du portefeuille depuis le fichier FXML
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
      Pane navbarContent = loader.load();

      navbar.setPrefWidth(125);
      mainContent.setPrefWidth(720);

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

  private void loadAdvertisings() {
    // Appeler la méthode getAllPosts() du modèle ou du service
    List<Advertising> advertisings = advertisingFacade.getAdvertisingByUser(userFacade.getCurrentUser());
    advertisingContainer.getChildren().clear();

    if (advertisings == null || advertisings.isEmpty()) {
      advertisingContainer.getChildren().add(new Label("No advertising to display"));
    } else {
      for (Advertising advertising : advertisings) {
        VBox advertisingDetails = createAdDetails(advertising);
        advertisingContainer.getChildren().add(advertisingDetails);
      }
    }
  }

  private VBox createAdDetails(Advertising advertising) {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/com/polytech/SocialBucket/advertising/userAdComponent.fxml"));

      VBox adContainer = loader.load();
      UserAdComponent controller = loader.getController();
      controller.loadAdvertising(advertising, this::loadAdvertisings);
      return adContainer;
    } catch (IOException e) {
      e.printStackTrace();
    }
    VBox adContainer = new VBox();
    return adContainer;
  }

  @FXML
  private void openNewADPopup() {
    try {
      // Charger le fichier FXML
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/com/polytech/SocialBucket/advertising/addAdvertising.fxml"));
      GridPane newADLayout = loader.load();

      // Créer la scène et la fenêtre modale
      Scene newADScene = new Scene(newADLayout);
      Stage newADStage = new Stage();
      newADStage.setTitle("Nouvelle Publicité");
      newADStage.initModality(Modality.APPLICATION_MODAL);
      newADStage.setScene(newADScene);

      // Afficher la fenêtre modale
      newADStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /*
   * private VBox createAdDetails2(Advertising advertising) {
   * VBox adBox = new VBox();
   * GridPane gridPane = new GridPane();
   * Label text = new Label(advertising.getText());
   * Label advertisingLink = new Label(advertising.getEnddate().toString());
   * LocalDate todat = LocalDate.now();
   * 
   * if (todat.isAfter(advertising.getEnddate())) {
   * Label expiredLabel = new Label("Expired");
   * gridPane.add(expiredLabel, 0, 1);
   * } else {
   * Button deleteButton = new Button("Withdrawal");
   * deleteButton.setOnAction(event -> deletePopup(advertising));
   * gridPane.add(deleteButton, 0, 1);
   * }
   * 
   * Button infoButton = new Button("+infos");
   * infoButton.setOnAction(event -> infosPopup(advertising));
   * 
   * gridPane.add(text, 0, 0);
   * gridPane.add(advertisingLink, 1, 0);
   * gridPane.add(infoButton, 2, 0);
   * adBox.getChildren().add(gridPane);
   * adBox.setStyle("-fx-background-color: #e6e6e6; -fx-border-width: 0;");
   * 
   * VBox.setMargin(adBox, new Insets(5, 0, 5, 0));
   * return adBox;
   * }
   * 
   * public void infosPopup(Advertising advertising) {
   * GridPane gridPane = new GridPane();
   * byte[] fileBytes = advertising.getImage();
   * 
   * ByteArrayInputStream byteArrayInputStream = new
   * ByteArrayInputStream(fileBytes);
   * Image image = new Image(byteArrayInputStream);
   * 
   * ImageView imageView = new ImageView(image);
   * imageView.setFitWidth(250);
   * imageView.setPreserveRatio(true);
   * 
   * gridPane.add(imageView, 0, 1);
   * 
   * Label text2 = new Label("Text: " + advertising.getText());
   * Label text3 = new Label("Link: " + advertising.getLink());
   * Label text4 = new Label("Start date: " +
   * advertising.getBegindate().toString());
   * Label text5 = new Label("End date: " + advertising.getEnddate().toString());
   * 
   * gridPane.add(text2, 0, 1);
   * gridPane.add(text3, 0, 2);
   * gridPane.add(text4, 0, 3);
   * gridPane.add(text5, 0, 4);
   * 
   * Scene newADScene = new Scene(gridPane);
   * Stage newADStage = new Stage();
   * newADStage.initModality(Modality.APPLICATION_MODAL);
   * newADStage.setScene(newADScene);
   * newADStage.showAndWait();
   * }
   * 
   * public void handleDeleteAdvertising(int advertisingId) {
   * advertisingFacade.deleteAdvertising(advertisingId);
   * loadAdvertisings();
   * }
   * 
   * 
   * public void deletePopup(Advertising advertising) {
   * 
   * GridPane gridPane = new GridPane();
   * Label text = new Label("Are you sure you want to delete this advertising?");
   * Label text2 = new
   * Label("(Please note: advertising costs will not be refunded)");
   * 
   * Button deleteButton = new Button("Delete");
   * deleteButton.setOnAction(event -> {
   * handleDeleteAdvertising(advertising.getId());
   * Stage stage = (Stage) deleteButton.getScene().getWindow();
   * stage.close();
   * });
   * 
   * Button cancelButton = new Button("Cancel");
   * cancelButton.setOnAction(event -> {
   * Stage stage = (Stage) cancelButton.getScene().getWindow();
   * stage.close();
   * });
   * 
   * gridPane.add(text, 0, 0);
   * gridPane.add(text2, 0, 1);
   * gridPane.add(deleteButton, 0, 2);
   * gridPane.add(cancelButton, 1, 2);
   * 
   * Scene newADScene = new Scene(gridPane);
   * Stage newADStage = new Stage();
   * newADStage.initModality(Modality.APPLICATION_MODAL);
   * newADStage.setScene(newADScene);
   * newADStage.showAndWait();
   * 
   * }
   */

}
