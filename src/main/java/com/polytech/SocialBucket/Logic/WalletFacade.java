package com.polytech.SocialBucket.Logic;

import java.time.LocalDate;
import java.util.List;

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



    public static WalletFacade getInstance() {
        if (walletFacade == null) {
         walletFacade = new WalletFacade();
        }
        return walletFacade;
    }

    public void addCard(String cardNumber, String holder, String cvc, LocalDate dateThru){
        WalletDAO walletDAO = WalletDAO.getWalletDAO();
        walletDAO.addCard(cardNumber, holder, cvc, dateThru);
    }


  

    public float getBalance(){
        
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        WalletDAO walletDAO = factory.getWalletDAO();

        float balance = walletDAO.getBalance();
        return balance;
    }



    public void deleteCard(Card card) {
        WalletDAO walletDAO = WalletDAO.getWalletDAO();
        walletDAO.deleteCard(card.getCardNumber());
    }
    

    

}
