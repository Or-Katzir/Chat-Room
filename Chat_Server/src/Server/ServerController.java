package Server;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;



public class ServerController {
	
	public ServerView _serverView;
	public ServerModel _serverModel;
	private String command;
	
	
	public ServerController(ServerView view, ServerModel model) {
		_serverView = view;
		_serverModel = model;
		
		_serverView.addActionListener(new ServerButtonsListener());
		
		_serverModel.addPropertyChangeListener(ServerModel.COMMAND, new ServerListener());
	}
	
	
	/*
	 * Private class to set the action listener to the server input
	 */
	private class ServerListener implements PropertyChangeListener {
	      
	        public void propertyChange(PropertyChangeEvent evtent) {
	        	
	            command = _serverModel.getCommand();

	            if(command.equals("join")) {
	            	_serverView.setDataText(_serverModel.getMessageToSend());
	            	_serverView.setUsers(_serverModel.getUsersString());
	            	return;
	            }
	            if(command.equals("leave")) {
	            	_serverView.setDataText(_serverModel.getMessageToSend());
	            	_serverView.setUsers(_serverModel.getUsersString());
	            	return;
	            }
	            
	            if(command.equals("all")) {
	            	_serverView.setMessagesText(_serverModel.getMessageToSend());
	            	return;
	            }
	            else {
	            	_serverView.setMessagesText(_serverModel.getMessageToSend());
	            }
	            
	        }
	    }   
	
	
	
	
	/*
	 * CLass to handle all the button action from the server manager 
	 */
	public class ServerButtonsListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event) {
			
			if(event.getSource() == _serverView._startServer) {
				_serverModel.startServer();
				_serverView.setDataText("Server is running\n");
				return;
			}
			if(event.getSource() == _serverView._deleteUser) {
				_serverView.setDataText(_serverModel.deleteUser(_serverView.getUserComboBox()));
				_serverView.setUsers(_serverModel.getUsersString());
			}
			if(event.getSource() == _serverView._sendButton) {
				_serverModel.sendServerMessage(_serverView.getMessageDestination(), _serverView.getMessageToSend());
			}
			if(event.getSource() == _serverView._closeServer) {
				_serverModel.closeServer();
			}
		}
		
	}


	
	
}
