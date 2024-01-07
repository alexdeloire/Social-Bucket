package com.polytech.SocialBucket.UI.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;


public class WalletController {

    @FXML
    private ScrollPane mainContent;

    @FXML
    private Pane navbar;

    @FXML
    private Button openButton;

    @FXML
    private Button closeButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private Pane payTab;

    @FXML
    private Pane topupTab;

    @FXML
    private Pane cardsTab;

    @FXML
    private Pane parametersTab;

    @FXML
    private Label moneyInWallet;

    @FXML 
    private Pane qrcodeBox;

    /* top up variables */

    @FXML
    private ComboBox<String> cardComboBox;

    @FXML
    private TextField amountTextfield;

    @FXML
    private TextField secretCodeTextfield;

    @FXML
    private Label statusLabelTopUp;

    @FXML
    private Pane infoCardBox;

    /* parameters variables */

    @FXML
    private ComboBox<String> defaultCardComboBox;

    @FXML
    private TextField currentCodeTextfield;

    @FXML
    private TextField newCodeTextfield;

    @FXML
    private Pane parametersBox;

    @FXML
    private Pane secretCodeBox;

    @FXML
    private Label statusLabelParameters;

    /* cards variables */

    @FXML
    private Pane cardListBox;

    @FXML
    private Pane addCardBox;

    @FXML
    private TextField cardNumberTextfield;

    @FXML
    private TextField holderTextfield;

    @FXML
    private TextField cvcTextfield;

    @FXML
    private DatePicker dateCardPicker;

    @FXML
    private Label statusLabelAddCard;

    @FXML
    private Pane cardListContainer;



    private UserFacade userFacade = UserFacade.getInstance();
    private WalletFacade walletFacade = WalletFacade.getInstance();

    @FXML
    private void initialize() {

        updateMoneyWallet();
        initializeTopUp();
        initializeParameters();
        initializeCards();

        qrcodeBox.setVisible(false);
        qrcodeBox.setManaged(false);

        openNavbar();
    }


    @FXML
    private void openNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
            Pane navbarContent = loader.load();

            navbar.setPrefWidth(140);
            mainContent.setPrefWidth(705);

            navbar.getChildren().add(navbarContent);

            handleButtonNavbar(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void closeNavbar() {
        navbar.setPrefWidth(0);
        mainContent.setPrefWidth(900);

        handleButtonNavbar(false);

        navbar.getChildren().clear();
    }

    private void handleButtonNavbar(boolean open) {
        openButton.setVisible(!open);
        openButton.setManaged(!open);
        closeButton.setVisible(open);
        closeButton.setManaged(open);
        if (open) {
            closeButton.toFront();
        }
    }

    private void updateMoneyWallet() {
        float count = walletFacade.getBalance();
        moneyInWallet.setText(count + " €");
    }
    
    private ObservableList<String> loadDefaultCardComboBox() {
        List<String> cardNumbers = new ArrayList<>();
        List<Card> cards = walletFacade.getCurrentCards();
        for (Card card : cards) {
            cardNumbers.add(card.getCardNumber());
        }
        return FXCollections.observableArrayList(cardNumbers);
    }

    /* pay methods */

    @FXML
    private void handlePay() {
        qrcodeBox.setManaged(true);
        qrcodeBox.setVisible(true);
    }

    /* top up methods */

    private void initializeTopUp() {
        cardComboBox.setItems(loadDefaultCardComboBox());
        cardComboBox.setValue(null);
        showInfoTopUp(false);
        statusLabelTopUp.setText("");
    }


    @FXML
    private void handleTopUp(){
        String cardNumber = cardComboBox.getValue();
        String amount = amountTextfield.getText();
        String secretCode = secretCodeTextfield.getText();

        if (cardNumber == null || amount == null || secretCode == null) {
            statusLabelTopUp.setText("Please fill all the fields");
            return;
        }

        if (cardNumber.isEmpty() || amount.isEmpty() || secretCode.isEmpty()) {
            statusLabelTopUp.setText("Please fill all the fields");
            return;
        }

        if (!amount.matches("[0-9]+")) {
            statusLabelTopUp.setText("Please enter a valid amount");
            return;
        }

        if (!secretCode.matches("[0-9]+")) {
            statusLabelTopUp.setText("Please enter a valid secret code");
            return;
        }

        int amountInt = 0;
        int secretCodeInt = 0;

        try{
            amountInt = Integer.parseInt(amount);
            secretCodeInt = Integer.parseInt(secretCode);
        } catch (Exception e) {
            statusLabelTopUp.setText("Please enter a valid amount");
            return;
        }

        if (amountInt < 0) {
            statusLabelTopUp.setText("Please enter a valid amount");
            return;
        }

        if (secretCodeInt < 0) {
            statusLabelTopUp.setText("Please enter a valid secret code");
            return;
        }

        int realCode = walletFacade.getCurrentWallet().getSecreteCode();

        if (realCode == secretCodeInt) {
            walletFacade.chargeMoney(amountInt);
            statusLabelTopUp.setText("Wallet updated");
            cardComboBox.setValue(null);
            amountTextfield.setText("");
            secretCodeTextfield.setText("");
            showInfoTopUp(false);
            updateMoneyWallet();
        } else {
            statusLabelTopUp.setText("Wrong secret code");
        }
        
    }

    @FXML
    private void showInfoTopUp() { showInfoTopUp(true);}

    private void showInfoTopUp(boolean show) {
        infoCardBox.setVisible(show);
        infoCardBox.setManaged(show);
    }
    
    /* parameters methods */

    private void initializeParameters() {
        defaultCardComboBox.setItems(loadDefaultCardComboBox());
        defaultCardComboBox.setValue(null);
        statusLabelParameters.setText("");
        showSecretCode(false);
    }

    private void showSecretCode(boolean show) {
        secretCodeBox.setVisible(show);
        secretCodeBox.setManaged(show);
        parametersBox.setVisible(!show);
        parametersBox.setManaged(!show);
    }

    @FXML
    private void modifySecretCode() {
        showSecretCode(true);
    }

    @FXML
    private void handleConfirmChangeParameter(){
        String selectedCardNumber = defaultCardComboBox.getValue();

        if (selectedCardNumber != null) {
            walletFacade.modifyDefaultCard(selectedCardNumber);
        }
    }

    @FXML
    private void handleModifySecretCode() {
        String currentSecretCodeStr = currentCodeTextfield.getText();
        String newSecretCodeStr = newCodeTextfield.getText();

        if (currentSecretCodeStr == null || newSecretCodeStr == null) {
            statusLabelParameters.setText("Please fill all the fields");
            return;
        }

        if (currentSecretCodeStr.isEmpty() || newSecretCodeStr.isEmpty()) {
            statusLabelParameters.setText("Please fill all the fields");
            return;
        }

        if (!currentSecretCodeStr.matches("[0-9]+") || !newSecretCodeStr.matches("[0-9]+")) {
            statusLabelParameters.setText("Please enter a valid secret code");
            return;
        }

        int currentSecretCode = 0;
        int newSecretCode = 0;

        try{
            currentSecretCode = Integer.parseInt(currentSecretCodeStr);
            newSecretCode = Integer.parseInt(newSecretCodeStr);
        } catch (Exception e) {
            statusLabelParameters.setText("Please enter a valid secret code");
            return;
        }

        if (currentSecretCode < 0 || newSecretCode < 0) {
            statusLabelParameters.setText("Please enter a valid secret code");
            return;
        }

        int realCode = walletFacade.getCurrentWallet().getSecreteCode();
        int iduser = userFacade.getCurrentUser().getId();

        if (realCode == currentSecretCode) {
            walletFacade.modifySecretCode(iduser, currentSecretCode, newSecretCode);
            statusLabelParameters.setText("");
            currentCodeTextfield.setText("");
            newCodeTextfield.setText("");
            showSecretCode(false);
        } else {
            statusLabelParameters.setText("Wrong secret code");
        }
    }

    @FXML
    private void handleCancelSecretCode () {
        currentCodeTextfield.setText("");
        newCodeTextfield.setText("");
        statusLabelParameters.setText("");
        showSecretCode(false);
    }


    /* cards methods */

    private void initializeCards() {
        cardNumberTextfield.setText("");
        holderTextfield.setText("");
        cvcTextfield.setText("");
        dateCardPicker.setValue(null);
        statusLabelAddCard.setText("");
        showAddCard(false);
        updateListCard();
    }

    @FXML
    private void handleDisplayAddCard() {
        showAddCard(true);
    }

    @FXML
    private void handleCancelAddCard() {
        initializeCards();
    }

    private void showAddCard(boolean show) {
        addCardBox.setVisible(show);
        addCardBox.setManaged(show);
        cardListBox.setVisible(!show);
        cardListBox.setManaged(!show);
    }

    @FXML
    private void handleAddCard() {
        String cardNumber = cardNumberTextfield.getText();
        String holder = holderTextfield.getText();
        String cvc = cvcTextfield.getText();
        LocalDate date = dateCardPicker.getValue();

        if (cardNumber == null || holder == null || cvc == null || date == null) {
            statusLabelAddCard.setText("Please fill all the fields");
            return;
        }

        if (cardNumber.isEmpty() || holder.isEmpty() || cvc.isEmpty()) {
            statusLabelAddCard.setText("Please fill all the fields");
            return;
        }

        if (!cardNumber.matches("[0-9]+") || !cvc.matches("[0-9]+")) {
            statusLabelAddCard.setText("Please enter a valid card number or cvc");
            return;
        }

        if (cvc.length() != 3) {
            statusLabelAddCard.setText("Please enter a valid cvc");
            return;
        }

        int cardNumberInt = 0;
        int cvcInt = 0;

        try{
            cardNumberInt = Integer.parseInt(cardNumber);
            cvcInt = Integer.parseInt(cvc);
        } catch (Exception e) {
            statusLabelAddCard.setText("Please enter a valid card number or cvc");
            return;
        }

        if (cardNumberInt < 0 || cvcInt < 0) {
            statusLabelAddCard.setText("Please enter a valid card number or cvc");
            return;
        }

        walletFacade.addCard(cardNumber, holder, cvc, date);

        statusLabelAddCard.setText("Card added");
        initializeCards();
        updateCards();
        updateListCard();
    }

    private void updateCards() { 
        cardComboBox.setItems(loadDefaultCardComboBox());
        defaultCardComboBox.setItems(loadDefaultCardComboBox());
    }

    private void updateListCard(){
        cardListContainer.getChildren().clear();
        List<Card> cards = walletFacade.getCurrentCards();
        for (Card card : cards) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/polytech/SocialBucket/wallet/cardComponent.fxml"));
                Pane cardContent = loader.load();
                CardComponent cardComponent = loader.getController();
                cardComponent.loadCard(card, this::updateCards);
                cardListContainer.getChildren().add(cardContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

