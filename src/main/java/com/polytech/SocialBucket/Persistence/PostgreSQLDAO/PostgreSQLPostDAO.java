package com.polytech.SocialBucket.Persistence.PostgreSQLDAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.PostDAO;
import com.polytech.SocialBucket.Persistence.UserDAO;

import javafx.scene.image.Image;

import java.sql.DriverManager;

public class PostgreSQLPostDAO extends PostDAO {

  @Override
  public Post createPost(String text, String type, File file, User user) throws IOException {
    String sql = "INSERT INTO public.\"post\" (text, type, file, filename, iduser) VALUES (?, ?, ?,?,?)";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, text);
      preparedStatement.setString(2, type);
      String fileName = null;
      byte[] fileBytes = null;

      if (file != null) {
        fileBytes = Files.readAllBytes(file.toPath());
        fileName = file.getName();
        preparedStatement.setBytes(3, fileBytes);
        preparedStatement.setString(4, fileName);
      } else {
        preparedStatement.setBytes(3, null);
        preparedStatement.setString(4, null);
      }

      preparedStatement.setInt(5, user.getId());

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            int idpost = generatedKeys.getInt(1);
            System.out.println("post " + idpost + " created");
            // Retournez la nouvelle instance de Post avec l'ID généré
            Post post = new Post(text, type, user, idpost);
            if (fileBytes != null) {
              post.setBytes(fileBytes);
              post.setFileName(fileName);
            }
            return post;
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Post> getPostsByUser(User user) {
    List<Post> posts = new ArrayList<>();

    String sql = "SELECT * FROM public.\"post\" WHERE iduser = ?";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, user.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          // Get the data from the row
          String text = resultSet.getString("text");
          String type = resultSet.getString("type");
          String fileName = resultSet.getString("filename");
          byte[] fileBytes = resultSet.getBytes("file");
          int id = resultSet.getInt("idpost");

          // Create a new Post object and add it to the list
          Post post = new Post(text, type, user, id);

          if (fileBytes != null) {
            post.setBytes(fileBytes);
            post.setFileName(fileName);
          }

          String sql2 = "SELECT * FROM public.\"reaction\" WHERE idpost = " + id;
          try (Connection connection2 = PostgreSQLDAOFactory.getConnection();
              PreparedStatement preparedStatement2 = connection2.prepareStatement(sql2)) {
            try (ResultSet resultSet2 = preparedStatement2.executeQuery()) {
              while (resultSet2.next()) {
                String typeReaction = resultSet2.getString("type");
                int iduser = resultSet2.getInt("iduser");
                Reaction reaction = new Reaction(typeReaction, iduser);
                post.addReaction(reaction);
              }
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          posts.add(post);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return posts;
  }

  @Override
  public Boolean deletePost(int postId) {
    String sql = "DELETE FROM public.\"post\" WHERE idpost = ?";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, postId);

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        System.out.println("post " + postId + " deleted");
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public Boolean addReaction(String typeReaction, Post post, User user) {
    String sql = "INSERT INTO public.\"reaction\" (type, idpost, iduser) VALUES (?, ?, ?)";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, typeReaction);
      preparedStatement.setInt(2, post.getId());
      preparedStatement.setInt(3, user.getId());

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        System.out.println("reaction " + typeReaction + " added to post " + post.getId());
        Reaction reaction = new Reaction(typeReaction, user.getId());
        post.addReaction(reaction);
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public Boolean deleteReaction(String typeReaction, Post post, User user) {
    String sql = "DELETE FROM public.\"reaction\" WHERE type = ? AND idpost = ? AND iduser = ?";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setString(1, typeReaction);
      preparedStatement.setInt(2, post.getId());
      preparedStatement.setInt(3, user.getId());

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        System.out.println("reaction " + typeReaction + " deleted from post " + post.getId());
        Reaction reaction = new Reaction(typeReaction, user.getId());
        post.deleteReaction(reaction);
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public List<Object> getNews(User user) {
    List<Post> posts = new ArrayList<>();
    List<Advertising> advertisings = new ArrayList<>();
    List<Object> news = new ArrayList<>();

    String sql = "SELECT * FROM public.\"post\" WHERE iduser IN (SELECT idfollowed FROM public.\"follow\" WHERE iduser = ?) OR iduser = ? ORDER BY idpost DESC";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, user.getId());
      preparedStatement.setInt(2, user.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          // Get the data from the row
          String text = resultSet.getString("text");
          String type = resultSet.getString("type");
          String fileName = resultSet.getString("filename");
          byte[] fileBytes = resultSet.getBytes("file");
          int id = resultSet.getInt("idpost");

          int idauthor = resultSet.getInt("iduser");

          UserDAO userDAO = PostgreSQLUserDAO.getUserDAO();
          User user2 = userDAO.getUserById(idauthor);

          // Create a new Post object and add it to the list
          Post post = new Post(text, type, user2, id);

          if (fileBytes != null) {
            post.setBytes(fileBytes);
            post.setFileName(fileName);
          }

          String sql2 = "SELECT * FROM public.\"reaction\" WHERE idpost = " + id;
          try (Connection connection2 = PostgreSQLDAOFactory.getConnection();
              PreparedStatement preparedStatement2 = connection2.prepareStatement(sql2)) {
            try (ResultSet resultSet2 = preparedStatement2.executeQuery()) {
              while (resultSet2.next()) {
                String typeReaction = resultSet2.getString("type");
                int iduser = resultSet2.getInt("iduser");
                Reaction reaction = new Reaction(typeReaction, iduser);
                post.addReaction(reaction);
              }
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          posts.add(post);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    String sql2 = "SELECT * FROM public.\"advertising\" WHERE end_date >= CURRENT_DATE AND iduser IN (SELECT idfollowed FROM public.\"follow\" WHERE iduser = ?) ORDER BY idadvertising DESC";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {

      preparedStatement.setInt(1, user.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          String text = resultSet.getString("text");
          String link = resultSet.getString("link");
          LocalDate beginDate = resultSet.getDate("begin_date").toLocalDate();
          LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
          byte[] image = resultSet.getBytes("image");
          int id = resultSet.getInt("idadvertising");

          Advertising advertising = new Advertising(user, beginDate, endDate, text, link, image);
          advertising.setId(id);
          advertisings.add(advertising);
        }

      } catch (SQLException e) {
        e.printStackTrace();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    int postIndex = 0;
    int adIndex = 0;

    while (postIndex < posts.size() || adIndex < advertisings.size()) {
      // Ajouter 2 posts à la liste news
      if (postIndex < posts.size()) {
        news.add(posts.get(postIndex));
        postIndex++;
      }
      if (postIndex < posts.size()) {
        news.add(posts.get(postIndex));
        postIndex++;
      }

      // Ajouter 1 ad à la liste news
      if (adIndex < advertisings.size()) {
        news.add(advertisings.get(adIndex));
        adIndex++;
      }
    }

    for (Object n : news) {
      if (n instanceof Post) {
        System.out.println("post");
      }
      if (n instanceof Advertising) {
        System.out.println("ad");
      }
    }

    return news;

  }

}
