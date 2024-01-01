package com.polytech.SocialBucket.Logic.Facade;

import java.time.LocalDate;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Wallet;
import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.CommentDAO;
import com.polytech.SocialBucket.Persistence.WalletDAO;

public class WalletFacade {

    private static WalletFacade walletFacade;
    private Wallet currentWallet;

    public Wallet getCurrentWallet() {
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        currentWallet = walletDAO.getCurrentWallet();
        return currentWallet;
    }

    public List<Card> getCurrentCards() {
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        List<Card> currentCards = walletDAO.cardsByUser(iduser);
        return currentCards;
    }

    public void modifyDefaultCard(String selectedCardNumber) {
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        walletDAO.modifyDefaultCard(selectedCardNumber);
    }

    public void modifySecretCode(int iduser, int currentSecretCode, int newSecretCode) {

        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        walletDAO.modifySecretCode(iduser, currentSecretCode, newSecretCode);
    }

    public static WalletFacade getInstance() {
        if (walletFacade == null) {
            walletFacade = new WalletFacade();
        }
        return walletFacade;
    }

    public void addCard(String cardNumber, String holder, String cvc, LocalDate dateThru) {
        WalletDAO walletDAO = WalletDAO.getWalletDAO();
        walletDAO.addCard(cardNumber, holder, cvc, dateThru);
    }

    public float getBalance() {

        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        float balance = walletDAO.getBalance();
        return balance;
    }

    public void deleteCard(Card card) {
        WalletDAO walletDAO = WalletDAO.getWalletDAO();
        walletDAO.deleteCard(card.getCardNumber());
    }

    public void chargeMoney(int amount) {

        WalletDAO walletDAO = WalletDAO.getWalletDAO();
        walletDAO.chargeMoney(amount);
    }

}
