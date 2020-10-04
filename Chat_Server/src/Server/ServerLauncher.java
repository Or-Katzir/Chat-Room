package Server;

public class ServerLauncher {
	
	public static void main(String[]args) {
		
		ServerView view = new ServerView();
		ServerModel model = new ServerModel();
		ServerController server = new ServerController(view, model);

	}

}


/*
*
*--- working ---
*add to the private message the destination name as well - sender + (private) + destination + message
*
*-- working ----
*update users list every time a user is leaving or joining the chat
*
*---working----
*update the users list at the server list as well
*
*---working--- need to fix the close resources in client model class
*add method to delete a user from the server - with printing the deleting data to all users - name + deleted + server manager deleted 
*
*---working---
*add method to let the server manager to send messages to all users or a private  message if needed 
*
*
*create better documentation to the code
*
*make sure all the printing looks good
*
*change the client vector to a balanced tree -> vector is not efficient at all  
*
*---working----
*make sure that there is no useless code in the program  
*
*---working----
*change token in the string tokenizer to be something not # "hash tag" - maybe something like !@#$% 
*
*change variables name to match in all classes - such as: _output to command - in ClientController 
*
*--working--
*need to fix server and client crashing if a client is leaving the chat without sending leave message first
*
*add the paint option so clients will be able to paint together 
*
*add the save option so client and server will be able to save the chat data 
*
*change GUI to look better -> need work!! 
*
*/








