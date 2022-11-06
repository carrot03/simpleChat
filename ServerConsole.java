/*author : Hiba Tantawi
 * Student number : 300250911
 */



import java.util.Scanner;

import client.ChatClient;
import common.ChatIF;


public class ServerConsole  implements ChatIF {
	
	//Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the server that created this ConsoleChat.
	   */
	  EchoServer server;
	  ChatClient client;
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole.
	   * @param port The port to connect on.
	   */
	  public ServerConsole( int port) 
	  {  
	      server= new EchoServer(port, this);//create a server
	      try 
	      {    server.listen(); //Start listening for connections
	      
	      	  
	      } 
	      catch (Exception ex) 
	      {
	        System.out.println("ERROR - Could not listen for clients!");
	      }
	      server.serverStarted();
	    
	    // Create scanner object to read from console
	      
	    fromConsole = new Scanner(System.in);
	  }
	  
	  
	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the server's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        this.display(message);
	       
			server.handleServerConsoleMessage(message);
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
	   * This method is responsible for the creation of the server.
	   *
	   * @param args[0] The port to connect to.
	   */
	  public static void main(String[] args) 
	  {
		  
		    int port = 0;


		    try
		    {
		    
		      port = Integer.parseInt(args[0]);
		    }
		    catch(ArrayIndexOutOfBoundsException e)
		    {
		      
		      port = DEFAULT_PORT;
		    }
		    catch(NumberFormatException ne) {
		      port = DEFAULT_PORT;
		    }
		    ServerConsole chat= new ServerConsole(port);
		    chat.accept();  //Wait for console data
	  }
	  

}
//End of the console chat
