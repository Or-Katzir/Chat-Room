package Server;

import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.SwingPropertyChangeSupport;

/*
 * ServerModel class will handle all the server methods 
 */
public class ServerModel implements Runnable {

	public static final String COMMAND = "Command";
	protected static Vector<Client> clientList;
	private SwingPropertyChangeSupport action= new SwingPropertyChangeSupport(this);
	private String input = "", command, message;
	private String _users;
	private boolean running = true;
	private DefaultComboBoxModel<String> users = new DefaultComboBoxModel<String>();
	private String[] usersString;
	private String messageToSend;
	private Thread serverThread;


	

	/*
	 * Constructor 
	 */
	public ServerModel() {

		clientList = new Vector<>();

	}

	/*
	 * Method to start the sever thread -> activate the server 
	 */
	public void startServer() {
		running = true;
		serverThread = new Thread(this);
		serverThread.start();
	}


	
	/*
	 * Run method to start the server thread 
	 * 
	 * Method will listen for any new user that want to join the chat
	 * Adding new users to the user list and starting the user thread
	 * 
	 */
	public void run() {

		try {
			ServerSocket serverSocket = new ServerSocket(7777);
			Socket socket = null;
			while (running) {

				socket = serverSocket.accept();
				DataInputStream dataInput = new DataInputStream(socket.getInputStream());
				DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
				Client client = new Client(socket, dataInput, dataOutput);
				client.start();
				
			}

			serverSocket.close();
			socket.close();
			serverThread.join();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

	
	/*
	 * Method to send messages from the server manager to the users 
	 * 
	 * @param clientName -> the name of the message destination 
	 * @param massage -> the message to send 
	 * 
	 */
	public void sendServerMessage(String clientName ,String message) {
		command = "all";
		if(clientName.equals("CHAT USERS:")) 
		{
			messageToSend = "Server Manager -> " + message;
			sendMessage(messageToSend);
			notifyController(message);
		}
		else {
			messageToSend ="Private message from Server Manager: to "+clientName+ ": " + message ;
			sendPrivateMessage(clientName, messageToSend, "null");
			notifyController(message);
		}
		System.out.println(messageToSend);
	}


	/*
	 * Method to delete a user by the server manager 
	 * 1-Remove the client from the client list
	 * 2-send message to all users and to the sever that the client has been deleted by the server manager
	 * 3-close deleted client resources 0
	 * 4-update chat users
	 * 
	 * 
	 * ---need to fix --- close resources in the client model class as well -- need to notify the client model that it has been kicked out by the server manager
	 */
	public String deleteUser(String userName) {
		
		messageToSend  = " -> has been deleted by the server mananger";

		for(Client client : clientList) {
			if(client.getClientName().equals(userName)) {
				sendPrivateMessage(userName, userName+messageToSend, "server");
				clientList.remove(client);
				client.closeResources();
				break;
			}
		}
	    updateChatUsers(userName, messageToSend);
		return userName + messageToSend;
			
	}
	
	
	
	/*
	 * private method to get the input from a user and to notify the controller a new input has been received 
	 */
	private void notifyController(String input) {
		String oldInput = this.input;
		String newInput = input;
		this.input = input;
		action.firePropertyChange(COMMAND, oldInput, newInput);
	}
	

	
	
	/*
	 * Method to analyze the input from the user
	 * 
	 * @param input the input from the user 
	 * @param client the client sending the input 
	 */
	public void breakMessage(String input, Client client) {

		StringTokenizer st = new StringTokenizer(input, "!@#$%");
		command = st.nextToken();
		message = st.nextToken();

		//empty message from the user - need to ignore
		if (command.equals("") || message.equals("")) {
			return;
		}
		if (command.equals("join")) {
			joinUser(client, message);
			messageToSend = message + " -> Join the chat room";
			notifyController(input);
			return;
		}
		if (command.equals("leave")) {
			messageToSend = client.getClientName() + " -> Left the chat room";
			deleteUser(client);
			notifyController(input);
			return;
		}
		if (command.equals("all")) {
			messageToSend = client.getClientName() + " : " + message ;
			sendMessage(messageToSend);
			notifyController(input);
			return;
		} 
		else //message to send is private: @param command will be the name of the recipient 
		{ 
			messageToSend ="(Private message) " + client.getClientName() + " --> "+command+ ": " + message ;
			sendPrivateMessage(command, messageToSend, client.getClientName());
			notifyController(input);
			return;
		}
		
	}

	
	
	private void joinUser(Client client, String name) {
		client.setClientName(name);
		clientList.add(client);
		updateChatUsers(client.getClientName(), " -> Join the chat room");
	}

	
	/*
	 * Method to delete a client that has left the chat room
	 * This method will remove the client from the client list 
	 * Update all the chat users list
	 * Close the server client thread resources
	 * 
	 * @param client the client that left the chat room
	 * 
	 */
	public void deleteUser(Client client) {
		
		String name = client.getClientName();
		clientList.removeElement(client);
		updateChatUsers(name, " -> Left the chat room");
		client.closeResources();

	}
	
	public void closeServer() {
		
		
		running = false;
	
		updateChatUsers("Server Manager", " -> Closed the chat room");
		
		for(Client client : clientList) {
			client.closeResources();
			clientList.remove(client);
		}

	}
	

	
	/*
	 * Private message to update all users if a user has joined or left the chat room
	 * This method will create a new string object with all the active users in the chat room 
	 * And it will send the message to all update the users list for all users
	 * Ass well as sending a message to notify all users of the new activity 
	 * @param name the name of the user that joined or left the chat room
	 * @param command a string to tell the users if a user has joined or left the chat room
	 */
	private void updateChatUsers(String name, String output) {
		
		usersString = new String[clientList.size()+1];
		usersString[0] = "CHAT USERS:";
		int i=1;

		_users = "users"; //the string of all the users in the chat to be sent to all users 
		for (Client client : ServerModel.clientList) {
			_users += "!@#$%" + client.getClientName();
			usersString[i] = client.getClientName();
			i++;
		}
		for (Client client : ServerModel.clientList) {
			try {
				client._output.writeUTF(_users);
				client._output.writeUTF(name + output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	


	/*
	 * Method to send a message to all the users in the chat room
	 * 
	 * @param message the message to send from a user
	 * @param name the name of the user that sent the message
	 */
	private void sendMessage(String message) {
	
		for (Client client : ServerModel.clientList) {
			try {
				client._output.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 
	 * Private method to send a private message from one user to another 
	 * If the count variable is equal to 2 then both users received the message and method can return
	 * This method also used to send users a message when they are deleted by the server manager, in this case sender name will be server and 
	 * 
	 * @param receiver the user that receive the private message
	 * @param message the message to send to the user
	 * @param sender the user that sends the private message
	 */
	private void sendPrivateMessage(String receiver, String message, String sender) {
		int count = 0;// if count equals to 2 then we have sent all the private messages and method can return
		if(sender.equals("null"))
			count++;
		for (Client client : clientList) {
			if(count == 2) return;
			if (client._clientName.equals(receiver) || client._clientName.equals(sender)) {
				try {
					count++;
					client._output.writeUTF(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	
	

	
	
	
	
	
	//----------- Getters and setters ----------------
	
	
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
	
	 public String getMessageToSend() {
		 return messageToSend;
	 }
	
	public String getMessage() {
		return message;
	}

	
	public String getCommand() {
		return command;
	}
	
	//return a string of the users list - used for the client thread
	public String getUsersList() {
		return _users;
	}
	
	//return a DefaultComboBoxModel object - used for the server view
	public DefaultComboBoxModel<String> getUsers() {
		return users;
	}
	
	public String[] getUsersString() {
		return usersString;
	}
	
	//---------- Getters and setters -----------------
	
	
	
	
	
	
	
	
	
	
	
	
	//--------------------- PRIVATE CLASS FOR THE SERVER CLIENT -------------------------------
	
	/*
	 * 
	 * Private class to construct a client thread object for the server
	 * 
	 */
	private class Client extends Thread {

		private String _clientName; 
		private final DataInputStream _input;
		private final DataOutputStream _output;
		private Socket _socket;
		private boolean running; 

		
		public Client(Socket socket, DataInputStream input, DataOutputStream output) {
			this._input = input;
			this._output = output;
			this._socket = socket;
			running = true;
		}

		
		
		public void run() {
			
			String inputFromUser;
			while (running) {
				try {
					
					inputFromUser = _input.readUTF();
					breakMessage(inputFromUser, this);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	
		
		/*
		 * Method to close this client thread object resources 
		 */
		private void closeResources() {
			try {
				running = false;
				this._input.close();
				this._output.close();
				this._socket.close();
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		
		private String getClientName() {
			return _clientName;
		}	
		
		
		private void setClientName(String name) {
			_clientName = name;
		}
		
		
		//END OF CLIENT CLASS
	}

	//----------------- END OF CLIENT CLASS ------------------------------------
	
	
	
	
}

	







