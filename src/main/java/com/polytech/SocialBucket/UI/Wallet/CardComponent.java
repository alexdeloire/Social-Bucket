package com.polytech.SocialBucket.UI.Wallet;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CardComponent {
    
    @FXML
    private Label holderLabel;

    @FXML
    private Label cardNumberLabel;

    @FXML
    private Button deleteButton;

    private Card card;

    private Runnable updateCards;

    @FXML
    private void initialize() {
    }

    public void loadCard(Card card, Runnable updateCards) {
        this.card = card;
        this.updateCards = updateCards;
        if (card != null) {
            holderLabel.setText(card.getHolder());
            cardNumberLabel.setText(card.getCardNumber());
        }
    }

    @FXML
    private void deleteCard() {
        WalletFacade walletFacade = WalletFacade.getInstance();
        walletFacade.deleteCard(card);
        cardNumberLabel.setText("");
        holderLabel.setText("Card deleted");
        deleteButton.setVisible(false);
        updateCards.run();
    }
    
}
