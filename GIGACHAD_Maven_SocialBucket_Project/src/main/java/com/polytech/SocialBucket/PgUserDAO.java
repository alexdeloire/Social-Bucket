package com.polytech.SocialBucket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class PgUserDAO extends UserDAO {
    

    
    

    @Override
    public User getUserByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM public.\"user\" WHERE pseudo = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Récupération des données de l'utilisateur depuis le ResultSet
                    String pseudo = resultSet.getString("pseudo");
                    String mail = resultSet.getString("mail");
                    String mdp = resultSet.getString("mdp");

                    // Création d'un objet User avec les données récupérées
                    user = new User(pseudo, mail, mdp);
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
        
        if (user_to_verify != null && password.equals(user_to_verify.getMdp())) {
            return user_to_verify;
        } else {
            return null;
        }
    }
}
