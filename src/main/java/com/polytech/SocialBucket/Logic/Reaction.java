package com.polytech.SocialBucket.Logic;

public class Reaction {

 String type;
 int iduser;

 public Reaction(String typeReaction, int iduser) {
  this.type = typeReaction;
  this.iduser = iduser;
 }

 // getters and setters
 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }

 public int getIduser() {
  return iduser;
 }

 public void setIduser(int iduser) {
  this.iduser = iduser;
 }

}
