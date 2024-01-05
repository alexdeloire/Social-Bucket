package com.polytech.SocialBucket.Persistence;

import java.time.LocalDate;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Wallet;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLWalletDAO;

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

 public abstract Wallet getCurrentWallet();

 public abstract void addCard(String cardNumber, String holder, String cvc, LocalDate dateThru);

 public abstract List<Card> cardsByUser(int iduser);

 public abstract void deleteCard(String cardNumber);

 public abstract void modifyDefaultCard(String selectedCardNumber);

 public abstract void modifySecretCode(int iduser, int currentSecretCode, int newSecretCode);

 public abstract void chargeMoney(int amount);

 public abstract boolean checkSecretCode(int iduser, int secretCode);

 public abstract boolean pay(int iduser, int amount);

}
