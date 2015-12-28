package tcpChat.messages;

import java.io.Serializable;


public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MessageType type;
	private String sender;
	private String receiver;
	private int TTL = 4;
	private String message;

	public ChatMessage(String s, String r, MessageType type, String message) 
	{
		this.type = type;
		this.sender = s;
		this.receiver = r;
		this.message = message;
	}

	public MessageType getType() {	return type; }

	public String getMessage() { return message; }
	
	public String getSender() { return sender; }
	
	public String getReceiver() { return receiver; }
	
	public void decTTL() { TTL--; }
	
	public boolean isAlive() { return TTL > 0; }
	
	


}
