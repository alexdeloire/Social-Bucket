package com.polytech.SocialBucket;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.polytech.SocialBucket.*;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.UserFacade;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * Login test
     */
    @Test  
    public void testLogin() {
        
        // Real user
        String username = "u1";
        String password = "mdp1";
        String mail = "utilisateur1@example.com";

        // Fake user
        String username2 = "u2";
        String password2 = "mdp2";
        String mail2 = "fakeemail@example.com";

        UserFacade userFacade = UserFacade.getInstance();
        User userTest = new User(username, mail, password);

        User userDB = userFacade.login(username, password);
        User shouldBeNull = userFacade.login(username2, password2);

        assertTrue( userTest.getUsername().equals(userDB.getUsername()) );
        assertTrue( userTest.getMail().equals(userDB.getMail()) );
        assertTrue( userTest.getPassword().equals(userDB.getPassword()) ); 
        assertTrue( shouldBeNull == null );
    }

}
