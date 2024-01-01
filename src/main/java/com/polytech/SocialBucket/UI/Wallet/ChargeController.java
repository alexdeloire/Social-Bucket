package com.polytech.SocialBucket.UI.Wallet;

import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChargeController {

    @FXML
    Label titre;

    @FXML
    Label card;

    @FXML
    ComboBox<String> defaultCardComboBox;

    @FXML
    Label secretCodeL;

    @FXML
    TextField secretCodeT;

    @FXML
    Button closeButton;

    @FXML
    Button confirmButton;
    @FXML
    Label amountL;
    @FXML
    TextField amountT;

    @FXML
    private void initialize() {
        loadDefaultCardComboBox();
    }

    @FXML
    private void closePopup(ActionEvent event) {
        // Récupérer la scène à partir de n'importe quel élément graphique dans la
        // fenêtre
        Scene scene = closeButton.getScene();

        // Récupérer la fenêtre à partir de la scène
        Stage stage = (Stage) scene.getWindow();

        // Fermer la fenêtre
        stage.close();
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
    private void handleUpdateMoney(ActionEvent event) {
        // Ajoutez ici le code pour gérer la mise à jour de l'argent
        // Utilisez la valeur de "amountTextField.getText()" dans votre logique de
        // gestion d'argent
        String amountText = amountT.getText();
        String card = defaultCardComboBox.getValue();
        String codeText = secretCodeT.getText();

        try {
            int amount = Integer.parseInt(amountText);
            int code = Integer.parseInt(codeText);

            int realCode = WalletFacade.getInstance().getCurrentWallet().getSecreteCode();

            if (realCode == code) {
                WalletFacade.getInstance().chargeMoney(amount);

            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format");
        }
    }
}
