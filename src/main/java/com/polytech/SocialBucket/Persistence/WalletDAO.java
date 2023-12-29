package com.polytech.SocialBucket.Persistence;

import java.time.LocalDate;
import java.util.List;

import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Wallet;

public abstract class WalletDAO {

 protected static WalletDAO WalletDAO;

 public static WalletDAO getWalletDAO() {
  if (WalletDAO == null) {
   WalletDAO = new PostgreSQLWalletDAO();
  }
  return WalletDAO;
 }
 
 // Récupère la somme sur le portefeuille
 public abstract float getBalance();
 public abstract Wallet getCurrentWallet() ;
 public abstract void addCard(String cardNumber, String holder, String cvc, LocalDate dateThru);

 public abstract void deleteCard(String cardNumber);


 


}
