package com.polytech.SocialBucket.Logic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.PostDAO;

import javafx.scene.image.Image;

public class PostFacade {

 private static PostFacade postFacade;
 private Post currentPost;

 public static PostFacade getInstance() {
  if (postFacade == null) {
   postFacade = new PostFacade();
  }
  return postFacade;
 }

 public Post getCurrentPost() {
  return this.currentPost;
 }

 public boolean createPost(String text, String type, File file, User user) throws IOException {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  PostDAO postDAO = factory.getPostDAO();
  Post post = postDAO.createPost(text, type, file, user);
  this.currentPost = post;
  return true;
 }

 public List<Post> getPostsByUser(User user) {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  PostDAO postDAO = factory.getPostDAO();
  List<Post> posts = postDAO.getPostsByUser(user);
  return posts;
 }

 public Boolean deletePost(int postId) {
  AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
  PostDAO postDAO = factory.getPostDAO();
  return postDAO.deletePost(postId);
 }

}
