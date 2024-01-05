package com.polytech.SocialBucket.UI.Profile;

import java.io.IOException;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class InfoPageController {

  @FXML
  private ScrollPane mainContent;

  @FXML
  private Label userIdLabel;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label mailLabel;

  @FXML
  private Pane navbar;

  @FXML
  private Button openButton;

  @FXML
  private Button closeButton;

  public void initialize() {
    // init name
    User user = UserFacade.getInstance().getCurrentUser();
    if (user == null) {
      return;
    }

    String name = user.getUsername();
    String mail = user.getMail();
    int id = user.getId();
    userIdLabel.setText("Id : " + id);
    usernameLabel.setText("Name : " + name);
    mailLabel.setText("Mail : " + mail);

    openNavbar();
  }

  @FXML
  private void goToModifyInfos() {
    try {
      FXRouter.goTo("modifyInfo");
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

}
