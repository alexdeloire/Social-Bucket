// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package com.polytech.SocialBucket.Chat;

import java.io.*;

import com.polytech.SocialBucket.Chat.common.ChatIF;
import com.polytech.SocialBucket.Chat.ocsf.server.*;
import java.util.Observer;
import java.util.Observable;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer implements Observer
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI;

  /**
   * The Observable Server.
   */
  ObservableOriginatorServer observableServer;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param serverUI The interface type variable.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    this.observableServer = new ObservableOriginatorServer(port);
    observableServer.addObserver(this);
    this.serverUI = serverUI;
    try {
        observableServer.listen();
      } catch (Exception e) {
        serverUI.display("ERROR - Could not listen for clients!");
      }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    // Check if message is empty
    if (msg.toString().length() == 0) {
      serverUI.display("Message received is empty, ignoring. id:hu5ty");
      return;
    }

    // Check if the very first message starts with a #login
    if (client.getInfo("loginID") == null) {
      String[] message = msg.toString().split(" ");
      if (!message[0].equals("#login")) {
        serverUI.display("ERROR - User not logged in!");
        try{
          client.sendToClient("SERVER MSG> ERROR - First message must be a #login command");
          client.close();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not send message or close connection!");
        }
        return;
      }
    }

    // Check if the message is a command
    // Commands start with a #
    if (msg.toString().charAt(0) == '#') {
      String[] command = msg.toString().split(" ");

      /**********************
       *       LOGIN        *
       **********************/

      if (command[0].equals("#login")) {
        if (client.getInfo("loginID") != null) { // Check if user is already logged in
          serverUI.display("ERROR - User already logged in!");
          try{
              client.sendToClient("SERVER MSG> You are already logged in!");
          } catch (IOException e) {
            serverUI.display("ERROR - Could not send message!");
          }
          return;
        }
        if (command.length != 2) { // Check if command is valid, should be #login <username>
          serverUI.display("ERROR - No username provided!");
          try{
            client.sendToClient("SERVER MSG> No username provided!");
            client.close();
          } catch (IOException e) {
            serverUI.display("ERROR - Could not send message or close connection!");
          }
          return;
        }
        client.setInfo("loginID", command[1]); // Set the loginID to the username
        serverUI.display("Message received: " + msg + " from " + client);
        observableServer.sendToAllClients("SERVER MSG> " + command[1] + " has logged on.");
      }

      /******************************
       *      LOGOFF OR LOGOUT      *
       * ****************************/

      else if (command[0].equals("#logoff") || command[0].equals("#logout")) {
        if (command.length != 1) { // Check if command is valid, should be #logoff
          try {
            client.sendToClient("SERVER MSG> Invalid command, should be only #logout or #logoff");
          } catch (IOException e) {
            serverUI.display("ERROR - Could not send message!");
          }
          serverUI.display("ERROR - Invalid command!");
          return;
        }
        if (client.getInfo("loginID") == null) { // Check if user is logged in, should be logged in to log off (big error if not)
          try{
            client.sendToClient("SERVER MSG> You are not logged in!");
          } catch (IOException e) {
            serverUI.display("ERROR - Could not send message!");
          }
          serverUI.display("ERROR - You are not logged in!");
          return;
        }
        serverUI.display("Message received: " + msg + " from " + client);
        observableServer.sendToAllClients("SERVER MSG> " + client.getInfo("loginID") + " has logged off.");
        client.setInfo("loginID", null);
        try {
          client.close();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not close connection!");
        }
        return;
      }

      /**********************
       *        QUIT        *
       * ********************/

      else if (command[0].equals("#quit")) {
        try {
          serverUI.display("Message received: " + msg + " from " + client);
          observableServer.sendToAllClients("SERVER MSG> " + client.getInfo("loginID") + " has disconnected.");
          client.setInfo("loginID", null);
          client.close();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not close connection!");
        }
        return;
      }

      /**********************
       *   INVALID COMMAND  *
       * ********************/  

      else {
        try {
          client.sendToClient("SERVER MSG> Invalid command!");
        } catch (IOException e) {
          serverUI.display("ERROR - Could not send message!");
        }
        serverUI.display("Command from client not recognized!");
        return;
      }
    }

    else {
      // Send message to all clients
      serverUI.display("Message received: " + msg + " from " + client);
      observableServer.sendToAllClients(msg);
    }
  }


  /**
   * This method is called when a client connects to the server. 
   * It overrides the method in the superclass.          
   *
   * @param client the connected client.    
   */
  public void clientConnected(ConnectionToClient client) {
    serverUI.display("A client has connected.");
  }

  /**
   * This method is called when a client disconnects from the server. 
   * It overrides the method in the superclass.          
   *
   * @param client the disconnected client.    
   */
  public void clientDisconnected(ConnectionToClient client) {
    serverUI.display("A client has disconnected.");
  }

  /**
   * This method is called each time an exception is thrown in a
   * ConnectionToClient thread.
   * It overrides the method in the superclass.
   *
   * @param client the client that raised the exception.
   * @param exception the exception thrown.
   */
  public void clientException(ConnectionToClient client, Throwable exception) {
    serverUI.display("Uh oh, a client has experienced an exception!");
    observableServer.sendToAllClients("SERVER MSG> " + client.getInfo("loginID") + " has disconnected due to an exception!");
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + observableServer.getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }


  /**
   * Called when the server admin types something into the console.
   * Sends the message to all clients.
   * @param message
   */
  public void handleMessageFromServerUI(String message) {

    // Check if message is empty
    if (message.length() == 0) {
      serverUI.display("Message is empty!");
      return;
    }

    if (message.charAt(0) == '#') {
      message = message.trim().strip();
      String[] msgArray = message.split(" ");

      // Check if message is empty
      if (msgArray.length == 0){ 
        serverUI.display("Message is empty!");
      }

      /**************
       *   GETPORT  *
       **************/

      else if(msgArray[0].equals("#getport")){
        int port = observableServer.getPort();
        serverUI.display("The port is: " + port);
      }

      /**************
       *   SETPORT  *
       **************/

      else if(msgArray[0].equals("#setport")){
        if (observableServer.isListening()){
          serverUI.display("You must close the server before setting the port.");
        } else if (msgArray.length != 2){
          serverUI.display("setport takes exactly one argument: #setport <port>");
        } else {
          int port = Integer.parseInt(message.substring(9));
          observableServer.setPort(port);
          serverUI.display("The port is now: " + observableServer.getPort());
        }
      }

      /**************
       *   START    *
       **************/

      else if (msgArray[0].equals("#start")){
        if (observableServer.isListening()){
          serverUI.display("The server is already running.");
        } else {
          try {
            observableServer.listen();
          } catch (Exception e) {
            serverUI.display("ERROR - Could not listen for clients!");
          }
        }
      }

      /**************
       *   STOP     *
       **************/

      else if (msgArray[0].equals("#stop")){
        if (!observableServer.isListening()){
          serverUI.display("The server is not running.");
        } else {
          observableServer.stopListening();
          observableServer.sendToAllClients("WARNING - The server has stopped listening for connections.");
        }
      }

      /**************
       *   CLOSE    *
       **************/

      else if (msgArray[0].equals("#close")){
        if (!observableServer.isListening()){
          serverUI.display("The server is not running.");
        } else {
          try {
            observableServer.close();
            observableServer.stopListening();
          } catch (IOException e) {
            serverUI.display("ERROR - Could not close server!");
          }
        }
      }

      /**************
       *   QUIT  *
       **************/

      else if (msgArray[0].equals("#quit")){
        try {
          observableServer.close();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not close server!");
        }
        finally {
          System.exit(0);
        }
      }

      /*********************
       *  INVALID COMMAND  *
       *********************/

      else{
        serverUI.display("ERROR - Command not recognized!");
      }

    } else {

      // Send message to all clients as the server

      observableServer.sendToAllClients("SERVER MSG> " + message);
      serverUI.display("SERVER MSG> " + message);
    }
  }

  /**
   * Update method for the observer pattern
   */
  public void update(Observable o, Object arg){
    OriginatorMessage oMsg = (OriginatorMessage) arg;
    ConnectionToClient client = (ConnectionToClient) oMsg.getOriginator();
    String message = (String) oMsg.getMessage();
    if (message.equals(ObservableOriginatorServer.CLIENT_CONNECTED)){
      clientConnected(client);
    } else if (message.equals(ObservableOriginatorServer.CLIENT_DISCONNECTED)){
      clientDisconnected(client);
    } else if (message.startsWith(ObservableOriginatorServer.CLIENT_EXCEPTION)){
      clientException(client, new Exception("Client has disconnected."));
    } else if (message.equals(ObservableOriginatorServer.SERVER_CLOSED)){
      serverStopped();
    } else if (message.equals(ObservableOriginatorServer.SERVER_STARTED)){
      serverStarted();
    } else if (message.equals(ObservableOriginatorServer.SERVER_STOPPED)){
      serverStopped();
    } else {
      handleMessageFromClient(message, client);
    }
    }
  
  //Class methods ***************************************************

}
//End of EchoServer class
