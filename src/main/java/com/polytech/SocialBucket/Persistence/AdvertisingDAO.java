package com.polytech.SocialBucket.Persistence;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLAdvertisingDAO;

public abstract class AdvertisingDAO {
 protected static AdvertisingDAO advertisingDAO;

 public static AdvertisingDAO getAdvertisingDAO() {
  if (advertisingDAO == null) {
   advertisingDAO = new PostgreSQLAdvertisingDAO();
  }
  return advertisingDAO;
 }

 public abstract Boolean createAdvertising(String text, String link, File image, User user, int duration)
   throws IOException;

 public abstract Boolean deleteAdvertising(int advertisingId);

 public abstract List<Advertising> getAdvertisingByUser(User user);

}
