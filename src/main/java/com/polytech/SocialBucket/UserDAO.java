package com.polytech.SocialBucket;

// Singleton Design Pattern
public abstract class UserDAO {

    protected static UserDAO userDAO;

    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new PostgreSQLUserDAO();
        }
        return userDAO;
    }

    public abstract User getUserByUsername(String username);

    public abstract User login(String username, String password);

}
