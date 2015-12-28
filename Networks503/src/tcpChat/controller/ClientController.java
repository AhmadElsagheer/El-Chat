package tcpChat.controller;

import java.awt.Color;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JTextArea;

import tcpChat.gui.ClientJoin;
import tcpChat.gui.ClientStartWindow;
import tcpChat.hosts.Client;
import tcpChat.listeners.ChatListener;
import tcpChat.listeners.ClientJoinListener;
import tcpChat.listeners.ClientStartWindowListener;
import tcpChat.messages.ChatMessage;

public class ClientController 
{

	private Client client;
	private ClientStartWindow clientWindow;
	private ClientJoin joinWindow;
	private ClientStartWindowListener clientWindowListener;
	private ClientJoinListener joinWindowListener;
	private ChatListener chatB;
	private TreeMap<String, JTextArea> chatText = new TreeMap<String, JTextArea>();
	private TreeMap<String, JButton> chatButtons = new TreeMap<String, JButton>();
	

	public ClientController()
	{
		client = new Client();
		joinWindow = new ClientJoin();
		joinWindowListener = new ClientJoinListener(this);
		clientWindowListener = new ClientStartWindowListener(this);
		chatB = new ChatListener(this);
		addActionListeners();
	}

	private void addActionListeners() 
	{
		joinWindow.getCloseButton().addActionListener(joinWindowListener);
		joinWindow.getServer().addActionListener(joinWindowListener);
		joinWindow.getJoin().addActionListener(joinWindowListener);
	}

	public void startClientWindow() 
	{
		clientWindow = new ClientStartWindow();
		clientWindow.setWelcome("Welcome " + client.getName());
		clientWindow.getCloseButton().addActionListener(clientWindowListener);
		clientWindow.getSendButton().addActionListener(clientWindowListener);
		clientWindow.getUpdateButton().addActionListener(clientWindowListener);
		clientWindow.getLogButton().addActionListener(clientWindowListener);
		clientWindow.getStartButton().addActionListener(clientWindowListener);
		clientWindow.getInput().addKeyListener(clientWindowListener);
		clientWindow.getCloseChatButton().addActionListener(clientWindowListener);
	}

	

	public void stop() 
	{
		clientWindow.setVisible(false);
		System.exit(0);
	}

	public void displayMessage(ChatMessage msg)
	{
		JTextArea k = getChatText().get(msg.getSender());
		if (getChatText().containsKey(msg.getSender())) 
		{
			getClientWindow().getContentPane().remove(getClientWindow().getChat());
			getClientWindow().addFeedback(getChatText().get(msg.getSender()));
		} 
		else 
		{
			getClientWindow().getContentPane().remove(getClientWindow().getChat());
			JTextArea feedback = new JTextArea("You are currently Chating with : " + msg.getSender() + "\n", 80, 72);
			getClientWindow().addFeedback(feedback);
			JButton z = new JButton(msg.getSender());
			getClientWindow().addChatButtons(getChatText().size(), z);
			getChatButtons().put(msg.getSender(), z);
			getChatText().put(msg.getSender(), feedback);
		}
		
		getChatButtons().get(msg.getSender()).addActionListener(getChatB());
		getClientWindow().getFeedback().append("" + msg.getSender() + " : " + msg.getMessage() + "\n");
		getClientWindow().getContentPane().remove(getClientWindow().getChat());
		
		if (k != null)
			getClientWindow().addFeedback(k);
		else
			getClientWindow().addFeedback(new JTextArea("Welcome to the Chat\n Please Start A Chat", 20, 72));
		
		if(getChatText().get(msg.getSender()) != getClientWindow().getFeedback()) 
		{
			getChatButtons().get(msg.getSender()).setBackground(Color.BLUE);
			getChatButtons().get(msg.getSender()).setContentAreaFilled(false);
			getChatButtons().get(msg.getSender()).setOpaque(true);
		}
	
	}
	
	public Client getClient() {	return client; }

	public ClientStartWindow getClientWindow() { return clientWindow; }
	
	public ChatListener getChatB() { return chatB; }

	public ClientJoin getJoinWindow() {	return joinWindow; }
	
	public TreeMap<String, JTextArea> getChatText() { return chatText; }
	
	public void setChatText(TreeMap<String, JTextArea> chatText) {	this.chatText = chatText; }

	public TreeMap<String, JButton> getChatButtons() { return chatButtons; }

	public void setChatButtons(TreeMap<String, JButton> chatButtons) { this.chatButtons = chatButtons; }

}