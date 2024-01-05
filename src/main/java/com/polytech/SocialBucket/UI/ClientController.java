package com.polytech.SocialBucket.UI;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;

import com.polytech.SocialBucket.Chat.client.ChatClient;
import com.polytech.SocialBucket.Chat.common.ChatIF;
import com.polytech.SocialBucket.Chat.EchoServer;
import com.polytech.SocialBucket.Logic.User;
import com.polytech.SocialBucket.Logic.Facade.UserFacade;

import java.io.IOException;

public class ClientController implements ChatIF{

    @FXML
    private TextArea messageArea;

    @FXML
    private Pane mainContent;

    @FXML
    private Pane navbar;

    @FXML
    private Button openButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField inputField;

    private ChatClient client;

    // Just for development
    private EchoServer server;

    private class DisplayImpl implements ChatIF {
        public void display(String message) {
            System.out.println("> " + message);
        }

    }

    @FXML
    private void initialize() {

        openNavbar();

        // Just for development
        DisplayImpl display = new DisplayImpl();

        // Just for development
        this.server = new EchoServer(5555, display);

        // Try to connect to server
        try {
            client = new ChatClient("localhost", 5555, this);
        } catch (IOException exception) {
            System.out.println("Error: Can't set up connection! Terminating client.");
            System.exit(1);
        }

        // Login
        UserFacade userFacade = UserFacade.getInstance();
        User user = userFacade.getCurrentUser();

        client.handleMessageFromClientUI("#login " + user.getUsername());

    }

    @FXML
    private void sendMessage() {
        String message = inputField.getText().trim();
        UserFacade userFacade = UserFacade.getInstance();
        User user = userFacade.getCurrentUser();
        if (!message.isEmpty()) {
            message = user.getUsername() + ": " + message;

            client.handleMessageFromClientUI(message);
            inputField.clear();
        }
    }

    public void display(String message) {
        messageArea.appendText(message + "\n");
    }

    @FXML
    private void openNavbar() {
        try {
            // Charger la page du portefeuille depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/polytech/SocialBucket/sidebarPage.fxml"));
            Pane navbarContent = loader.load();

            navbar.setPrefWidth(140);
            mainContent.setPrefWidth(705);

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

