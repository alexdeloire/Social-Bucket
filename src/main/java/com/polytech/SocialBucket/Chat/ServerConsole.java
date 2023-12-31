package com.polytech.SocialBucket.Chat;
import java.io.*;
import com.polytech.SocialBucket.Chat.common.*;

/*
 * Server UI made by Alexandre Deloire and Jorge Rémi
 * All Rights Reserved
 */

/**
 * This class constructs the UI for a chat server.  It implements the
 * chat interface in order to activate the display() method.
 * 
 * @author Alexandre Deloire
 * @author Rémi Jorge
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to launch on.
   */
  public ServerConsole(int port) 
  {
    server= new EchoServer(port, this);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
   */
  public void accept() 
  {

    display("Welcome to the server console. Type #start to start the server.");

    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    int port = 0;  //The port number

    try
    {   
      port = Integer.parseInt(args[0]); // Get port from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }
    catch(NumberFormatException e)
    {
      System.err.println("Le port doit être un entier valide.");
      System.exit(1);
    }

    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}
//End of ServerConsoleChat class
