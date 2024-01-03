package com.polytech.SocialBucket.Logic.Facade;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.AdvertisingDAO;

public class AdvertisingFacade {
 private static AdvertisingFacade advertisingFacade;
 private Advertising currentAdvertising;

 public static AdvertisingFacade getInstance() {
  if (advertisingFacade == null) {
   advertisingFacade = new AdvertisingFacade();
  }
  return advertisingFacade;
 }

 public Advertising getCurrentAdvertising() {
  return currentAdvertising;
 }

 public Boolean createAdvertising(String text, String link, File image, User user, int duration) throws IOException {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  AdvertisingDAO advertisingDAO = factory.getAdvertisingDAO();
  Boolean created = advertisingDAO.createAdvertising(text, link, image, user, duration);
  return created;
 }

 public Boolean deleteAdvertising(int advertisingId) {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  AdvertisingDAO advertisingDAO = factory.getAdvertisingDAO();
  Boolean deleted = advertisingDAO.deleteAdvertising(advertisingId);
  return deleted;
 }

 public List<Advertising> getAdvertisingByUser(User user) {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  AdvertisingDAO advertisingDAO = factory.getAdvertisingDAO();
  List<Advertising> advertisings = advertisingDAO.getAdvertisingByUser(user);
  return advertisings;
 }

}
