package com.polytech.SocialBucket.UI.OtherUsers;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;

public class SearchUsersController {

    @FXML
    private TextField searchField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private VBox resultsContainer;
    
    @FXML
    private ListView<String> userListView;
    
    @FXML
    private Label noUsersLabel;

    private ObservableList<User> UserList = FXCollections.observableArrayList();


    @FXML
    public void searchUsers() {
        // Get the search query from the TextField
        String query = searchField.getText().trim();

        // Perform your search logic here (replace with your actual search logic)
        ObservableList<String> searchResults = performSearch(query);

        // Update the UI based on the search results
        updateUI(searchResults);
    }

    private ObservableList<String> performSearch(String query) {
        // Dummy method for testing, replace with your actual search logic
        ObservableList<String> searchResults = FXCollections.observableArrayList();

        List<User> tempList = UserFacade.getInstance().searchUsers(query);

        this.UserList.clear();
        this.UserList.addAll(tempList);

        for (User user : UserList) {
            // Add users matching the query to the searchResults
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(user.getUsername());
            }
        }

        return searchResults;
    }

    private void updateUI(ObservableList<String> searchResults) {
        // Update the ListView with search results
        userListView.setItems(searchResults);

        // Create buttons for each user and add them to the list view
        userListView.setCellFactory(param -> new UserCell());

        // Update visibility of the ListView and Label based on search results
        if (searchResults.isEmpty()) {
            resultsContainer.setVisible(false);
            noUsersLabel.setVisible(true);
        } else {
            resultsContainer.setVisible(true);
            noUsersLabel.setVisible(false);
        }
    }

    private class UserCell extends ListCell<String> {
        @Override
        protected void updateItem(String username, boolean empty) {
            super.updateItem(username, empty);

            if (empty || username == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox userBox = new HBox(10);
                Label usernameLabel = new Label(username);
                Button goToProfileButton = new Button("Go to Profile");
                goToProfileButton.setOnAction(event -> goToProfile(username));
                userBox.getChildren().addAll(usernameLabel, goToProfileButton);
                setGraphic(userBox);
            }
        }
    }

    private void goToProfile(String username) {
        // Implement the logic to navigate to the profile of the selected user
        System.out.println("Navigating to the profile of: " + username);
    }
}
