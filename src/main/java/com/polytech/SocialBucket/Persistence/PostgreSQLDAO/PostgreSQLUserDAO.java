package com.polytech.SocialBucket.Persistence.PostgreSQLDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.UserDAO;

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
                    int id = resultSet.getInt("iduser");

                    // Create a new User object
                    user = new User(usernameDB, mail, password, id);

                    // Get the number of followers
                    user.setNbFollowers(getNbFollowersById(id));
                    // Get the number of following
                    user.setNbFollowing(getNbFollowingById(id));
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
            addUser(new_user);
            User user_after = getUserByUsername(username);
            addWallet(user_after, 0);
            return true;
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

    public boolean addWallet(User user, float balance) {
        if (user == null) {
            // Handle the case where the user's ID is null (perhaps log an error or throw an
            // exception)
            System.err.println("User ID is null. Cannot add wallet.");
            return false;
        }

        String insertWalletSQL = "INSERT INTO public.wallet (balance, iduser, secret_code) VALUES (?, ?, ?)";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement insertWalletStatement = connection.prepareStatement(insertWalletSQL)) {

            // Add a wallet with the specified balance and the user's ID
            insertWalletStatement.setFloat(1, balance);
            insertWalletStatement.setInt(2, user.getId());
            insertWalletStatement.setInt(3, 123); // Adjust the secret_code value as needed
            insertWalletStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM public.\"user\" WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the data from the row
                    String usernameDB = resultSet.getString("username");
                    String mail = resultSet.getString("mail");
                    String password = resultSet.getString("password");

                    // Create a new User object
                    user = new User(usernameDB, mail, password, id);

                    // Get the number of followers
                    user.setNbFollowers(getNbFollowersById(id));
                    // Get the number of following
                    user.setNbFollowing(getNbFollowingById(id));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public int getNbFollowersById(int id) {
        int nbFollowers = 0;
        String sql = "SELECT COUNT(*) FROM public.follow WHERE idfollowed = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the data from the row
                    nbFollowers = resultSet.getInt("count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbFollowers;
    }

    public boolean followUser(int iduser, int idFollowed) {
        String sql = "INSERT INTO public.follow (iduser, idfollowed) VALUES (?, ?)";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, iduser);
            preparedStatement.setInt(2, idFollowed);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unfollowUser(int iduser, int idFollowed) {
        String sql = "DELETE FROM public.follow WHERE iduser = ? AND idfollowed = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, iduser);
            preparedStatement.setInt(2, idFollowed);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFollowing(int iduser, int idFollowed) {
        String sql = "SELECT * FROM public.follow WHERE iduser = ? AND idfollowed = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, iduser);
            preparedStatement.setInt(2, idFollowed);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getNbFollowingById(int id) {
        int nbFollowing = 0;
        String sql = "SELECT COUNT(*) FROM public.follow WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the data from the row
                    nbFollowing = resultSet.getInt("count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbFollowing;
    }

    public List<User> searchUsers(String query) {
        String sql = "SELECT * FROM public.\"user\" WHERE username LIKE ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + query + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Get the data from the row
                    String usernameDB = resultSet.getString("username");
                    String mail = resultSet.getString("mail");
                    String password = resultSet.getString("password");
                    int id = resultSet.getInt("iduser");

                    // Create a new User object
                    User user = new User(usernameDB, mail, password, id);

                    // Get the number of followers
                    user.setNbFollowers(getNbFollowersById(id));
                    // Get the number of following
                    user.setNbFollowing(getNbFollowingById(id));

                    users.add(user);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE public.\"user\" SET username = ?, mail = ?, password = ? WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getMail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyPassword(int idUser, String password) {
        String sql = "SELECT * FROM public.\"user\" WHERE iduser = ? AND password = ?";
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUser);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
