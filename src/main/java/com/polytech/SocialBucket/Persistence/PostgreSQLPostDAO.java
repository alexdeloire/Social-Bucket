package com.polytech.SocialBucket.Persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.User;

import javafx.scene.image.Image;

import java.sql.DriverManager;

public class PostgreSQLPostDAO extends PostDAO {

  @Override
  public Post createPost(String text, String type, File file, User user) throws IOException {
    String sql = "INSERT INTO public.\"post\" (text, type, file, filename, iduser) VALUES (?, ?, ?,?,?)";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      byte[] fileBytes = Files.readAllBytes(file.toPath());
      String fileName = file.getName();
      System.out.println(text);
      System.out.println(type);
      System.out.println(fileBytes);
      preparedStatement.setString(1, text);
      preparedStatement.setString(2, type);
      preparedStatement.setBytes(3, fileBytes);
      preparedStatement.setString(4, fileName);
      preparedStatement.setInt(5, user.getId());

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            int idpost = generatedKeys.getInt(1);
            System.out.println("post " + idpost + " created");
            // Retournez la nouvelle instance de Post avec l'ID généré
            return new Post(text, type, file, fileName, user, idpost);
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
    // TODO Auto-generated method stub
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
          File file = null;
          System.out.println(fileName);
          try {
            file = new File(fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
              fos.write(fileBytes);
            }

          } catch (IOException e) {
            e.printStackTrace();
          }

          // Create a new Post object and add it to the list
          Post post = new Post(text, type, file, fileName, user, id);
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

}
