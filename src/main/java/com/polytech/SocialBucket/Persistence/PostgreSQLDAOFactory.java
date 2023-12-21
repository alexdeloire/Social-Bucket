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

        String url = "jdbc:postgresql://localhost:5432/SocialBucketDB";
        String user = "postgres";
        String password = "123";

        return DriverManager.getConnection(url, user, password);
    }

}
