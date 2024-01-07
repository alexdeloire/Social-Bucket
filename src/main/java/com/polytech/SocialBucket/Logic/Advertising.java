package com.polytech.SocialBucket.Logic;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Advertising {
 private int id;
 private User user;
 private LocalDate begindate;
 private LocalDate enddate;
 private String text;
 private String link;
 private byte[] image;

 public Advertising() {
 }

 public Advertising(User user, LocalDate begindate, LocalDate enddate, String text, String link, byte[] image) {
  this.user = user;
  this.begindate = begindate;
  this.enddate = enddate;
  this.text = text;
  this.link = link;
  this.image = image;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public User getUser() {
  return user;
 }

 public void setUser(User user) {
  this.user = user;
 }

 public LocalDate getBegindate() {
  return begindate;
 }

 public void setBegindate(LocalDate begindate) {
  this.begindate = begindate;
 }

 public LocalDate getEnddate() {
  return enddate;
 }

 public void setEnddate(LocalDate enddate) {
  this.enddate = enddate;
 }

 public String getText() {
  return text;
 }

 public void setText(String text) {
  this.text = text;
 }

 public String getLink() {
  return link;
 }

 public void setLink(String link) {
  this.link = link;
 }

 public byte[] getImage() {
  return image;
 }

 public void setImage(byte[] image) {
  this.image = image;
 }

}
