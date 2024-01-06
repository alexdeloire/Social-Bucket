package com.polytech.SocialBucket.UI.OtherUsers;

import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class OtherUsersComponent {
    
    @FXML
    private Label usernameLabel;

    @FXML
    private Label followingLabel;

    @FXML
    private Label followersLabel;

    private UserFacade userFacade = UserFacade.getInstance();

    private User user;

    @FXML
    private void initialize() {
    }

    public void loadUser(User user) {
        this.user = user;
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            followingLabel.setText(user.getNbFollowing() + " following");
            followersLabel.setText(user.getNbFollowers() + " followers");
        }
    }


    @FXML
    private void openProfile() {
        userFacade.setCurrentViewedUser(user);
        try {
            if (userFacade.getCurrentUser().getId() == user.getId()) {
                FXRouter.goTo("profile");
                return;
            } else {
                FXRouter.goTo("otherUser");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
