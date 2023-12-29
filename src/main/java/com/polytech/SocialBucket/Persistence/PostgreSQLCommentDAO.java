package com.polytech.SocialBucket.Persistence;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.User;

import com.polytech.SocialBucket.Logic.UserFacade;

public class PostgreSQLCommentDAO extends CommentDAO {

    @Override
    public Comment addComment(String content, User user, Post post) {
        String sql = "INSERT INTO public.\"comment\" (content, iduser, idpost) VALUES (?, ?, ?)";

        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, content);
        preparedStatement.setInt(2, user.getId());
        preparedStatement.setInt(3, post.getId());
        
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int idcomment = generatedKeys.getInt(1);
                System.out.println("comment " + idcomment + " created");
                // Retournez la nouvelle instance de Comment avec l'ID généré
                return new Comment(idcomment, content, user, post);
            }
            }
        }

        } catch (SQLException e) {
        e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Comment> getComments(Post post) {

        // Récupère la liste de commentaire d'un post 
        UserDAO userDAO = UserDAO.getUserDAO();
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM public.\"comment\" WHERE idpost = ?";

        // se connecte et execute la requête pour recip les commentaires
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, post.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int idcomment = resultSet.getInt("idcomment");
                    String content = resultSet.getString("content");
                    User user = userDAO.getUserById(resultSet.getInt("iduser"));
                    
                    // Créer l'instance du commentaire
                    Comment comment = new Comment(idcomment, content, user, post);
                    List<Reaction> liste = getReactionsByComment(comment);
                    comment.setReactions(liste);
                    
                    comments.add(comment);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("taille: "+ comments.size());
        return comments;
    }

    public List<Reaction> getReactionsByComment(Comment comment){

        List<Reaction> liste_reactions = new ArrayList<>();
        
        String sql2 = "SELECT * FROM public.\"reactioncomment\" WHERE idcomment = ?"  ;
        try (Connection connection2 = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection2.prepareStatement(sql2)) {

            preparedStatement.setInt(1, comment.getId());

            try (ResultSet resultSet2 = preparedStatement.executeQuery()) {
                while (resultSet2.next()) {
                    String typeReaction = resultSet2.getString("type");
                    int iduser = resultSet2.getInt("iduser");
                   
                    Reaction reaction = new Reaction(typeReaction, iduser);
                    liste_reactions.add(reaction);
                }
                
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste_reactions;
    }

    @Override
    public void deleteComment(Comment comment){
        
        UserFacade userFacade = UserFacade.getInstance();

        if(userFacade.getCurrentUser().getId() == comment.getUser().getId()){
            String sql = "DELETE FROM public.\"comment\" WHERE idcomment = ?";
            try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.executeUpdate();
            
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addReaction(String type, Comment comment, User user) {
        String sql = "INSERT INTO public.\"reactioncomment\" (type, idcomment, iduser) VALUES (?, ?, ?)";
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, comment.getId());
            preparedStatement.setInt(3, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Reaction> liste = getReactionsByComment(comment);
        comment.setReactions(liste);
    }

    @Override
    public void deleteReaction(Comment comment, User user) {
        String sql = "DELETE FROM public.\"reactioncomment\" WHERE  idcomment = ? AND iduser = ?";
        
        try (Connection connection = PostgreSQLDAOFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.setInt(2, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Reaction> liste = getReactionsByComment(comment);
        comment.setReactions(liste);
    }


            


}
