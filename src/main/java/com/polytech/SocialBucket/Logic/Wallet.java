package com.polytech.SocialBucket.Logic;

import java.util.List;

public class Wallet {
    
    private float balance ;
    private List<Card> cards;
    private Card defaultCard;
    private int secreteCode;
    private User user;

   // private List<Payment> paymentHistory;


    public Wallet(float balance, User user) {
        this.balance = balance;
        this.user = user;
    }

    // Getter Setters
   public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public Card getDefaultCard() {
        return defaultCard;
    }
    public void setDefaultCard(Card defaultCard) {
        this.defaultCard = defaultCard;
    }
    public int getSecreteCode() {
        return secreteCode;
    }
    public void setSecreteCode(int secreteCode) {
        this.secreteCode = secreteCode;
    }
   
}
