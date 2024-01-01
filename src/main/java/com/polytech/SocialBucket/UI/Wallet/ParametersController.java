package com.polytech.SocialBucket.UI.Wallet;

import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ParametersController {

    @FXML
    Label titre;

    @FXML
    Label defaultCard;

    @FXML
    ComboBox<String> defaultCardComboBox;

    @FXML
    Label secretCodeL;

    @FXML
    Button secretCodeB;

    @FXML
    Label currentCode;

    @FXML
    TextField currentCodeT;

    @FXML
    Label newCode;

    @FXML
    TextField newCodeT;

    @FXML
    Button cancel;

    @FXML
    Button confirmButtonCode;

    @FXML
    Button closeButton;

    @FXML
    Button confirmButton;

    @FXML
    private void initialize() {
        loadDefaultCardComboBox();
    }

    private void loadDefaultCardComboBox() {
        WalletFacade walletFacade = WalletFacade.getInstance();
        // Replace this with actual logic to load card numbers
        List<String> cardNumbers = new ArrayList<>();
        List<Card> cards = walletFacade.getCurrentCards();
        for (Card card : cards) {
            cardNumbers.add(card.getCardNumber());
        }
        ObservableList<String> observableCardNumbers = FXCollections.observableArrayList(cardNumbers);

        // Set the items for the ComboBox
        defaultCardComboBox.setItems(observableCardNumbers);

    }

    @FXML
    private void handleModifySecretCode() {
        // Implement your logic to handle modifying the secret code
        // You can use currentCodeTextField.getText() and newCodeTextField.getText() to
        // get the entered values
        // Update the visibility of the components accordingly
        titre.setText("Secret Code");
        currentCode.setVisible(true);
        currentCodeT.setVisible(true);
        newCode.setVisible(true);
        newCodeT.setVisible(true);
        cancel.setVisible(true);
        confirmButtonCode.setVisible(true);

        // Disable other components not relevant for modifying secret code
        defaultCard.setVisible(false);
        defaultCardComboBox.setVisible(false);
        secretCodeL.setVisible(false);
        secretCodeB.setVisible(false);
        closeButton.setVisible(false);
        confirmButton.setVisible(false);
    }

    @FXML
    private void handleModifyDefaultCard() {
        WalletFacade walletFacade = WalletFacade.getInstance();
        // Assuming defaultCardComboBox is an instance of ComboBox<String>
        String selectedCardNumber = defaultCardComboBox.getValue();

        if (selectedCardNumber != null) {
            walletFacade.modifyDefaultCard(selectedCardNumber);
            System.out.println("Selected Card Number: " + selectedCardNumber);

        } else {
            System.out.println("No card number selected.");
        }

    }

    @FXML
    private void cancel() {
        // Implement cancel logic
        // Update the visibility of the components accordingly
        titre.setText("Parameters");
        currentCode.setVisible(false);
        currentCodeT.setVisible(false);
        newCode.setVisible(false);
        newCodeT.setVisible(false);
        cancel.setVisible(false);
        confirmButtonCode.setVisible(false);

        defaultCard.setVisible(true);
        defaultCardComboBox.setVisible(true);
        secretCodeL.setVisible(true);
        secretCodeB.setVisible(true);
        closeButton.setVisible(true);
        confirmButton.setVisible(true);
    }

    @FXML
    private void modifySecretCode() {
        WalletFacade walletFacade = WalletFacade.getInstance();

        String currentSecretCodeStr = currentCodeT.getText();
        String newSecretCodeStr = newCodeT.getText();

        // Validate if currentSecretCode and newSecretCode are not empty before
        // proceeding
        if (!currentSecretCodeStr.isEmpty() && !newSecretCodeStr.isEmpty()) {

            int currentSecretCode = Integer.parseInt(currentSecretCodeStr);
            int newSecretCode = Integer.parseInt(newSecretCodeStr);

            int secretcode = WalletFacade.getInstance().getCurrentWallet().getSecreteCode();
            int iduser = UserFacade.getInstance().getCurrentUser().getId();
            if (secretcode == currentSecretCode) {
                walletFacade.modifySecretCode(iduser, currentSecretCode, newSecretCode);
            } else {
                System.out.println("not right secret code");
            }

        } else {
            System.out.println("Please enter both current and new secret codes.");
        }
    }

    @FXML
    private void closePopup() {
        // Récupérer la scène à partir de n'importe quel élément graphique dans la
        // fenêtre
        Scene scene = closeButton.getScene();

        // Récupérer la fenêtre à partir de la scène
        Stage stage = (Stage) scene.getWindow();

        // Fermer la fenêtre
        stage.close();
    }
}
