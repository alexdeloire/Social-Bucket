package com.polytech.SocialBucket.Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.UserFacade;
import com.polytech.SocialBucket.Logic.Wallet;

public class PostgreSQLWalletDAO extends WalletDAO {

    @Override
    public float getBalance() {

        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        float resultat = 0;  
        String sql = "SELECT balance FROM public.wallet WHERE iduser = ?";
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    resultat = resultSet.getFloat("balance");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return resultat;
    }

    @Override
    public Wallet getCurrentWallet(){

        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();
        
        String sql = "SELECT * FROM public.wallet WHERE iduser = ?";
        Wallet wallet = new Wallet();  // Create a new Wallet object to populate
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve data from the result set and populate the Wallet object
                    wallet.setBalance(resultSet.getFloat("balance"));
                    wallet.setSecreteCode(resultSet.getInt("secret_code"));
                    wallet.setDefaultCard(buildCard(resultSet.getInt("iddefaultcard")));                  
                    wallet.setCards(cardsByUser(resultSet.getInt("iduser")));
                    
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wallet;
    }

    private Card buildCard(int idcard) {
        String sql = "SELECT * FROM public.card WHERE idcard = ?";
        UserDAO userDAO = UserDAO.getUserDAO();
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, idcard);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String cardNumber = resultSet.getString("card_number");  // Correct column name
                    String holder = resultSet.getString("holder");
                    LocalDate validThru = resultSet.getDate("valid_thru").toLocalDate();  // Correct column name and conversion to LocalDate
                    String cvc = resultSet.getString("cvc");
                    User user = userDAO.getUserById(resultSet.getInt("iduser"));

                    // Create and return the Card object
                    return new Card(cardNumber, holder, validThru, cvc, user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if card not found or an exception occurs
    }

    private List<Card> cardsByUser(int iduser) {
        String sql = "SELECT * FROM public.card WHERE iduser = ?";
        List<Card> cards = new ArrayList<>();
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Card card = buildCard(resultSet.getInt("idcard"));
                    cards.add(card);
                }
                return cards;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if card not found or an exception occurs
    }

    @Override
    public void addCard(String cardNumber, String holder, String cvc, LocalDate dateThru) {
        
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        String sql = "INSERT INTO public.card (card_number, holder, valid_thru, cvc, iduser) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, holder);
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateThru)); // Convert LocalDate to java.sql.Date
            preparedStatement.setString(4, cvc);
            preparedStatement.setInt(5, iduser);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCard(String cardNumber) {
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        String sql = "DELETE FROM public.card WHERE iduser = ? AND card_number = ?";
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, iduser);
            preparedStatement.setString(2, cardNumber);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
