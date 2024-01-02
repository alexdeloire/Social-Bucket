package com.polytech.SocialBucket.Logic;

import java.util.List;

public class User {

    private String username;
    private String mail;
    private String password;
    private int id;
    private int nbFollowers;
    private int nbFollowing;

    public User(String username, String mail, String password, int id) {
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.id = id;
    }

    public User(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbFollowers() {
        return nbFollowers;
    }

    public void setNbFollowers(int nbFollowers) {
        this.nbFollowers = nbFollowers;
    }

    public int getNbFollowing() {
        return nbFollowing;
    }

    public void setNbFollowing(int nbFollowing) {
        this.nbFollowing = nbFollowing;
    }
}
