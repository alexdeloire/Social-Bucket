package com.polytech.SocialBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class PostgreSQLUserDAO extends UserDAO {

    @Override
    public User getUserByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM public.\"user\" WHERE username = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the data from the row
                    String pseudo = resultSet.getString("username");
                    String mail = resultSet.getString("mail");
                    String password = resultSet.getString("password");

                    // Create a new User object
                    user = new User(pseudo, mail, password);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User login(String username, String password) {

        User user_to_verify = getUserByUsername(username);

        if (user_to_verify != null && password.equals(user_to_verify.getPassword())) {
            return user_to_verify;
        } else {
            return null;
        }
    }
}
