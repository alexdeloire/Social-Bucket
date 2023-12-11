package com.polytech.SocialBucket.Persistence;

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

}
