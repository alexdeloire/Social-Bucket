package com.polytech.SocialBucket.UI;

import java.time.LocalDate;
import java.util.List;



import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.Wallet;
import com.polytech.SocialBucket.Logic.WalletFacade;

import com.polytech.SocialBucket.Logic.Card;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardsController {
    
    @FXML
        private VBox cardContainer;

    @FXML
        Button closeButton;

    @FXML
        Label titre;
    
    @FXML
        Button addCardButton;

    @FXML
        Button cancelButton;

    @FXML
        Button confirmButton;

    @FXML
        Label cardNumber;

    @FXML
        TextField cardNumberT;

    @FXML
        Label holder;

    @FXML
        TextField holderT;

    @FXML
        Label cvc;

    @FXML
        TextField cvcT;

    @FXML
        Label dateThru;

    @FXML
        DatePicker dateThruT;

    @FXML
    private void initialize(){
        loadCards();
    }

    @FXML
    private void handleAddCard(){
        titre.setText("Add a card");
        addCardButton.setVisible(false);
        cardContainer.setVisible(false);
        closeButton.setVisible(false);
        cancelButton.setVisible(true);
        confirmButton.setVisible(true);
        cvc.setVisible(true);
        cvcT.setVisible(true);
        holder.setVisible(true);
        holderT.setVisible(true);
        cardNumber.setVisible(true);
        cardNumberT.setVisible(true);
        dateThru.setVisible(true);
        dateThruT.setVisible(true);
    }

    @FXML
    private void cancel(){
        titre.setText("Liste des cartes :");
        addCardButton.setVisible(true);
        cardContainer.setVisible(true);
        closeButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        cvc.setVisible(false);
        cvcT.setVisible(false);
        holder.setVisible(false);
        holderT.setVisible(false);
        cardNumber.setVisible(false);
        cardNumberT.setVisible(false);
        dateThru.setVisible(false);
        dateThruT.setVisible(false);
    }

    private void loadCards() {
        WalletFacade walletFacade = WalletFacade.getInstance();
        List<Card> cards = walletFacade.getCurrentWallet().getCards();
        System.out.println(cards.size());
        if (cards != null){
            cardContainer.getChildren().clear();

            for (Card card : cards) {
                VBox cardDetails = createCardDetails(card);
                cardContainer.getChildren().add(cardDetails);
            }

        }
    }

    private VBox createCardDetails(Card card) {
        VBox cardDetails = new VBox();

        // Créer une grille pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50); // Espacement horizontal

        Label cardNumber = new Label("cardNumber: " + card.getCardNumber());
        

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteCard(card));
        

        // Ajouter les éléments à la grille
        gridPane.add(cardNumber, 0, 0);
        gridPane.add(deleteButton, 3, 0);
        
        cardDetails.getChildren().add(gridPane);

        // Style
        cardDetails.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        VBox.setMargin(cardDetails, new Insets(5, 0, 5, 0));

        return cardDetails;
    }

    public void addCard(){
        WalletFacade walletFacade = WalletFacade.getInstance();

        // Récupérer les valeurs des champs
        String cardNumber = cardNumberT.getText();
        String holder = holderT.getText();
        String cvc = cvcT.getText();
        LocalDate dateThru = dateThruT.getValue(); // Utilisez getValue pour obtenir la date du DatePicker

        // Valider que les champs obligatoires sont renseignés
        if (cardNumber.isEmpty() || holder.isEmpty() || cvc.isEmpty() || dateThru == null) {
            // Afficher un message d'erreur ou prendre d'autres mesures nécessaires
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        // Appeler la méthode addCard avec les informations récupérées
        walletFacade.addCard(cardNumber, holder, cvc, dateThru);
        
        // Recharger la liste des cartes après l'ajout
        loadCards();
        cancel();
    }

    public void handleDeleteCard(Card card){
        WalletFacade walletFacade = WalletFacade.getInstance();
        walletFacade.deleteCard(card);
        loadCards();
    }

    public void closePopup() {
        // Récupérer la scène à partir de n'importe quel élément graphique dans la fenêtre
        Scene scene = closeButton.getScene();
    
        // Récupérer la fenêtre à partir de la scène
        Stage stage = (Stage) scene.getWindow();
    
        // Fermer la fenêtre
        stage.close();
    }

}
