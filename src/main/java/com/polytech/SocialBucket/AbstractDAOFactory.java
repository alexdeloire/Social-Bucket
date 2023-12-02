package com.polytech.SocialBucket;

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

}
