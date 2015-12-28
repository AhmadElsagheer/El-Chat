package tcpChat.threads;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.TreeSet;

import tcpChat.hosts.Server;
import tcpChat.messages.ChatMessage;
import tcpChat.messages.MessageType;

//A Thread at the server side to receive messages from the DNS server
public class DNSReceivingThread extends Thread
{
	Socket serverSocket;
	Server	server;
	ObjectInputStream inStream;

	public DNSReceivingThread(Socket serverSocket, ObjectInputStream inStream, Server server) 
	{	
		this.serverSocket = serverSocket;
		this.server = server;
		this.inStream = inStream;	
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try 
		{
			while (true) 
			{
				Object x = inStream.readObject();
				if (x instanceof ChatMessage)
				{
					ChatMessage msg = (ChatMessage) x;
					if(msg.getType() == MessageType.LOGIN)
						server.joinResponse(msg.getReceiver(), msg.getMessage().equals("Accepted"));
					else
						server.route(msg);
				}
				else 	//an update for member list
					server.memberListResponse((TreeSet<String>) x);
				
			}

		} 
		catch (Exception e) 
		{

			System.out.println("Server: error while receiving something from the DNS server.");
			System.out.println("Server: connection with the DNS server is terminated.");
		}

	}


}
