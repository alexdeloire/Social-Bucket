package com.polytech.SocialBucket;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.junit.Test;

import com.polytech.SocialBucket.*;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLDAOFactory;
import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.UserDAO;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;

/**
 * Unit test for simple App.
 */
public class ProfileTest {
    /**
     * Register two test users
     */
    @Test
    public void testRegisterAndLogin() {

        // Delete test users if they already exist
        String sql = "DELETE FROM \"user\" WHERE username = 'test1' OR username = 'test2'";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // User 1
        String username1 = "test1";
        String password1 = "mdp1";
        String mail1 = "test1@test1.com";

        // User 2
        String username2 = "test2";
        String password2 = "mdp2";
        String mail2 = "test2@test2.com";

        UserFacade userFacade = UserFacade.getInstance();

        userFacade.register(username1, mail1, password1);
        userFacade.register(username2, mail2, password2);

        User user1 = userFacade.login(username1, password1);
        User user2 = userFacade.login(username2, password2);

        assertTrue(user1.getUsername().equals(username1));
        assertTrue(user1.getMail().equals(mail1));
        assertTrue(user1.getPassword().equals(password1));
        assertTrue(user2.getUsername().equals(username2));
        assertTrue(user2.getMail().equals(mail2));
        assertTrue(user2.getPassword().equals(password2));
    }

    /**
     * Follow/Unfollow test
     */
    @Test
    public void testFollow() {

        // User 1
        String username1 = "test1";
        // User 2
        String username2 = "test2";

        UserFacade userFacade = UserFacade.getInstance();

        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        UserDAO userDAO = factory.getUserDAO();

        User user1 = userDAO.getUserByUsername(username1);
        User user2 = userDAO.getUserByUsername(username2);

        System.out.println(user1.getId());
        System.out.println(user2.getId());

        // User 1 follows User 2
        userFacade.followUser(user1.getId(), user2.getId());

        // Check if User 1 is following User 2
        assertTrue(userFacade.isFollowing(user1.getId(), user2.getId()));

        // User 1 unfollows User 2
        userFacade.unfollowUser(user1.getId(), user2.getId());

        // Check if User 1 is not following User 2
        assertTrue(!userFacade.isFollowing(user1.getId(), user2.getId()));

    }

    /**
     * Search user test
     */
    @Test
    public void testSearchUser() {

        // Assuming your UserFacade has a searchUser method
        UserFacade userFacade = UserFacade.getInstance();

        // Search for users with the keyword "test"
        List<User> searchResults = userFacade.searchUsers("test");

        // Assert that the search results include users with usernames "test1" and
        // "test2"
        assertTrue(searchResults.stream().anyMatch(user -> user.getUsername().equals("test1")));
        assertTrue(searchResults.stream().anyMatch(user -> user.getUsername().equals("test2")));
    }

    /**
     *  Test the number of followers
     */
    @Test
    public void testNumberOfFollowers() {

        // User 1
        String username1 = "test1";
        // User 2
        String username2 = "test2";

        UserFacade userFacade = UserFacade.getInstance();

        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        UserDAO userDAO = factory.getUserDAO();

        User user1 = userDAO.getUserByUsername(username1);
        User user2 = userDAO.getUserByUsername(username2);

        // User 1 follows User 2
        userFacade.followUser(user1.getId(), user2.getId());

        // Check if User 1 is following User 2
        assertTrue(userFacade.isFollowing(user1.getId(), user2.getId()));

        user1 = userDAO.getUserByUsername(username1);
        user2 = userDAO.getUserByUsername(username2);

        // Check if User 2 has 1 follower
        assertTrue(user2.getNbFollowers() == 1);

        // Check if User 1 has 0 follower
        assertTrue(user1.getNbFollowers() == 0);

        // Check if User 1 has 1 following
        assertTrue(user1.getNbFollowing() == 1);

    }

}