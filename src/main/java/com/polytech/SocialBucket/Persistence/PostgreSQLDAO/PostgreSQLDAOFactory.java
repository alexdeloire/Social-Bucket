package com.polytech.SocialBucket.Persistence.PostgreSQLDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;

public class PostgreSQLDAOFactory extends AbstractDAOFactory {

    public static Connection getConnection() throws SQLException {
        try {
            // Load the driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://dpg-cm6qfben7f5s73caq5fg-a.frankfurt-postgres.render.com/socialbucketdb";
        String user = "user1";
        String password = "5h0QoqqHWezddZpqf9VhjKKlda8fJ653";

        // String url = "jdbc:postgresql://localhost:5432/SocialBucketDB";
        // String user = "postgres";
        // String password = "123";

        return DriverManager.getConnection(url, user, password);
    }

}
