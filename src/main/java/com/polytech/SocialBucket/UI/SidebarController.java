package com.polytech.SocialBucket.UI;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SidebarController {

    
    @FXML
    private BorderPane mainContent;

    @FXML
    private void initialize() {
        // Vous pouvez effectuer des initialisations ici si nécessaire
    }

    @FXML
    private void changeToWallet() {
        // Logique pour changer le contenu vers la page Wallet
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/walletPage.fxml"));
            GridPane walletPage = loader.load();

            // Définir la page du portefeuille comme contenu principal
            mainContent.setCenter(walletPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToInfos() {
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/infosPage.fxml"));
            GridPane infosPage = loader.load();

            // Définir la page du portefeuille comme contenu principal
            mainContent.setCenter(infosPage);
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
