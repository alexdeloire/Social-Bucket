package com.polytech.SocialBucket.UI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;

public class WalletController {

    @FXML
    private Label moneyInWallet;

    private void updateMoneyWallet(){
        double count = 0;
        moneyInWallet.setText("Money : " +  count );
    }

    
    public void initialize() {
        // Initialisez la valeur de moneyInWallet ici
        updateMoneyWallet();
    }

    @FXML
    private void handlePay(){
        // Générez le contenu du code QR (remplacez "VotreDonneeQRCode" par vos propres données)
        //String qrData = "VotreDonneeQRCode";
        //ByteArrayOutputStream out = QRCode.from(qrData).to(ImageType.PNG).stream();

        // Créez une image JavaFX à partir du flux d'octets
        //ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        //ImageView imageView = new ImageView(new javafx.scene.image.Image(in));

        // Créez une étiquette pour le texte des informations (facultatif)
        Label label = new Label("QRCODE");

        // Créez une mise en page VBox pour contenir l'image et l'étiquette
        //VBox vBox = new VBox(imageView, label);
        //vBox.setSpacing(10);  // Espacement entre les éléments
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
