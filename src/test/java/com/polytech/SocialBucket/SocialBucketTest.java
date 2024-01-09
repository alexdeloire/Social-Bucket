package com.polytech.SocialBucket;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;



import org.junit.Test;

import com.polytech.SocialBucket.*;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLDAOFactory;
import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.UserDAO;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.Logic.Facade.CommentFacade;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Logic.Facade.WalletFacade;

/**
 * Unit test for simple App.
 */
public class SocialBucketTest {
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

    /**
     * Add card test
     */
    @Test
    public void testAddCard() {

        

        String username1 = "test1";
        String password1 = "mdp1";

        // Log in the user
        UserFacade userFacade = UserFacade.getInstance();
        User user1 = userFacade.login(username1, password1);
        int iduser = user1.getId();

        // Delete cards if they already exist
        String sql = "DELETE FROM \"card\" WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, iduser);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        WalletFacade walletFacade = WalletFacade.getInstance();
        List<Card> currentCardsBefore = walletFacade.getCurrentCards();

        // Add a card to the user's wallet
        LocalDate currentDate = LocalDate.now();
        walletFacade.addCard("1234567890", "test1","213", currentDate);
       
        List<Card> currentCardsAfter = walletFacade.getCurrentCards();


        assertTrue(currentCardsBefore.size() == 0);
        assertTrue(currentCardsAfter.size() == 1);
        assertTrue(currentCardsAfter.get(0).getCardNumber().equals("1234567890"));
        assertTrue(currentCardsAfter.get(0).getCvc().equals("213"));
        assertTrue(currentCardsAfter.get(0).getHolder().equals("test1"));
        assertTrue(currentCardsAfter.get(0).getValidThru().equals(currentDate));        
        assertTrue(currentCardsAfter.get(0).getUser().getId()==(user1.getId()));
        
       
       
    }

    /**
     * Add post test
     * @throws IOException
     */
    @Test
    public void testAddPost() throws IOException {

               
        String username1 = "test1";
        String password1 = "mdp1";

        // Log in the user
        UserFacade userFacade = UserFacade.getInstance();
        User user1 = userFacade.login(username1, password1);
        int iduser = user1.getId();

        // Delete cards if they already exist
        String sql = "DELETE FROM \"post\" WHERE iduser =  ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, iduser);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        PostFacade postFacade = PostFacade.getInstance();
        List<Post> postsBefore = postFacade.getPostsByUser(user1);

        // Add a post of a user
        String text = "Mon premier post";
        String type = "texte";

        postFacade.createPost(text,type,null,user1);
       
        List<Post> postsAfter = postFacade.getPostsByUser(user1);

        
        assertTrue(postsBefore.size() == 0);
        assertTrue(postsAfter.size() == 1);
        assertTrue(postsAfter.get(0).getText().equals(text));
        assertTrue(postsAfter.get(0).getType().equals(type));       
        assertTrue(postsAfter.get(0).getUser().getId()==(user1.getId()));
        
       
       
    }
    /**
     * Add comment test
     * @throws IOException
     */
    @Test
    public void testAddComment() throws IOException{

        String username1 = "test1";
        String password1 = "mdp1";
       
        // Log in the user
        UserFacade userFacade = UserFacade.getInstance();
        User user1 = userFacade.login(username1, password1);
        int iduser = user1.getId();

        String sql = "DELETE FROM \"comment\" WHERE iduser = ? ";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iduser);    
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // Add a post of a user
        String text = "Mon premier post";
        String type = "texte";

        PostFacade postFacade = PostFacade.getInstance();
        CommentFacade commentFacade = CommentFacade.getInstance();

        postFacade.createPost(text,type,null,user1);
       
        
        // Get the post created earlier
        Post newPost = postFacade.getPostsByUser(user1).get(0);
        
        // Get the comment for the post created just before
        List<Comment> commentsBefore = commentFacade.getComments(newPost);


        // Create a comment
        String content = "Mon premier commentaire !";
        commentFacade.addComment(content, newPost);
        
        List<Comment> commentsAfter = commentFacade.getComments(newPost);

        
        assertTrue(commentsBefore.size() == 0);
        assertTrue(commentsAfter.size() == 1);
        
        
       
       
    }

    @Test
    public void testAddAdvertising() throws IOException {

        String username1 = "test1";
        String password1 = "mdp1";

        // Log in the user
        UserFacade userFacade = UserFacade.getInstance();
        User user1 = userFacade.login(username1, password1);
        int iduser = user1.getId();

        String sql = "DELETE FROM \"advertising\" WHERE iduser = ? ";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iduser);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Add a advertising of a user
        String text = "Mon premier post";
        String link = "https://www.google.com/";
        int duration = 10;

        String imagePath = "com/polytech/SocialBucket/UI/Icones/avatar.png";
        String absolutePath = new File("src/main/java", imagePath).getAbsolutePath();
        File imageFile = new File(absolutePath);

        AdvertisingFacade advertisingFacade = AdvertisingFacade.getInstance();

        advertisingFacade.createAdvertising(text, link, imageFile, user1, duration);

        // Get the advertising created earlier
        List<Advertising> advertisings = advertisingFacade.getAdvertisingByUser(user1);
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusMonths(duration);

        assertTrue(advertisings.size() == 1);
        assertTrue(advertisings.get(0).getText().equals(text));
        assertTrue(advertisings.get(0).getLink().equals(link));
        assertTrue(advertisings.get(0).getBegindate().equals(currentDate));
        assertTrue(advertisings.get(0).getEnddate().equals(endDate));
        assertTrue(advertisings.get(0).getUser().getId() == (user1.getId()));
    }

}