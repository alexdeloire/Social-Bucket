package com.polytech.SocialBucket.UI;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.polytech.SocialBucket.Chat.client.ChatClient;
import com.polytech.SocialBucket.Chat.common.ChatIF;
import com.polytech.SocialBucket.Chat.ServerConsole;

import java.io.IOException;

public class ClientController implements ChatIF{

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField inputField;

    // Replace this with your actual client logic
    private ChatClient client;

    //private ServerConsole server;

    @FXML
    private void initialize() {
        // Initialization code, if needed
        //this.server = new ServerConsole(5555);


        try {
            client = new ChatClient("localhost", 5555, this);
        } catch (IOException exception) {
            System.out.println("Error: Can't set up connection! Terminating client.");
            System.exit(1);
        }
    }

    @FXML
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Append the sent message to the message area
            display("You: " + message);

            // Replace this with your actual logic to send the message
            client.handleMessageFromClientUI(message);
            inputField.clear();
        }
    }

    // This method is called by ChatClient to display received messages
    public void display(String message) {
        messageArea.appendText(message + "\n");
    }

    // You can have additional methods or fields for your client logic
}

