package com.polytech.SocialBucket.Persistence;

import com.polytech.SocialBucket.Logic.User;

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

    public abstract void addUser(User user);
}