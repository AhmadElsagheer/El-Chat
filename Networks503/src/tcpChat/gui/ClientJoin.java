package tcpChat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientJoin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton closeButton = new JButton(new ImageIcon("resources/images/closeButton.png"));
	private JButton Join = new JButton("Join");
	private JButton Server = new JButton("Join Server");
	private int port = 6000;
	private String serverName;

	public ClientJoin()
	{
		super("Client Name");
		try
		{
			serverName = InetAddress.getLocalHost().getHostName();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		setContentPane(new JLabel(new ImageIcon("resources/images/clientSWBack.jpg")));
		setBounds(400, 100, 600, 400);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setLayout(null);
		addComponents();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	private void addComponents()
	{
		Color color = new Color(240, 255, 255);
		
		add(closeButton);
		closeButton.setBounds(550, 10, 30, 30);
		closeButton.setOpaque(true);
		closeButton.setBorder(null);
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setOpaque(false);
		
		add(Join);
		Join.setBounds(150, 200, 300, 100);
		Join.setBackground(color);
		
		add(Server);
		Server.setBounds(150, 50, 300, 100);
		Server.setBackground(color);
	}

	public JButton getCloseButton() { return closeButton; }

	public JButton getJoin() { return Join; }

	public JButton getServer() { return Server; }

	public int getPort() { return port; }

	public void setPort(int port) { this.port = port; }

	public String getServerName() { return serverName; }

	public void setServerName(String serverName) { this.serverName = serverName; }
}
