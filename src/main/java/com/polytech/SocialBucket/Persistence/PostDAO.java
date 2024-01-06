package com.polytech.SocialBucket.Persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.scene.image.Image;

import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLPostDAO;
import com.polytech.SocialBucket.Logic.Reaction;

// Singleton Design Pattern
public abstract class PostDAO {

 protected static PostDAO userDAO;

 public static PostDAO getPostDAO() {
  if (userDAO == null) {
   userDAO = new PostgreSQLPostDAO();
  }
  return userDAO;
 }

 public abstract Post createPost(String text, String type, File file, User user) throws IOException;

 public abstract List<Post> getPostsByUser(User user);

 public abstract Boolean deletePost(int postId);

 public abstract Boolean addReaction(String typeReaction, Post post, User user);

 public abstract Boolean deleteReaction(String typeReaction, Post post, User user);

 public abstract List<Object> getNews(User user);
}
