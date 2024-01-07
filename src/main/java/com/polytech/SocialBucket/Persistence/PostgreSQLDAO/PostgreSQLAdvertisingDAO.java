package com.polytech.SocialBucket.Persistence.PostgreSQLDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Date;
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
import com.polytech.SocialBucket.Persistence.AdvertisingDAO;

public class PostgreSQLAdvertisingDAO extends AdvertisingDAO {

  @Override
  public Boolean createAdvertising(String text, String link, File image, User user, int duration) throws IOException {
    String sql = "INSERT INTO advertising (iduser, begin_date, end_date, text, link, image) VALUES(?,?,?,?,?,?)";
    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, user.getId());

      LocalDate currentDate = LocalDate.now();
      preparedStatement.setDate(2, Date.valueOf(currentDate));

      LocalDate endDate = currentDate.plusMonths(duration);
      preparedStatement.setDate(3, Date.valueOf(endDate));

      preparedStatement.setString(4, text);
      preparedStatement.setString(5, link);

      byte[] fileBytes = Files.readAllBytes(image.toPath());
      preparedStatement.setBytes(6, fileBytes);

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return true;
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;

  }

  @Override
  public Boolean deleteAdvertising(int advertisingId) {
    String sql = "DELETE FROM public.\"advertising\" WHERE idadvertising = ?";

    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, advertisingId);
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows > 0) {
        System.out.println("advertising " + advertisingId + " deleted");
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public List<Advertising> getAdvertisingByUser(User user) {
    List<Advertising> advertisings = new ArrayList<>();

    String sql = "SELECT * FROM public.\"advertising\" WHERE iduser = ? ORDER BY end_date DESC";
    try (Connection connection = PostgreSQLDAOFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return advertisings;
  }

}
