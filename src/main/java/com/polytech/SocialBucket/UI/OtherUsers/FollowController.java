package com.polytech.SocialBucket.UI.OtherUsers;

import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class FollowController {
 @FXML
 private ScrollPane mainContent;

 @FXML
 private Pane navbar;

 @FXML
 private Button openButton;

 @FXML
 private Button closeButton;

 @FXML
 private Pane usersContainer;

 @FXML
 private Pane usersBox;

 @FXML
 private Label statusLabel;

 @FXML
 private Label title;

 @FXML
 private Pane statusBox;

 @FXML
 private TextField inputField;

 private UserFacade userFacade = UserFacade.getInstance();

 @FXML
 private void initialize() {
  closeNavbar();
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
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
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

 public void loadUsers(String type) {
  User user = userFacade.getCurrentUser();
  List<User> users = null;

  if (type.equals("followers")) {
   users = userFacade.getFollowers(user.getId());
   title.setText("Followers");
  }

  if (type.equals("following")) {
   users = userFacade.getFollowing(user.getId());
   title.setText("Following");
  }

  displayUsers(users);
 }

 private void displayUsers(List<User> users) {
  usersContainer.getChildren().clear();

  if (users == null || users.isEmpty()) {
   Label noUsers = new Label("No users to display");
   noUsers.setStyle("-fx-font-size: 15px; -fx-alignment: CENTER; -fx-padding: 0 0 10 0;");
   usersContainer.getChildren().add(noUsers);
   return;
  } else {
   for (User user : users) {
    Pane usersDetails = createUserDetails(user);
    usersContainer.getChildren().add(usersDetails);
   }
  }
 }

 private Pane createUserDetails(User user) {
  try {
   FXMLLoader loader = new FXMLLoader(
     getClass().getResource("/com/polytech/SocialBucket/otherusers/otherUsersComponent.fxml"));

   Pane userDetail = loader.load();
   OtherUsersComponent controller = loader.getController();
   controller.loadUser(user);

   return userDetail;
  } catch (IOException e) {
   e.printStackTrace();
  }
  Pane userDetail = new Pane();
  return userDetail;
 }

}
