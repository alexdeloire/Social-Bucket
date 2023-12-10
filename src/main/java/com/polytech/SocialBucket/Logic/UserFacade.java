package com.polytech.SocialBucket.Logic;

import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.UserDAO;

// Singleton Design Pattern
public class UserFacade {

    private static UserFacade userFacade;
    private User currentUser;

    public static UserFacade getInstance() {
        if (userFacade == null) {
            userFacade = new UserFacade();
        }
        return userFacade;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public User login(String username, String password) {
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        UserDAO userDAO = factory.getUserDAO();
        User user = userDAO.login(username, password);
        this.currentUser = user;
        return user;
    }

    public boolean register(String username, String mail, String password) {
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        UserDAO userDAO = factory.getUserDAO();
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            user = new User(username, mail, password);
            userDAO.addUser(user);
            return true;
        } else {
            return false;
        }
    }
}
