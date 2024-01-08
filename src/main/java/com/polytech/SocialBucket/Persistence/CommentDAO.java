package com.polytech.SocialBucket.Persistence;

import java.util.List;

import com.polytech.SocialBucket.Logic.Comment;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Reaction;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Persistence.PostgreSQLDAO.PostgreSQLCommentDAO;

public abstract class CommentDAO {

 protected static CommentDAO CommentDAO;

 public static CommentDAO getCommentDAO() {
  if (CommentDAO == null) {
   CommentDAO = new PostgreSQLCommentDAO();
  }
  return CommentDAO;
 }

 // getComments(post: Post): List
 public abstract List<Comment> getComments(Post post);

 // addComment(content: String, user: User, post: Post) : Comment
 public abstract Comment addComment(String content, User user, Post post); // throws IOException;

 // deleteComment(comment: Comment) : Bool
 public abstract void deleteComment(Comment comment);

 // AddReaction(type: String, user: User, comment: Comment): String
 public abstract void addReaction(String type, Comment comment, User user);

 public abstract void deleteReaction(Comment comment, User user);

 public abstract List<Reaction> getReactionsByComment(Comment comment);
 

}
