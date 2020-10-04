package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import Client.ClientController.ClientListener;

/*
 * <h2>ClintView Class creating the GUI for the client</h2>
 * 
 * @author Or Katzir
 * 
 * @version 2.0
 * 
 * 
 */
public class ClientView extends JFrame {

	protected JButton _join;
	protected JButton _leave;
	protected JButton _paint;
	protected JButton _save;
	protected JButton _send;
	private DefaultComboBoxModel<String> _usersList;
	private JComboBox<String> _usersListComboBox;// combo box to hold all the chat users and to select a user for a private message
	private JTextArea _chatInput; // the text area for the input from the chat users
	private JTextArea _textArea; // the test area for the message to be send from this user
	private JScrollPane _infoScroll;
	private JScrollPane _textScroll;
	private JPanel _buttonPanel; // panel to hold all the upper buttons in the the client GUI
	private JPanel _mainTextPanel; //panel to hold the main text area 
	private JPanel _sendPanel; // panel for the user send message area
	private Border border = BorderFactory.createLineBorder(Color.BLACK);

	
	/*
	 * Client view constructor 
	 * Construct a new client GUI object
	 */
	public ClientView() {

		super("Client Chat");

		setButtonsPanel();
		
		setMainTextPanel();
	
		setSendPanel();

		add(_buttonPanel, BorderLayout.NORTH);
		add(_mainTextPanel, BorderLayout.CENTER);
		add(_sendPanel, BorderLayout.SOUTH);

		setSize(800, 600);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	
	
	/*
	 * Private method to set the main panel of the GUI
	 * 
	 * Panel will contain the main text input area
	 * 
	 */
	private void setMainTextPanel() {
		
		_mainTextPanel = new JPanel();
		_mainTextPanel.setLayout(new BorderLayout());
		
		_chatInput = new JTextArea(10, 69);
		_chatInput.setEditable(false);
		_infoScroll = new JScrollPane(_chatInput);
		_infoScroll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		_mainTextPanel.add(_infoScroll, BorderLayout.CENTER);
	}
	
	
	/*
	 * Private method to set the upper button panel 
	 * 
	 * Panel will contain the functional buttons for the user 
	 */
	private void setButtonsPanel() {
		
		_buttonPanel = new JPanel();
		_buttonPanel.setLayout(new GridLayout(1, 1, 20, 20));
		
		_buttonPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		_join = new JButton("Join");
		_leave = new JButton("Leave");
		_paint = new JButton("Paint");
		_save = new JButton("Save");
		
		_usersList = new DefaultComboBoxModel<>();
		_usersList.addElement("CHAT USERS:");
		_usersListComboBox = new JComboBox<String>(_usersList);
		
		_buttonPanel.add(_join);
		_buttonPanel.add(_leave);
		_buttonPanel.add(_paint);
		_buttonPanel.add(_save);
		_buttonPanel.add(_usersListComboBox);
		
	}
	
	
	/*
	 * Private method to set the lower panel of the GUI
	 * 
	 * Panel will contain the message input text area and the send button
	 * 
	 */
	private void setSendPanel() {
		
		_sendPanel = new JPanel();
		_sendPanel.setLayout(new BorderLayout());

		_textArea = new JTextArea(3, 10);
		_textScroll = new JScrollPane(_textArea);
		_textArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		_send = new JButton("Send Message");
		
		_sendPanel.add(_textScroll, BorderLayout.NORTH);
		_sendPanel.add(_send, BorderLayout.SOUTH);
	}
	
	
	
	/*
	 * Method to set text to the input text area
	 * 
	 * @param input The input to set to the text, will be set to the text area in a new line
	 * 
	 */
	protected void setChatInput(String input) {
		_chatInput.append(input + "\n");
	}
	

	
	/*
	 * Method to set action listener to the buttons in the GUI
	 * 
	 * @param listener The action listener to ass to the buttons
	 * 
	 */
	protected void addActionListener(ClientListener listener) {
		
		_join.addActionListener(listener);
		_leave.addActionListener(listener);
		_paint.addActionListener(listener);
		_save.addActionListener(listener);
		_send.addActionListener(listener);
		_usersListComboBox.addActionListener(listener);
	}	
	
	/*
	 * Method to update the active users list in the combo box
	 * Method is called only is a user has joined or left the chat room
	 * 
	 * @param users The new users data list to update
	 */
	public void updateUsers(StringTokenizer users) {
		int numberOfUsers = users.countTokens();
		_usersList.removeAllElements();
		_usersList.addElement("CHAT USERS:");
		for (int i = 0; i < numberOfUsers; i++) {
		//	String chatUserName = users.nextToken();
			_usersList.addElement(users.nextToken());
		}
		_usersListComboBox.setSelectedItem(_usersList);
	}
	
	/*
	 * Method to get the user name for the chat room 
	 * 
	 * @return the user name entered by the user
	 * User name can not be empty 
	 */
	public String getUserName() {
		return JOptionPane.showInputDialog("Please enter your name");

	}
	
	
	/*
	 * Method to get the output message from the user to send to the chat users 
	 * If selected item from the combo box is equal to "CHAT USERS:" - message will be sent to all users 
	 * If selected item from the combo box is equal to a user name - message will be sent only to that user
	 * 
	 * @return the new message from the user to sent to the server
	 * 
	 */
	protected String getMessageToSend() {
		
		String selectedUser = (String) _usersListComboBox.getSelectedItem();
		String message = _textArea.getText();
		if(message.equals(""))
			return "";
		
		//check if the message is to all users or a private message
		if(selectedUser.equals("CHAT USERS:")) 
		{
			message = "all#" + _textArea.getText();
		}else 
		{
			message = selectedUser + "#"+ _textArea.getText();
		}
		
		_textArea.setText(""); 

		return message;
	}
	

	
}


















