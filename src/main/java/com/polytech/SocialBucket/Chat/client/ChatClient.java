// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package com.polytech.SocialBucket.Chat.client;

import com.polytech.SocialBucket.Chat.ocsf.client.*;
import com.polytech.SocialBucket.Chat.common.*;
import java.io.*;
import java.util.Observer;
import java.util.Observable;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient implements Observer
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  /**
   * The login ID of the client.
   */
  private String loginID;

  /**
   * The Observable Client
   */
  private ObservableClient observableClient;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    this.observableClient = new ObservableClient(host, port); 
    observableClient.addObserver(this);
    this.clientUI = clientUI;
    this.loginID = "";
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {

      // Check if message is empty
      if (message.length() == 0){
        return;
      }

      message = message.trim().strip();
      String[] msgArray = message.split(" ");

      /****************
       *    GETHOST   *
       ****************/

      if (msgArray[0].equals("#gethost")){
        if (msgArray.length != 1){
          clientUI.display("gethost does not take any arguments.");
        } else {
          clientUI.display("The host is: " + observableClient.getHost());
        }
      }

      /****************
       *    GETPORT   *
       ****************/

      else if (msgArray[0].equals("#getport")){
        if (msgArray.length != 1){
          clientUI.display("getport does not take any arguments.");
        } else {
          clientUI.display("The port is: " + observableClient.getPort());
        }
      }
      
      /****************
       *    SETHOST   *
       ****************/

      else if (msgArray[0].equals("#sethost")){
        if (observableClient.isConnected()){
          clientUI.display("You must log off before setting the host.");
        } else if (msgArray.length != 2){
          clientUI.display("sethost takes exactly one argument: #sethost <host>");
        } else {
          String host = message.substring(9);
          observableClient.setHost(host);
          clientUI.display("The host is now: " + observableClient.getHost());
        }
      }

      /****************
       *    SETPORT   *
       ****************/

      else if (msgArray[0].equals("#setport")){
        if (observableClient.isConnected()){
          clientUI.display("You must log off before setting the port.");
        } else if (msgArray.length != 2){
          clientUI.display("setport takes exactly one argument: #setport <port>");
        } else {
          int port = Integer.parseInt(message.substring(9));
          observableClient.setPort(port);
          clientUI.display("The port is now: " + observableClient.getPort());
        }
      }

      /****************
       *    LOGIN     *
       ****************/

      else if (msgArray[0].equals("#login")){
        if (observableClient.isConnected()){
          clientUI.display("You are already logged in.");
        } else if (msgArray.length > 2){
          clientUI.display("login takes at most one argument: \n\t#login <login id>\n\t#login (to generate a random login id))");
        } else {
          if (msgArray.length == 2){
            loginID = msgArray[1];
          } else {
            loginID = "guest" + (int)(Math.random() * 8999 + 1000);
          }
          observableClient.openConnection();
          observableClient.sendToServer("#login " + loginID);
        }
      }

      /*************************
       *   LOGOFF OR LOGOUT    *
       *************************/

      else if (msgArray[0].equals("#logoff") || msgArray[0].equals("#logout")){
        if (!observableClient.isConnected()){
          clientUI.display("You are already logged off.");
        } else if (msgArray.length != 1){
          clientUI.display("logoff does not take any arguments.");
        } else {
          observableClient.sendToServer("#logoff");
          observableClient.closeConnection();
        }
      }

      /**************
       *    QUIT    *
       **************/

      else if (msgArray[0].equals("#quit")){
        if (msgArray.length != 1){
          clientUI.display("quit does not take any arguments.");
        } else {
          if (observableClient.isConnected()){
            observableClient.sendToServer("#logoff");
          }
          quit();
        }
      }

      /*************************
       *    INVALID COMMAND    *
       *************************/

      else if (message.startsWith("#")){
        clientUI.display("Invalid command.");
      }

      else{
        // Send message to server if connected
        if (observableClient.isConnected()){
          observableClient.sendToServer(message);
        }
        else{
          clientUI.display("You must log in before sending a message.");
        }
      }

    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server. Please check your configuration.");
    }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void connectionEstablished()
  {
    clientUI.display("Connection established.");
  }

  /**
   * This method is called when the connection to the server is closed.           
   * This method is overridden from the superclass.
   */
  public void connectionClosed()
  {
    clientUI.display("Connection closed.");
  }

  /**
   * This method handles the exception thrown when something goes wrong
   * with the connection.
   *
   * @param exception The exception thrown.
   */
  public void connectionException(Exception exception)
  {
    clientUI.display
      ("WARNING - The server has stopped listening for connections\n" +
      "SERVER SHUTTING DOWN! DISCONNECTING!\n" + "Abnormal termination of connection.");
    try{
      observableClient.closeConnection();
    } catch (IOException e){
      clientUI.display("Could not close connection.");
    }
  }


  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      observableClient.closeConnection();
    }
    catch(IOException e) {
      clientUI.display("Could not close connection.");
    }
    finally{
      System.exit(0);
    }
  }

  // Update Method
  public void update(Observable o, Object arg){
    if (arg instanceof Exception){
      connectionException((Exception)arg);
    } 

    else if ((String) arg == ObservableClient.CONNECTION_ESTABLISHED){
      connectionEstablished();
    } 
    
    else if ((String) arg == ObservableClient.CONNECTION_CLOSED){
      connectionClosed();
    }
    
    
    else if (arg instanceof String){
      handleMessageFromServer(arg);
    } 
  }



}
//End of ChatClient class
