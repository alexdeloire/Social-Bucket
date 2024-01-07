package com.polytech.SocialBucket.UI.OtherUsers;

import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class SearchUsersController {

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
    private Pane statusBox;

    @FXML
    private TextField inputField;


    private UserFacade userFacade = UserFacade.getInstance();

    @FXML
    private void initialize() {
        openNavbar();

        setStatusLabel("");
        handleUsersBox(false);

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

    private void setStatusLabel(String status) {
        if (status == null || status.isEmpty()) {
            statusBox.setVisible(false);
            statusBox.setManaged(false);
        } else {
            statusLabel.setText(status);
            statusBox.setVisible(true);
            statusBox.setManaged(true);
        }
    }

    private void handleUsersBox(boolean open) {
        usersBox.setVisible(open);
        usersBox.setManaged(open);
    }

    @FXML
    private void handleSearch() {
        handleUsersBox(false);
        setStatusLabel("");
        String query = inputField.getText().trim();
        if (query.isEmpty()) {
            setStatusLabel("Please enter a username");
            return;
        }

        List<User> users = userFacade.searchUsers(query);

        displayUsers(users);
    }




    private void displayUsers(List<User> users) {
        usersContainer.getChildren().clear();
        if (users == null || users.isEmpty()) {
            setStatusLabel("No users found");
            return;
        } else {
            for (User user : users) {
                Pane usersDetails = createUserDetails(user);
                usersContainer.getChildren().add(usersDetails);
            }
            handleUsersBox(true);
        }
    }

    private Pane createUserDetails(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/otherusers/otherUsersComponent.fxml"));

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
