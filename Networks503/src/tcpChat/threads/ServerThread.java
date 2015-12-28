package tcpChat.threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tcpChat.hosts.DNSServer;
import tcpChat.messages.ChatMessage;
import tcpChat.messages.MessageType;

//A Thread at the DNS server side to serve a single server as a client
public class ServerThread extends Thread 
{
	private DNSServer dns;
	private int serverID;
	private Socket serverSocket;
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;

	public ServerThread(DNSServer dns, Socket serverSocket) 
	{
		this.dns = dns;
		this.serverSocket = serverSocket;
		serverID = dns.joinResponse(this);
		try
		{
			this.sOutput = new ObjectOutputStream(serverSocket.getOutputStream());
			this.sInput = new ObjectInputStream(serverSocket.getInputStream());
		}
		catch(Exception e)
		{
			System.out.printf("DNS server: error while opening streams for the server thread for server %d.\n", serverID);
		}

	}

	public void run() 
	{

		try 
		{
			while (true) 
			{

				ChatMessage message = (ChatMessage) sInput.readObject();
				if (!message.isAlive()) 
				{
					String response = "Message was not sent. Please try again.";
					sOutput.writeObject(new ChatMessage("Server", message.getSender(), MessageType.SERVER_RESPONSE, response));
					sOutput.flush();
					continue;
				}
				message.decTTL();
				switch (message.getType()) 
				{
					case MESSAGE: case SERVER_RESPONSE:
						dns.route(message);	break;
						
					case LOGIN:
						boolean success = dns.memberLogin(message.getMessage(), serverID);
						if(success)
							sOutput.writeObject(new ChatMessage(dns.toString(), message.getSender(), MessageType.LOGIN, "Accepted"));
						else
							sOutput.writeObject(new ChatMessage(dns.toString(), message.getSender(), MessageType.LOGIN, "Rejected"));
						sOutput.flush();
						break;
						
					case LOGOUT:
						dns.logOff(message.getSender()); break;
						
					default: 	//WHO_IS_IN
						dns.updateMemberLists(); break;
				}
			}

		} 
		catch (Exception e) 
		{
			System.out.println("DNS server: error at the server thread while waiting/receiving a request from a server");
			System.out.printf("DNS server: connection with server %d is terminated.\n", serverID);
		}

	}

	public Socket getServerSocket() { return serverSocket; }

	public ObjectInputStream getsInput() { return sInput; }

	public ObjectOutputStream getsOutput() { return sOutput; }

}
