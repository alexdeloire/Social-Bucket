package com.polytech.SocialBucket.Persistence;

import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLDAOFactory;

// Abstract Factory Design Pattern
// Singleton Design Pattern
public abstract class AbstractDAOFactory {

  protected static AbstractDAOFactory factory;

  public static AbstractDAOFactory getFactory() {
    if (factory == null) {
      factory = new PostgreSQLDAOFactory();
    }
    return factory;
  }

  public UserDAO getUserDAO() {
    return UserDAO.getUserDAO();
  };

  public PostDAO getPostDAO() {
    return PostDAO.getPostDAO();
  };

  public CommentDAO getCommentDAO() {
    return CommentDAO.getCommentDAO();
  }

  public WalletDAO getWalletDAO() {
    return WalletDAO.getWalletDAO();
  }

}
