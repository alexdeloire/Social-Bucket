package com.polytech.SocialBucket.Logic;

import java.time.LocalDate;

public class Card {
    
    private User user;
    private String cardNumber;
    private String holder;
    private LocalDate validThru;
    private String cvc;

    public Card(String cardNumber, String holder, LocalDate validThru, String cvc, User user) {
        this.cardNumber = cardNumber;
        this.holder = holder;
        this.validThru = validThru;
        this.cvc = cvc;
        this.user = user;
    }


    // Getter and Setter

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getHolder() {
        return holder;
    }
    public void setHolder(String holder) {
        this.holder = holder;
    }
    public LocalDate getValidThru() {
        return validThru;
    }
    public void setValidThru(LocalDate validThru) {
        this.validThru = validThru;
    }
    public String getCvc() {
        return cvc;
    }
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

}
