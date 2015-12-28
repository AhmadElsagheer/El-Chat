package tcpChat.hosts;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import tcpChat.controller.ClientController;
import tcpChat.messages.ChatMessage;
import tcpChat.messages.MessageType;
import tcpChat.threads.ReceivingThread;

public class Client {

	private Socket socket;
	private String name;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private ReceivingThread rt;

	
	public void join(String hostname, int port, ClientController gui)
	{

		try
		{
			socket = new Socket(hostname, port);
			outStream = new ObjectOutputStream(socket.getOutputStream());
			inStream = new ObjectInputStream(socket.getInputStream());
			
			while (true) 
			{
				String nickname = (String) JOptionPane.showInputDialog(null, "Please Enter A UNIQUE Nickname", "Joining Server...", 
						JOptionPane.PLAIN_MESSAGE, null, null, null);
				
				if (nickname != null && !nickname.isEmpty()) 
				{
					name = nickname;
					outStream.writeObject(name);
					outStream.flush();
					String response = (String) inStream.readObject();
					JOptionPane.showMessageDialog(null, response);
					if (response.charAt(0) == 'W')
						break;
				}
			}
			gui.getJoinWindow().setVisible(false);
			gui.startClientWindow();
			rt = new ReceivingThread(socket, inStream, gui);
			rt.start();
		}
		catch(Exception e)
		{
			System.out.println("Client: error while connecting to the server.");
		}
	}

	public void chat(String destination, String message) 
	{
		try 
		{
			outStream.writeObject(new ChatMessage(name, destination, MessageType.MESSAGE, message));
			outStream.flush();
		} 
		catch (Exception e) 
		{
			System.out.println("Client: can't send chat message.");
		}
	}

	
	
	public synchronized void quit(TreeMap<String, JTextArea> x) {
		try
		{
			for (String user : x.keySet()) 
			{
				outStream.writeObject(new ChatMessage(name, user, MessageType.SERVER_RESPONSE, "Logged Off."));
				outStream.flush();
			}
			outStream.writeObject(new ChatMessage(name, "Server", MessageType.LOGOUT, null));
			outStream.flush();
			
		}
		catch(Exception e)
		{
			System.out.println("Client: error in logging off.");
		}
	}

	public String getName() { return name; }
	
}
