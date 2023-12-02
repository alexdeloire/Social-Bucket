package com.polytech.SocialBucket;

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
        return user;
    }

}
