package com.polytech.SocialBucket.UI.Profile;

import java.io.IOException;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ModifyInfoController {

  @FXML
  private ScrollPane mainContent;

  @FXML
  private TextField usernameField;

  @FXML
  private TextField mailField;

  @FXML
  private PasswordField newPwField;

  @FXML
  private PasswordField oldPwField;

  @FXML
  private PasswordField confirmPwField;

  @FXML
  private Label newPwLabel;

  @FXML
  private Label oldPwLabel;

  @FXML
  private Label confirmPwLabel;

  @FXML
  private Label statusLabel;

  @FXML
  private Pane navbar;

  @FXML
  private Button openButton;

  @FXML
  private Button closeButton;

  @FXML
  private Button pwButton;

  @FXML
  private HBox infoContainer;

  private UserFacade userFacade = UserFacade.getInstance();

  private boolean isModifyingPW;

  public void initialize() {
    // init name
    User user = UserFacade.getInstance().getCurrentUser();
    if (user == null) {
      return;
    }

    String name = user.getUsername();
    String mail = user.getMail();
    usernameField.setText(name);
    mailField.setText(mail);

    openNavbar();
    closePW();
  }

  @FXML
  private void goToInfoPage() {
    try {
      FXRouter.goTo("infos");
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
  private void openPW() {

    oldPwField.setVisible(true);
    oldPwLabel.setVisible(true);
    newPwField.setVisible(true);
    newPwLabel.setVisible(true);
    confirmPwField.setVisible(true);
    confirmPwLabel.setVisible(true);
    infoContainer.setPrefHeight(330);
    this.isModifyingPW = true;

    pwButton.setVisible(false);
  }

  @FXML
  private void closePW() {

    oldPwField.setVisible(false);
    oldPwLabel.setVisible(false);
    newPwField.setVisible(false);
    newPwLabel.setVisible(false);
    confirmPwField.setVisible(false);
    confirmPwLabel.setVisible(false);
    infoContainer.setPrefHeight(200);
    this.isModifyingPW = false;

    pwButton.setVisible(true);

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
  private void handleModifyInfo() {
    User user = UserFacade.getInstance().getCurrentUser();
    int id = UserFacade.getInstance().getCurrentUser().getId();
    String username = usernameField.getText();
    String mail = mailField.getText();

    String oldPassword = oldPwField.getText();
    String newPassword = newPwField.getText();
    String confirmPassword = confirmPwField.getText();

    if (!isModifyingPW) {
      if (username.isEmpty() || mail.isEmpty()) {
        statusLabel.setText("Please fill in all fields!");
        return;
      }
    } else {
      if (username.isEmpty() || mail.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()
          || confirmPassword.isEmpty()) {
        statusLabel.setText("Please fill in all fields!");
        return;
      }
      if (!newPassword.equals(confirmPassword)) {
        statusLabel.setText("Passwords do not match!");
        return;
      }
      if (!userFacade.verifyPassword(id, oldPassword)) {
        statusLabel.setText("Wrong password!");
        return;
      }
      user.setPassword(newPassword);
    }

    user.setUsername(username);
    user.setMail(mail);

    if (userFacade.updateUser(user)) {
      statusLabel.setText("Information updated!");
      goToInfoPage();
    } else {
      statusLabel.setText("Error while updating information!");
    }
  }

}
