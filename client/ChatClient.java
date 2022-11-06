// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import ocsf.server.ConnectionToClient;
import common.*;
import java.io.*;
import java.util.StringTokenizer;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  //variables
  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
	
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    this.loginID=loginID;
    connectionEstablished();
  }

  
  //Instance methods ************************************************
  /**
	 * Implementation of the hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
  @Override
	protected void connectionEstablished() {
	  try{
		  this.sendToServer("#loginID "+loginID);}
	  catch(IOException e) {
		  clientUI.display("problems with creating the client's loginID");
		  
	  }
  }
	
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
    	if(message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToServer(message);
    	}
   
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String cmd) {
	  
	  if(cmd.equals("#quit")) {
		  clientUI.display("The client will quit");
		  quit();
	  }
	  
	  else if(cmd.equals("#logoff")) {
		  try {
			  if (this.isConnected()) {
				  clientUI.display("The client will log off");
			this.closeConnection();
		  }
			  else {
				  clientUI.display("Cannot log off! The client is already logged off");
			  }
		} catch (IOException e) {
			clientUI.display("There is a problem with logging the client off");
		}
	  }
	  
	  else if(cmd.startsWith("#sethost")) {
		  
		  if(!this.isConnected()){
			  StringTokenizer st = new StringTokenizer (cmd);
			  
			  st.nextToken();
			  String host = st.nextToken();
			  setHost(host);}
		  else {clientUI.display("The client must log off to be able to change the host");}
	  }
	  
	  else if(cmd.startsWith("#setport")) {
		  
		  if(!this.isConnected()){
			  StringTokenizer st = new StringTokenizer (cmd);
			  
			  st.nextToken();
			  String port = st.nextToken();
			  setPort(Integer.parseInt(port));}
		  else {clientUI.display("The client must log off to be able to change the port");}
	  }
	  
	  else if(cmd.equals("#login")) {
		  try {
			  if (!this.isConnected()) {
				  clientUI.display("The client will log in");
			this.openConnection();
		  }
			  else {
				  clientUI.display("Cannot log in! The client is already logged in");
			  }
		} catch (IOException e) {
			clientUI.display("There is a problem with logging the client off");
		}
	  }
	  
	  else if(cmd.equals("#gethost")) {
		  clientUI.display("The number of the host will show up");
		  this.getHost();
	  }
	  else if(cmd.equals("#getport")) {
		  clientUI.display("The number of the port will show up");
		  this.getPort();
	  }
	  
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Implementation of the Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
	 clientUI.display("The connection has been terminated");
	}
  
  synchronized protected void clientException(
		    ConnectionToClient client, Throwable exception) {
	  
	  clientUI.display("The connection has been terminated");
  }

	/**
	 * Implementation of the Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down");
	  System.exit(0);
	}
  
}
//End of ChatClient class
