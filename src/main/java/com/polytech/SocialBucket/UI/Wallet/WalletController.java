package com.polytech.SocialBucket.UI.Wallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Wallet;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class WalletController {

    @FXML
    private Label moneyInWallet;

    private void updateMoneyWallet() {
        WalletFacade walletFacade = WalletFacade.getInstance();
        float count = walletFacade.getBalance();

        moneyInWallet.setText("Money : " + count);
    }

    @FXML
    private void handleOpenCardsPopup() {
        openCardsPopup();
    }

    private void openCardsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/wallet/cardsPopUp.fxml"));
            Parent root = loader.load();

            Stage cardsPopupStage = new Stage();
            cardsPopupStage.initModality(Modality.APPLICATION_MODAL);
            cardsPopupStage.setTitle("Cards Popup ");

            Scene scene = new Scene(root);
            cardsPopupStage.setScene(scene);

            cardsPopupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion de l'exception
        }
    }

    @FXML
    private void handleOpenParametersPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/wallet/parametersPopup.fxml"));
            Parent root = loader.load();

            Stage parametersPopupStage = new Stage();
            parametersPopupStage.initModality(Modality.APPLICATION_MODAL);
            parametersPopupStage.setTitle("Parameters Popup ");

            Scene scene = new Scene(root);
            parametersPopupStage.setScene(scene);

            parametersPopupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion de l'exception
        }
    }

    @FXML
    private void handleChargePopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/wallet/chargePopUp.fxml"));
            Parent root = loader.load();

            Stage chargePopupStage = new Stage();
            chargePopupStage.initModality(Modality.APPLICATION_MODAL);
            chargePopupStage.setTitle("Charge Popup ");

            Scene scene = new Scene(root);
            chargePopupStage.setScene(scene);

            chargePopupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion de l'exception
        }
        updateMoneyWallet();
    }

    public void initialize() {
        // Initialisez la valeur de moneyInWallet ici
        updateMoneyWallet();
    }

    @FXML
    private void handlePay() {
        // Générez le contenu du code QR (remplacez "VotreDonneeQRCode" par vos propres
        // données)
        // String qrData = "VotreDonneeQRCode";
        // ByteArrayOutputStream out = QRCode.from(qrData).to(ImageType.PNG).stream();

        // Créez une image JavaFX à partir du flux d'octets
        // ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        // ImageView imageView = new ImageView(new javafx.scene.image.Image(in));

        // Créez une étiquette pour le texte des informations (facultatif)
        Label label = new Label("QRCODE");

        // Créez une mise en page VBox pour contenir l'image et l'étiquette
        // VBox vBox = new VBox(imageView, label);
        // vBox.setSpacing(10); // Espacement entre les éléments
        VBox vBox = new VBox(label);
        // Créez une nouvelle scène avec la mise en page VBox
        Scene scene = new Scene(vBox);

        // Créez une nouvelle fenêtre (stage) pour la popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("QR Code Popup");
        popupStage.setResizable(false);

        // Définissez la scène pour la fenêtre popup
        popupStage.setScene(scene);

        // Affichez la fenêtre popup
        popupStage.show();
    }

}
