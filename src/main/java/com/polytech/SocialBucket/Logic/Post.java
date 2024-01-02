package com.polytech.SocialBucket.Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Post {

 private String text;
 private String type;
 private String fileName;
 private User user;
 private int id;
 private byte[] bytes;

 private List<Reaction> reactions;

 // Constructeur
 public Post(String text, String type, User user, int id) {
  this.text = text;
  this.type = type;
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

 public byte[] getBytes() {
  return bytes;
 }

 public void setBytes(byte[] bytes) {
  this.bytes = bytes;
 }
}
