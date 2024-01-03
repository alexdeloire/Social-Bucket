package com.polytech.SocialBucket.UI;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;


public class SidebarController {

    @FXML
    private Pane mainContent;

    @FXML
    private void initialize() {
        // Vous pouvez effectuer des initialisations ici si nécessaire
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/profile/profile.fxml"));
            Pane walletPage = loader.load();
            walletPage.setPrefWidth(720);
            // Définir la page du portefeuille comme contenu principal
            mainContent.getChildren().add(walletPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToWallet() {
        // Logique pour changer le contenu vers la page Wallet
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/wallet/walletPage.fxml"));
            GridPane walletPage = loader.load();

            // Définir la page du portefeuille comme contenu principal
            mainContent.getChildren().add(walletPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToInfos() {
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/profile/infosPage.fxml"));
            GridPane infosPage = loader.load();

            // Définir la page du portefeuille comme contenu principal
            mainContent.getChildren().add(infosPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void changeToAdvertising() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/advertising/userAdvertisings.fxml"));
            GridPane advertisingPage = loader.load();

            mainContent.getChildren().add(advertisingPage);
        } catch (IOException e) {
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
    private void changeToMain() {
        try {
            FXRouter.goTo("profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
