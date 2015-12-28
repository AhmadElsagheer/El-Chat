package tcpChat.threads;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.TreeSet;

import tcpChat.controller.ClientController;
import tcpChat.messages.ChatMessage;

//A Thread at the client side to receive messages from the server
public class ReceivingThread extends Thread
{
	Socket clientSocket;
	ObjectInputStream inStream;
	ClientController controller;

	public ReceivingThread(Socket clientSocket, ObjectInputStream inStream, ClientController controller) 
	{
		this.clientSocket = clientSocket;
		this.inStream = inStream;
		this.controller = controller;
	}

	@SuppressWarnings("unchecked")
	public void run() 
	{
		try
		{
			while (true) 
			{
				Object x = inStream.readObject();
				if(x instanceof ChatMessage) 
				{
					ChatMessage msg = (ChatMessage) x;
					switch(msg.getType())
					{
					case MESSAGE:
						controller.displayMessage(msg); break;

					case LOGOUT:
						clientSocket.close(); 
						controller.stop();
						return;

					default: //server response
						if (msg.getSender().equals("Server"))
							controller.getClientWindow().getFeedback().append("" + msg.getSender() + " : " + msg.getMessage() + "\n");
						else
							controller.getChatText().get(msg.getSender()).append("Server : " + msg.getSender() + " has "+ msg.getMessage() + "\n");
					}	
				} 
				else	//an update for the member list 
				{
					TreeSet<String> list = (TreeSet<String>) x;
					if (list.size() == 0)
						list.add("Only You Are Available");
										
					controller.getClientWindow().getContentPane().remove(controller.getClientWindow().getmList());
					String[] arr = new String[list.size()];
					int nxt = 0;
					for(String name : list)
						arr[nxt++] = name;
					controller.getClientWindow().setmList(arr);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Client: error while receiving something from the server.");
			System.out.println("Client: connection with the server is terminated.");
		}
	}
}
