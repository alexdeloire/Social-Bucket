package com.polytech.SocialBucket.UI.Post;

import java.io.IOException;
import java.util.List;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.PostFacade;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;
import com.polytech.SocialBucket.UI.Advertising.OtherUserAdComponent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewsController {

 @FXML
 private VBox newsContainer;

 @FXML
 private ScrollPane mainContent;

 @FXML
 private Pane navbar;

 @FXML
 private Button openButton;

 @FXML
 private Button closeButton;

 @FXML
 private Pane loading;

 private UserFacade userFacade = UserFacade.getInstance();

 private PostFacade postFacade = PostFacade.getInstance();

 public void initialize() {
  // init name
  User user = UserFacade.getInstance().getCurrentUser();
  if (user == null) {
   return;
  }

  // Charger les posts lors de l'initialisation
  loadNews();

  openNavbar();
 }

 public void loadNews() {
  /*
   * 
   List<Object> news = postFacade.getNews(userFacade.getCurrentUser());
   newsContainer.getChildren().clear();
   if (news.size() == 0) {
     newsContainer.getChildren().add(new Label("No post to display"));
    } else {
      for (Object n : news) {
        if (n instanceof Post) {
          VBox postDetails = createPostDetails((Post) n);
          newsContainer.getChildren().add(postDetails);
        }
        if (n instanceof Advertising) {
          VBox advertisingDetails = createAdDetails((Advertising) n);
          newsContainer.getChildren().add(advertisingDetails);
          
        }
      }
    }
    */

    loading.setVisible(true);
    loading.setManaged(true);
    loading.setPrefHeight(100);

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("debut requete");
        try {
          List<Object> news = postFacade.getNews(userFacade.getCurrentUser());
          System.out.println("fin requete");
          Platform.runLater(new Runnable() {
          @Override
          public void run() {
            try {
            System.out.println("debut affichage");
            loading.setVisible(false);
            loading.setManaged(false);
            System.out.println("milieu affichage");
            displayNews(news);
            System.out.println("fin affichage");
            } catch (Exception e) {
            e.printStackTrace();
            }
          }
          });
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      });

      thread.start();
 }

  private void displayNews(List<Object> news) {
    newsContainer.getChildren().clear();
    // Ajouter les détails de chaque post dans le postsContainer
    if (news == null || news.isEmpty()) {
    newsContainer.getChildren().add(new Label("No news to display"));
    } else {
    System.out.println("debut boucle");
    for (Object n : news) {
      if (n instanceof Post) {
      VBox postDetails = createPostDetails((Post) n);
      newsContainer.getChildren().add(postDetails);
      }
      if (n instanceof Advertising) {
      VBox advertisingDetails = createAdDetails((Advertising) n);
      newsContainer.getChildren().add(advertisingDetails);
  
      }
    }
    System.out.println("fin boucle");
  
    }
  }

 private VBox createAdDetails(Advertising advertising) {
  try {
   FXMLLoader loader = new FXMLLoader(
     getClass().getResource("/com/polytech/SocialBucket/advertising/otherUserAdComponent.fxml"));

   VBox adContainer = loader.load();
   OtherUserAdComponent controller = loader.getController();
   controller.loadAdvertising(advertising, this::loadNews);
   return adContainer;
  } catch (IOException e) {
   e.printStackTrace();
  }
  VBox adContainer = new VBox();
  return adContainer;
 }

 private VBox createPostDetails(Post post) {
  try {
   FXMLLoader loader = new FXMLLoader(
     getClass().getResource("/com/polytech/SocialBucket/post/postComponent.fxml"));

   VBox postContainer = loader.load();
   PostComponent controller = loader.getController();
   controller.loadPost(post, this::loadNews);

   return postContainer;
  } catch (IOException e) {
   e.printStackTrace();
  }
  VBox postContainer = new VBox();
  return postContainer;
 }

 @FXML
 private void openNavbar() {
  try {
   // Charger la page du portefeuille depuis le fichier FXML
   FXMLLoader loader = new FXMLLoader(
     getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
   Pane navbarContent = loader.load();

   navbar.setPrefWidth(125);
   mainContent.setPrefWidth(720);

   navbar.getChildren().add(navbarContent);

   handleButtonNavbar(true);

  } catch (IOException e) {
   e.printStackTrace();
  }

 }

 @FXML
 private void closeNavbar() {
  navbar.setPrefWidth(0);
  mainContent.setPrefWidth(900);

  handleButtonNavbar(false);

  navbar.getChildren().clear();
 }

 private void handleButtonNavbar(boolean open) {
  openButton.setVisible(!open);
  openButton.setManaged(!open);
  closeButton.setVisible(open);
  closeButton.setManaged(open);
  if (open) {
   closeButton.toFront();
  }
 }

}
