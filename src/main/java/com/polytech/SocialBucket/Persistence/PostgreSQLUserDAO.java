package com.polytech.SocialBucket.Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.polytech.SocialBucket.Logic.User;

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
                    String usernameDB = resultSet.getString("username");
                    String mail = resultSet.getString("mail");
                    String password = resultSet.getString("password");

                    // Create a new User object
                    user = new User(usernameDB, mail, password);
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

    @Override
    public boolean register(String username, String mail, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            User new_user = new User(username, mail, password);
            return addUser(new_user);
        } else {
            return false;
        }
    }
 
    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO public.\"user\" (username, mail, password) VALUES (?, ?, ?)";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getMail());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
