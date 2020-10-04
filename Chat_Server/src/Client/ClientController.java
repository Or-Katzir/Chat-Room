package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ClientController {

	
	private ClientModel _clientModel;
	private ClientView _clientView;
	private String _input, _output;
	
	/*
	 * Construct a new ClientController object
	 * 
	 */
	public ClientController(ClientModel clientModel, ClientView clientView) {
		this._clientModel = clientModel;
		this._clientView = clientView;
		
		this._clientView.addActionListener(new ClientListener());
		
		clientModel.addPropertyChangeListener(ClientModel.INPUT, new ServerListener());
		
	}
	
	
	/*
	 * Private class to set the action listener to the server input
	 */
	private class ServerListener implements PropertyChangeListener {
	      
	        public void propertyChange(PropertyChangeEvent evtent) {
	        	
	            _input = _clientModel.analyzeMessage();
	            
	            if(_input.equals("updateUsers")) {
	            	_clientView.updateUsers(_clientModel.getUsersList());
	            }
	            else {
	            	_input = _clientModel.getServerInput();
	            	_clientView.setChatInput(_input);
	            }
	        }
	    }   
	
	
	/*
	 * Private class to set the action listener to the ClientView buttons
	 */
	public class ClientListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			
			if (event.getSource() == _clientView._join) 
			{
				joinNewClient();
			} 
			if (event.getSource() == _clientView._send) 
			{
				_output = _clientView.getMessageToSend();
				_clientModel.sendMessage(_output);
			}
			if (event.getSource() == _clientView._leave) 
			{
				_clientModel.leaveChat();	
			}
		}	
		
		/*
		 * Private method to add the new client to the chat room
		 * 
		 */
		private void joinNewClient() {
			String clientName = _clientView.getUserName();
			if(clientName == null)
				return;
			_clientModel.connectToServer();
			_clientModel.joinChat(clientName);
		}
		
	}
	
	

	
}
