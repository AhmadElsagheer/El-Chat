package tcpChat.hosts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.TreeSet;

import tcpChat.messages.ChatMessage;
import tcpChat.threads.ServerThread;

public class DNSServer 
{
	private String hostname;
	private ServerSocket DNS;
	private int port = 6060;
	private TreeMap<Integer, ServerThread> servers;
	private TreeMap<String, Integer> clients = new TreeMap<String, Integer>();
	private int cap = 0;

	public DNSServer() 
	{
		try 
		{
			DNS = new ServerSocket(port);
			servers = new TreeMap<Integer, ServerThread>();
			hostname = InetAddress.getLocalHost().getHostName();
			run();
		} 
		catch (Exception e) 
		{
			System.out.println("Error while creating the DNS server.");
		}
	}

	public void run() 
	{
		System.out.printf("DNS server is running\nHostname: %s\nport number = %d\n", hostname, port);
		while (true) 
		{
			Socket LowerServerSocket;
			ServerThread st = null;
			try
			{
				LowerServerSocket = DNS.accept();
				st = new ServerThread(this, LowerServerSocket);
				st.start();
			} 
			catch (IOException e) 
			{
				System.out.println("DNS server: Error while accepting a connection request from a server.");
			}
		}
	}

	public synchronized int joinResponse(ServerThread st) 
	{
		servers.put(++cap, st);
		return cap;
	}

	public synchronized void logOff(String name)
	{
		clients.remove(name);
		updateMemberLists();

	}

	public void route(ChatMessage message)
	{
		ServerThread st = servers.get(clients.get(message.getReceiver()));

		try
		{
			st.getsOutput().writeObject(message);
			st.getsOutput().flush();
		}
		catch(Exception e)
		{
			System.out.printf("DNS server: unable to route a message [sender: %s, receiver %s, content: %s]\n", 
					message.getSender(), message.getReceiver(), message.getMessage());
		}

	}

	public synchronized void updateMemberLists()
	{
		TreeSet<String> latest = new TreeSet<String>();
		for (String user : clients.keySet())
			latest.add(user);


		for(int i = 1; i <= 4; ++i)
			if(servers.containsKey(i))
			{
				ServerThread st = servers.get(i);
				try
				{
					st.getsOutput().writeObject(latest);
					st.getsOutput().flush();
				}
				catch(Exception e)
				{
					System.out.printf("DNS server: can't send member list to server %d.\n", i);
				}
			}
	}

	public synchronized boolean memberLogin(String name, int serverID)
	{
		if(isUsedName(name))
			return false;
		clients.put(name, serverID);
		updateMemberLists();
		return true;
	}

	public boolean isUsedName(String name) { return clients.containsKey(name); }

	public TreeMap<String, Integer> getClients() { return clients; }

	public String toString() { return "DNS Server: " + hostname + ", port number = " + port; }
}
