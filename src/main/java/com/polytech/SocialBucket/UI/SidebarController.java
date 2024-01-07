package com.polytech.SocialBucket.UI;

import java.io.IOException;

import javafx.fxml.FXML;

public class SidebarController {

    @FXML
    private void initialize() {
    }

    @FXML
    private void changeToWallet() {
        try {
            FXRouter.goTo("wallet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToInfos() {
        try {
            FXRouter.goTo("infos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void changeToAdvertising() {
        try {
            FXRouter.goTo("userAdvertisings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToChat() {
        try {
            FXRouter.goTo("clientConsole");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToSearchUsers() {
        try {
            FXRouter.goTo("searchUsers");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToProfile() {
        try {
            FXRouter.goTo("profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToLogin() {
        try {
            FXRouter.goTo("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToNews() {
        try {
            FXRouter.goTo("news");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
