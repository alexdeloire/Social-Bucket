package com.polytech.SocialBucket.Chat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import com.polytech.SocialBucket.Chat.common.ChatIF;
import com.polytech.SocialBucket.Chat.client.ChatClient;

public class ClientGUI extends JFrame implements ChatIF, ActionListener {
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton loginButton;
    private JButton logoutButton;
    private ChatClient client;

    // Host and port
    private static String host = "localhost";
    private static int port = 5555;

    public ClientGUI(String host, int port) {
        super("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(20);
        sendButton = new JButton("Send");
        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        inputPanel.add(loginButton);
        inputPanel.add(logoutButton);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this);
        loginButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add a KeyListener to the inputField
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        try {
            client = new ChatClient(host, port, this);
        } catch (IOException exception) {
            System.out.println("Error: Can't set up connection!"
                    + " Terminating client.");
            System.exit(1);
        }

        setVisible(true);
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            client.handleMessageFromClientUI(message);
            inputField.setText("");
        }
    }

    @Override
    public void display(String message) {
        messageArea.append(message + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            sendMessage();
        }

        else if (e.getSource() == loginButton) {
            String pseudo = inputField.getText();
            String message = "#login";
            if (!pseudo.isEmpty()) {
                message += " " + pseudo;
                inputField.setText("");
            }
            client.handleMessageFromClientUI(message);
        }

        else if (e.getSource() == logoutButton) {
            client.handleMessageFromClientUI("#logout");
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            host = args[0];
        }

        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Port must be a valid integer.");
                System.exit(1);
            }
        }

        SwingUtilities.invokeLater(() -> new ClientGUI(host, port));
    }
}
