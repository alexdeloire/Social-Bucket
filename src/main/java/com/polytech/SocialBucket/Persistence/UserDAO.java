package com.polytech.SocialBucket.Persistence;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLUserDAO;
import java.util.List;

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

    public abstract boolean register(String username, String mail, String password);

    public abstract boolean addUser(User user);

    public abstract User getUserById(int id);

    public abstract int getNbFollowersById(int id);

    public abstract int getNbFollowingById(int id);

    public abstract boolean followUser(int idUser, int idUserToFollow);

    public abstract boolean unfollowUser(int idUser, int idUserToUnfollow);

    public abstract boolean isFollowing(int idUser, int idUserToFollow);

    public abstract List<User> searchUsers(String query);

    public abstract boolean updateUser(User user);

    public abstract boolean verifyPassword(int idUser, String password);
}
