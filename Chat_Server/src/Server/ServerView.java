package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import Server.ServerController.ServerButtonsListener;


public class ServerView extends JFrame{

	protected JButton _startServer;
	protected JButton _closeServer;
	protected JButton _deleteUser;
	protected JButton _foo;
	protected JButton _sendButton;
	
	private String[] usersString = {"CHAT USERS:"};
	private JComboBox<String> usersComboBox;
	
	private JTextArea _chatMessages; // text area for the input and output messages sent by the users
	private JTextArea _chatData;
	private JTextArea _sendTextArea;
	
	
	private JPanel buttonPanel;
	private JPanel _mainPanel;
	private JPanel _textPanel;
	
	private Border border = BorderFactory.createLineBorder(Color.BLACK);
	
	
	
	public ServerView() {

		super("Chat Server");
		
		setMainPanel();
		
		add(_mainPanel);

		setSize(800, 600);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	private void setMainPanel() {
		
		_mainPanel = new JPanel();
		
		setButtonsPanel();
		setTextPanel();
		
		_mainPanel.setLayout(new BorderLayout());
		_mainPanel.add(buttonPanel, BorderLayout.NORTH);
		_mainPanel.add(_textPanel, BorderLayout.CENTER); 
	}
	
	
	
	/*
	 * Private method to set the upper buttons panel for the server
	 */
	private void setButtonsPanel() {
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 1, 20, 20));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		_startServer = new JButton("Start Server");
		_closeServer = new JButton("Close Server");
		_deleteUser = new JButton("Delete User");
		_foo = new JButton("Foo");
		
		usersComboBox = new JComboBox<String>(usersString);
		
		buttonPanel.add(_startServer);
		buttonPanel.add(_closeServer);
		buttonPanel.add(_foo);
		buttonPanel.add(_deleteUser);
		buttonPanel.add(usersComboBox);
		
	}
	
	
	
	private void setTextPanel(){
		
		_textPanel = new JPanel();
		_textPanel.setLayout(new BorderLayout());
		

		//---- Panel for the information about new users, deleted users and users that left the chat room-----
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());

		JLabel dataLabel = new JLabel("This area will show all chat data:");
		dataLabel.setSize(4, 4);
		
		_chatData = new JTextArea();
		_chatData.setEditable(false);
		JScrollPane dataScroll = new JScrollPane(_chatData);
		dataScroll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10,10,10,10)));
		
		dataPanel.add(dataLabel, BorderLayout.NORTH);
		dataPanel.add(dataScroll, BorderLayout.CENTER);
		//-------------------------------------------------------------------------------------------
		
		
		//---- Panel for the message sent by the chat users -----------------------------------------
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());

		JLabel infoLabel = new JLabel("This area will show all chat messages:");
		infoLabel.setSize(4, 4);
		
		_chatMessages = new JTextArea();
		_chatMessages.setEditable(false);
		JScrollPane infoScroll = new JScrollPane(_chatMessages);
		infoScroll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));		// _chatInfo.setFont(new java.awt.Font("Courier New", 0, 15));
		
		infoPanel.add(infoLabel, BorderLayout.NORTH);
		infoPanel.add(infoScroll, BorderLayout.CENTER);
		//--------------------------------------------------------------------------------------------
		
		
		//-------- Panel for sending message ---------------------------------------------------------
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new BorderLayout());

		_sendTextArea = new JTextArea(3, 10);
		JScrollPane textScroll = new JScrollPane(_sendTextArea);
		_sendTextArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		_sendButton = new JButton("Send Message");
		
		JLabel sendLabel = new JLabel("Send message");
		sendLabel.setSize(4, 4);

		sendPanel.add(sendLabel, BorderLayout.NORTH);
		sendPanel.add(textScroll, BorderLayout.CENTER);
		sendPanel.add(_sendButton, BorderLayout.SOUTH);
		//-----------------------------------------------------------------------------------------------
		
		
		//----- Panel to hold the data and info panels --------------------------------------------------
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridLayout(2,1));
			
		tempPanel.add(dataPanel);
		tempPanel.add(infoPanel);
		//------------------------------------------------------------------------------------------------

		_textPanel.add(tempPanel, BorderLayout.CENTER);
		_textPanel.add(sendPanel, BorderLayout.SOUTH);

	}
	

	
		
	/*
	 * Method to add the action listener to the server buttons 
	 * 
	 * @param listener ServerListener class inner class in the serverController for handling the action from server manager
	 */
	public void addActionListener(ServerButtonsListener listener) {
		_startServer.addActionListener(listener);
		_closeServer.addActionListener(listener);
		_deleteUser.addActionListener(listener);
		_foo.addActionListener(listener);
		_sendButton.addActionListener(listener);
	}
	
	
	

	public void setUsers(String[] usersString) {
		this.usersString = usersString;
		usersComboBox.setModel(new DefaultComboBoxModel<String>(this.usersString));
	}
	
	
	public String getUserComboBox() {
		return (String)usersComboBox.getSelectedItem();
	}
	
	public String getMessageDestination() {
		return (String) usersComboBox.getSelectedItem();
	}
	
	public String getMessageToSend() {
		String message = _sendTextArea.getText();
		_sendTextArea.setText(" ");
		return message;
	}
	
	/*
	 * Set new text to the server text area
	 */
	public void setDataText(String input) {
		_chatData.append(input + "\n");
	}
	
	public void setMessagesText(String input) {
		_chatMessages.append(input + "\n");
	}
	
}
