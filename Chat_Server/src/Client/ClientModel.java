package Client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;

/*
 * 
 * <h2> ClientModel class will handle all the server backEnd functions </h2>
 * 
 *
 */
public class ClientModel implements Runnable{

	public static final String INPUT = "Input";
	private final static int ServerPort = 7777; //the port of the server
	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;
	private Thread thread;
	private boolean running=false;
	private String _userName;
	private SwingPropertyChangeSupport action= new SwingPropertyChangeSupport(this);
	private StringTokenizer _tokenizerInput;
	private String _serverInput="";

	
	
	
	/*
	 * Method to set the connection to the server
	 * 
	 */
	public void connectToServer() {
		
		try {
			InetAddress ip = InetAddress.getByName("localhost");
			socket = new Socket(ip, ServerPort);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Run method for running the thread 
	 * 
	 * @param input the input from the server
	 * Every time a new input received from the server the set input method notify the controller 
	 * 
	 */
	public void run() {

		String input;
		
		while (running) {
			try {
				
				input = in.readUTF();
				notifyController(input);
		
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			in.close();
			out.close();
			socket.close();
			thread.join();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	
	/*
	 * Join the user to the chat room
	 * This method will start the client thread and start listening the the server
	 * 
	 * @param userName The name of the user
	 */
	public void joinChat(String userName)
	{
		if(running==true)
			return;
		running=true;
		thread = new Thread(this);
		thread.start();

		_userName = userName;
		sendMessage("join!@#$%" + _userName);
	}
	
	

	/*
	 * Method to notify the server the client is leaving the chat and to stop the client thread
	 */
	public void leaveChat() 
	{
		sendMessage("leave!@#$%" + _userName);
		
		if(!running)
			return;
		running = false;

	}

	


	
	/*
	 * Method to send the user output message to the server, If message is empty nothing will be sent
	 * 
	 * @param message The message from the user to send to the rest of the users
	 */
	protected void sendMessage(String message) {
		
		if(message.equals(""))
			return;
		try {
			out.writeUTF(message);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	/*
	 * Analyze the input from the server 
	 * 
	 * @return the input command -> new message or update users
	 *
	 */
	public String analyzeMessage() {
		
		_tokenizerInput = new StringTokenizer(_serverInput, "!@#$%"); 
		String input = _tokenizerInput.nextToken();
	
		//Update the users combo list 
		if (input.equals("users")) {
			
			return "updateUsers";		
		} 

		return "message";
	}

	

	
	/*
	 * Set the new input from the server and to notify the controller that a new message has received 
	 * 
	 * @param input The input string from the server 
	 */
	private void notifyController(String input) {
		String oldInput = _serverInput;
		String newInput = input;
		_serverInput = input;
		action.firePropertyChange(INPUT, oldInput, newInput);
	}
	
	
	
	
	/*
	 * Return a StringTokenizer object with the updated users list
	 * 
	 * @return A StringTokenizer with the updated users list
	 */
	public StringTokenizer getUsersList() {
		return _tokenizerInput;
	}
	
	
	/*
	 * Return the string input from the server
	 * 
	 * @return A String object of the input from the server
	 */
	public String getServerInput() {
		return _serverInput;
	}
	
	
	
	/*
	 * Methods to set and remove the listener to the server input
	 */
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 action.addPropertyChangeListener(listener);
	 }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
    	 action.removePropertyChangeListener(listener);
     }

	 public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
		 action.addPropertyChangeListener(name, listener);
	 }

	 public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
		 action.removePropertyChangeListener(name, listener);
	 }
	
			

}
