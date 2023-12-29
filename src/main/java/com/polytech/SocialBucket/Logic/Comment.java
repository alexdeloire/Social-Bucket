package com.polytech.SocialBucket.Logic;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private int id;
    private String content;
    private User user;
    private Comment father;
    private List<Comment> replies;
    private List<Reaction> reactions= new ArrayList<>();
    private Post post;

    // Constructeurs
    
    public Comment(int id,String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.id = id;
    }



    public Comment(int id, String content, User user, Comment father, List<Comment> replies, List<Reaction> reactions,
            Post post) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.father = father;
        this.replies = replies;
        this.reactions = reactions;
        this.post = post;
    }
    


    // Getter and Setter

    // id


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // content

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    // user

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    // father

    public Comment getFather() {
        return father;
    }
    public void setFather(Comment father) {
        this.father = father;
    }

    // replies

    public List<Comment> getReplies() {
        return replies;
    }
    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }


    // reactions

    public List<Reaction> getReactions() {

        return reactions;
    }
    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    

    // post

    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
   

}
