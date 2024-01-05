package com.polytech.SocialBucket.Persistence.PostgreSQLDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Card;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Wallet;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.Persistence.UserDAO;
import com.polytech.SocialBucket.Persistence.WalletDAO;

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
                    System.out.println(resultat);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultat;
    }

    @Override
    public Wallet getCurrentWallet() {

        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        String sql = "SELECT * FROM public.wallet WHERE iduser = ?";
        Wallet wallet = new Wallet(); // Create a new Wallet object to populate

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
                    String cardNumber = resultSet.getString("card_number"); // Correct column name
                    String holder = resultSet.getString("holder");
                    LocalDate validThru = resultSet.getDate("valid_thru").toLocalDate(); // Correct column name and
                                                                                         // conversion to LocalDate
                    String cvc = resultSet.getString("cvc");
                    User user = userDAO.getUserById(resultSet.getInt("iduser"));

                    // Create and return the Card object
                    return new Card(cardNumber, holder, validThru, cvc, user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if card not found or an exception occurs
    }

    @Override
    public void modifyDefaultCard(String selectedDefaultCard) {

        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        String sql = "SELECT idcard FROM public.card WHERE card_number = ? and iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, selectedDefaultCard);
            preparedStatement.setInt(2, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_idcard = resultSet.getInt("idcard");

                    // Now you have the new_idcard, you can proceed to update the default card
                    String sql2 = "UPDATE wallet SET iddefaultcard = ? WHERE iduser = ?";

                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                        preparedStatement2.setInt(1, new_idcard);
                        preparedStatement2.setInt(2, iduser);

                        // Use executeUpdate for update queries
                        int affectedRows = preparedStatement2.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("Default card updated successfully.");
                        } else {
                            System.out.println("Failed to update default card.");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getDefaultCard() {
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();
        int defaultCardId = -1; // Valeur par défaut si la carte par défaut n'est pas trouvée

        String sql = "SELECT iddefaultcard FROM public.wallet WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    defaultCardId = resultSet.getInt("iddefaultcard");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return defaultCardId;
    }

    @Override
    public List<Card> cardsByUser(int iduser) {
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
        return null; // Return null if card not found or an exception occurs
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
    public void deleteCard(String selectedDefaultCard) {
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        // Utilisez la fonction getDefaultCard pour obtenir l'ID de la carte par défaut
        // actuelle
        int currentDefaultCardId = getDefaultCard();

        // On récup l'id de la carte selectionné
        String sql = "SELECT idcard FROM public.card WHERE card_number = ? AND iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, selectedDefaultCard);
            preparedStatement.setInt(2, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_idcard = resultSet.getInt("idcard");

                    // Vérifiez si la nouvelle carte est la carte par défaut actuelle
                    if (new_idcard == currentDefaultCardId) {
                        // Si c'est le cas, mettez à jour le wallet pour que la carte par défaut soit
                        // nulle
                        String updateWalletSql = "UPDATE wallet SET iddefaultcard = NULL WHERE iduser = ?";

                        try (PreparedStatement updateWalletStatement = connection.prepareStatement(updateWalletSql)) {
                            updateWalletStatement.setInt(1, iduser);

                            // Utilisez executeUpdate pour les requêtes de mise à jour
                            int affectedRows = updateWalletStatement.executeUpdate();

                            if (affectedRows > 0) {
                                System.out.println("Default card set to null in wallet.");
                            } else {
                                System.out.println("Failed to set default card to null in wallet.");
                            }
                        }
                    }

                    // Maintenant que vous avez new_idcard, vous pouvez procéder à la suppression de
                    // la carte
                    String deleteCardSql = "DELETE FROM public.card WHERE iduser = ? AND idcard = ?";

                    try (PreparedStatement deleteCardStatement = connection.prepareStatement(deleteCardSql)) {
                        deleteCardStatement.setInt(1, iduser);
                        deleteCardStatement.setInt(2, new_idcard);

                        // Utilisez executeUpdate pour les requêtes de suppression
                        int affectedRows = deleteCardStatement.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("Card deleted successfully.");
                        } else {
                            System.out.println("Failed to delete card.");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifySecretCode(int iduser, int currentSecretCode, int newSecretCode) {
        // Validate the currentSecretCode before attempting to modify

        String updateSql = "UPDATE wallet SET secret_code = ? WHERE iduser = ? AND secret_code = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {

            preparedStatement.setInt(1, newSecretCode);
            preparedStatement.setInt(2, iduser);
            preparedStatement.setInt(3, currentSecretCode);

            // Execute the update query
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Secret Code updated successfully.");
            } else {
                System.out.println("Failed to update Secret Code. Please check the current code.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void chargeMoney(int amount) {
        UserFacade userFacade = UserFacade.getInstance();
        int iduser = userFacade.getCurrentUser().getId();

        // Get the current balance
        float currentBalance = getBalance();

        // Calculate the new balance after adding the amount
        float newBalance = currentBalance + amount;

        // Update the balance in the database
        String updateBalanceSql = "UPDATE wallet SET balance = ? WHERE iduser = ?";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceSql)) {

            preparedStatement.setFloat(1, newBalance);
            preparedStatement.setInt(2, iduser);

            // Execute the update query
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Money charged successfully. New balance: " + newBalance);
            } else {
                System.out.println("Failed to charge money.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkSecretCode(int iduser, int secretCode) {
        String sql = "SELECT secret_code FROM public.wallet WHERE iduser = ?";
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, iduser);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int correctSecretCode = resultSet.getInt("secret_code");
                    return secretCode == correctSecretCode;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean pay(int iduser, int amount) {

        float currentBalance = getBalance();
        float newBalance = currentBalance - amount;
        if (newBalance < 0) {
            return false;
        } else {
            String updateBalanceSql = "UPDATE wallet SET balance = ? WHERE iduser = ?";

            try (Connection connection = PostgreSQLDAOFactory.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceSql)) {

                preparedStatement.setFloat(1, newBalance);
                preparedStatement.setInt(2, iduser);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Money paid successfully. New balance: " + newBalance);
                    return true;
                } else {
                    System.out.println("Failed to pay money.");
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }
    }

}
