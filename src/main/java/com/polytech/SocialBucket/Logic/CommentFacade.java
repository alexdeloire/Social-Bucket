package com.polytech.SocialBucket.Logic;

import java.util.List;

import com.polytech.SocialBucket.Persistence.AbstractDAOFactory;
import com.polytech.SocialBucket.Persistence.CommentDAO;
import com.polytech.SocialBucket.Persistence.PostDAO;

public class CommentFacade {

    private static CommentFacade commentFacade;
    private Comment currentComment;

    

    public static CommentFacade getInstance() {
        if (commentFacade == null) {
         commentFacade = new CommentFacade();
        }
        return commentFacade;
    }


    public boolean addComment(String content, Post post) {
        boolean test = false;
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
    
        UserFacade userFacade = UserFacade.getInstance();
        User user = userFacade.getCurrentUser();
    
        CommentDAO commentDAO = factory.getCommentDAO();
        Comment comment = commentDAO.addComment(content, user, post);
    
        if (comment instanceof Comment) {
            test = true;
        }
    
        this.currentComment = comment;
        return true;
    }

    public List<Comment> getComments(Post post){
        
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        CommentDAO commentDAO = factory.getCommentDAO();

        List<Comment> comments = commentDAO.getComments(post);
        return comments;
    }
    

    // Getter and setter

    public Comment getCurrentComment() {
        return currentComment;
    }

    public void deleteComment(Comment comment){
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        CommentDAO commentDAO = factory.getCommentDAO();
        
        commentDAO.deleteComment(comment);
    }

    public void addReaction(String type, Comment comment,  User user){
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        CommentDAO commentDAO = factory.getCommentDAO();

        commentDAO.addReaction(type, comment, user);
    }
    public void deleteReaction( Comment comment,  User user){
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        CommentDAO commentDAO = factory.getCommentDAO();

        commentDAO.deleteReaction( comment, user);
    }

    public List<Reaction> getReactionsByComment(Comment comment){
        AbstractDAOFactory factory = AbstractDAOFactory.getFactory();
        CommentDAO commentDAO = factory.getCommentDAO();

        return commentDAO.getReactionsByComment(comment);
    }

}

