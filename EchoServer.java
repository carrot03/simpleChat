// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import java.util.StringTokenizer;

import common.ChatIF;
import ocsf.server.*;

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
public class EchoServer extends AbstractServer 
{
	
	//Instance variables **********************************************
	  
	  /**
	   * The interface type variable.  It allows the implementation of 
	   * the display method in the client.
	   */
	ChatIF ServerUI; 
	  
	  
  //Class variables *************************************************
	final private String key="loginKey";
	final private String loginTimes="loginTimes";
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF ServerUI) 
  {
    super(port);
    this.ServerUI = ServerUI;
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
	  String msgStr = (String)msg;
	  int count=0;
	  try {
	  if ((Integer)client.getInfo(loginTimes)!=0) {
		  client.close();
	  }}
	  catch(IOException e) {
		  ServerUI.display("the client has been already logged in. Close now is mandatory");
		  
	  }
	  if (msgStr.startsWith("#loginID") && (Integer)client.getInfo(loginTimes)==0) {
		  StringTokenizer st = new StringTokenizer (msgStr);
		  
		  st.nextToken();
		  String loginID = st.nextToken();
		  client.setInfo(key, loginID);
		  client.setInfo(loginTimes, count);
		  count=1;
	  }
	  
	  ServerUI.display("Message received: " + msg + " from " + client.getInfo(key));
	 
    this.sendToAllClients(client.getInfo(key)+" "+msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  ServerUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  ServerUI.display
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {    sv.listen(); //Start listening for connections
    
    	  
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }*/
  /**
   * Implementation of the Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  ServerUI.display("A client is connected");}

  /**
   * Implementation of the Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {System.out.println("A client is disconnected");}
  /**
   * Implementation of the Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  @Override
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
	 clientDisconnected(client);
	  
  }
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
 
  public void handleServerConsoleMessage(String msg) 
  {	  
	    	if(msg.startsWith("#")) {
	    		
	    		this.handleCommand(msg);
	    	}
	    	if(msg.startsWith("SERVER MSG")) {
	    		StringTokenizer st = new StringTokenizer (msg);
				  
				  st.nextToken();
				  st.nextToken();
				  String message = st.nextToken();
				  ServerUI.display(message);
				  this.sendToAllClients(message);
	    	}
	    	else {
	    		this.sendToAllClients(msg);
	    	}
	    
  }
  //method that filters the message and do what is asked for from the server
private void handleCommand(String cmd){
	  
	  if(cmd.equals("#quit")) {
		  ServerUI.display("The server will quit");
		  serverClosed();
		  serverStopped();
	  }
	  
	  else if(cmd.equals("#stop")) {
			  if (this.isListening()) {
				  ServerUI.display("The server will stop listening");
			stopListening();
			serverStopped();
		  }
			  else {
				  ServerUI.display("Cannot stop listening! The server is already closed");
			  }
	  }
	  
	  else if(cmd.startsWith("#close")) {
		  try {
		  if(this.isListening()){
			  this.close();
		  }else {ServerUI.display("The server cannot close");}
		  }
		  catch(IOException e) {
			  ServerUI.display("The server couldn't close");
		  }
	  }
	  
	  else if(cmd.startsWith("#setport")) {
		  
		  if(!this.isListening()){
			  StringTokenizer st = new StringTokenizer (cmd);
			  
			  st.nextToken();
			  String port = st.nextToken();
			  setPort(Integer.parseInt(port));}
		  else {ServerUI.display("The server must close to be able to change the port");}
	  }
	  
	  else if(cmd.equals("#start")) {
			  if (!this.isListening()) {
				  serverStarted();
				  ServerUI.display("The server will start listening");
			
		  }
			  else {
				  ServerUI.display("Cannot start! The server is already listening");
			  }
	  }
	  
	
	  else if(cmd.equals("#getport")) {
		  ServerUI.display("The number of the port will show up : "+ this.getPort());
		  
	  }
	  
	  
  }
  
}

//End of EchoServer class
