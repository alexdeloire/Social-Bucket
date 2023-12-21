package com.polytech.SocialBucket.Logic;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Post {

 private String text;
 private File file;
 private String type;
 private String fileName;
 private User user;
 private int id;

 private List<Reaction> reactions;

 // Constructeur
 public Post(String text, String type, File file, String fileName, User user, int id) {
  this.text = text;
  this.file = file;
  this.type = type;
  this.fileName = fileName;
  this.user = user;
  this.id = id;
  this.reactions = new ArrayList<Reaction>();
 }

 public void addReaction(Reaction reaction) {
  reactions.add(reaction);
 }

 public void deleteReaction(Reaction reaction) {
  reactions.remove(reaction);
 }

 public List<Reaction> getReactions() {
  return reactions;
 }

 public void setReactions(List<Reaction> reactions) {
  this.reactions = reactions;
 }

 // Getters et Setters

 public String getText() {
  return text;
 }

 public void setText(String text) {
  this.text = text;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }

 public File getFile() {
  return file;
 }

 public void setFile(File file) {
  this.file = file;
 }

 public User getUser() {
  return user;
 }

 public void setUser(User user) {
  this.user = user;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public String getFileName() {
  return fileName;
 }

 public void setFileName(String fileName) {
  this.fileName = fileName;
 }
}
