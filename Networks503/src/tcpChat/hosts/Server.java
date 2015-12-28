package tcpChat.hosts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.TreeSet;

import tcpChat.messages.ChatMessage;
import tcpChat.messages.MessageType;
import tcpChat.threads.ClientThread;
import tcpChat.threads.DNSReceivingThread;


public class Server 
{

	private String hostname;
	private ServerSocket server;
	private Socket toDNS;
	private int port;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private TreeMap<String, ClientThread> clients;
	private TreeMap<Integer, ClientThread> ctID;
	private TreeSet<String> allClients;
	private int nxtCT;

	public Server(String dnsName, int p) 
	{
		try 
		{
			server = new ServerSocket(port = p);
			clients = new TreeMap<String, ClientThread>();
			ctID = new TreeMap<Integer, ClientThread>();
			hostname = InetAddress.getLocalHost().getHostName();
			connectToDNS(dnsName);
			run();
		} 
		catch (Exception e) 
		{
			System.out.println("can't create the server!");
		}

	}

	public void connectToDNS(String dnsName)
	{

		try
		{
			toDNS = new Socket(dnsName, 6060);
			outStream = new ObjectOutputStream(toDNS.getOutputStream());
			inStream = new ObjectInputStream(toDNS.getInputStream());
			DNSReceivingThread dt = new DNSReceivingThread(toDNS, inStream, this);
			dt.start();	
		}
		catch(Exception e)
		{
			System.out.println("Server: can't connect to DNS server");
		}

	}

	public void run() 
	{
		System.out.println(this);
		while (true) 
		{
			Socket clientSocket;
			ClientThread ct = null;
			try 
			{
				clientSocket = server.accept();
				ct = new ClientThread(this, clientSocket, nxtCT);
				
				ctID.put(nxtCT++, ct);
			} 
			catch (IOException e) 
			{
				System.out.println("Server: can't accept client connection request.");
			}
		}
	}

	public synchronized void joinRequest(int ct, String username) 
	{
		try
		{
			outStream.writeObject(new ChatMessage(Integer.toString(ct), "DNS", MessageType.LOGIN, username));
			outStream.flush();
		}
		catch(Exception e)
		{
			System.out.println("Server: error while requesting from DNS to join a client.");
		}
	}
	
	public synchronized void joinResponse(String ct, boolean accepted)
	{
		ctID.get(Integer.parseInt(ct)).joinResponse(accepted);
		if(accepted)
			System.out.printf("Connected clients = %d\n", clients.size());
	}

	public void logOff(String name) throws IOException 
	{
		clients.get(name).getClientSocket().close();
		clients.remove(name);
		outStream.writeObject(new ChatMessage(name, "Server", MessageType.LOGOUT, null));
		System.out.printf("Connected clients = %d\n", clients.size());
	}

	public void route(ChatMessage message)
	{
		try
		{
			ClientThread receiverThread = getClient(message.getReceiver());
			if (receiverThread == null) 
				if (!allClients.contains(message.getReceiver())) 
				{
					ClientThread senderThread = getClient(message.getSender());
					senderThread.getsOutput().writeObject(new ChatMessage("Server", message.getSender(), MessageType.SERVER_RESPONSE, "User is not availabe."));
					senderThread.getsOutput().flush();
				} 
				else 
				{
					outStream.writeObject(message);
					outStream.flush();
				} 
			else 
			{
				receiverThread.getsOutput().writeObject(message);
				receiverThread.getsOutput().flush();
			}
		}
		catch(Exception e)
		{
			System.out.printf("DNS server: unable to route a message [sender: %s, receiver %s, content: %s]\n", 
					message.getSender(), message.getReceiver(), message.getMessage());
		}
		
	}

	public void memberListResponse(TreeSet<String> allClients) 
	{
		this.allClients = allClients;
		for(ClientThread ct : clients.values())
			ct.updateList(allClients);
	}

	public String getHostname() { return hostname; }
	
	public TreeSet<String> getAllClients() { return allClients; }
	
	public TreeMap<String, ClientThread> getClients() { return clients; }
	
	public synchronized void addClient(String name, ClientThread ct) { clients.put(name, ct); }

	public String toString() { return "Server: " + hostname + ", port number = " + port; }
	
	public ClientThread getClient(String name) { return clients.get(name); }
}