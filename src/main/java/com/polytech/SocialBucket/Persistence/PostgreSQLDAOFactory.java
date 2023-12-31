package com.polytech.SocialBucket.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

        return DriverManager.getConnection(url, user, password);
    }

}
