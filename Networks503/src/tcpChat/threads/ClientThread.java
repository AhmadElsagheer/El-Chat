package tcpChat.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeSet;

import tcpChat.hosts.Server;
import tcpChat.messages.ChatMessage;
import tcpChat.messages.MessageType;

//A Thread at the server side to serve a single client
public class ClientThread extends Thread 
{
	private int ctID;
	private Server server;
	private Socket clientSocket;
	private String username;
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;

	public ClientThread(Server server, Socket clientSocket, int ctID) 
	{
		this.server = server;
		this.clientSocket = clientSocket;
		this.ctID = ctID;
		try
		{
			this.sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			this.sInput = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch(IOException e)
		{
			System.out.println("Server: IO Exception with the streams while creating the client thread.");
		}
		
		setUsername();
	}

	public void setUsername()
	{
		try
		{
			username = (String)sInput.readObject();
			server.joinRequest(ctID, username);
		}
		catch(Exception e)
		{
			System.out.println("Server: Error while setting client nickname(1).");
		}
	}
	
	public void joinResponse(boolean accepted)
	{
		try
		{
			if(accepted)
			{
				sOutput.writeObject("Welcome " + username + " :)\n");
				sOutput.flush();
				server.addClient(username, this);
				start();
			}
			else
			{
				sOutput.writeObject("This nickname is already used! Enter a UNIQUE nickname: ");
				sOutput.flush();
				setUsername();
			}
		}
		catch(Exception e)
		{
			System.out.println("Server: error while setting client nickname(2).");
		}
		
			
	}
	
	public void run() 
	{
		updateList(server.getAllClients());
		boolean working = true;
		try 
		{
			while (working) 
			{

				ChatMessage message = (ChatMessage) sInput.readObject();
				if (!message.isAlive()) 
				{
					String response = "Message was not sent. Please try again.";
					sOutput.writeObject(new ChatMessage(server.toString(), message.getSender(), MessageType.SERVER_RESPONSE, response));
					sOutput.flush();
					continue;
				}
				message.decTTL();
				String response;
				switch (message.getType()) 
				{
					case MESSAGE: case SERVER_RESPONSE:
						server.route(message); break;
						
					case LOGOUT:
						response = "Logged off.";
						sOutput.writeObject(new ChatMessage(server.toString(), message.getSender(), MessageType.LOGOUT, response));
						sOutput.flush();
						server.logOff(message.getSender());
						working = false;
					default:
						
						
				}
			}

		} 
		catch (Exception e) 
		{
			System.out.println("Server: error while waiting/receiving a message from the client.");
			System.out.printf("Connection terminated with client: %s.\n", username);
		}

	}
	
	public void updateList(TreeSet<String> allClients)
	{
		try
		{
			allClients.remove(username);
			sOutput.writeObject(allClients);
			sOutput.flush();
			allClients.add(username);
		}
		catch(Exception e)
		{
			System.out.println("Server: can't update member list for client: " + username);
		}
	}
	
	public Socket getClientSocket() { return clientSocket; }

	public ObjectInputStream getsInput() { return sInput; }

	public ObjectOutputStream getsOutput() { return sOutput; }

}
