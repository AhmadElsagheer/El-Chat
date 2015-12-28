package tcpChat.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import tcpChat.controller.ClientController;

public class ClientJoinListener implements ActionListener
{

	private ClientController controller;

	public ClientJoinListener(ClientController controller) { this.controller = controller; }

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Join Server"))
		{
			String s = (String)JOptionPane.showInputDialog(null, "Please Enter Server Name You Want To Join",
					"Server Name", JOptionPane.PLAIN_MESSAGE, null,  null,controller.getJoinWindow().getServerName());
			
			String p = (String)JOptionPane.showInputDialog(null, "Please Enter Server Port You Want To Join",
                    "Server Port", JOptionPane.PLAIN_MESSAGE, null, null, "6000");
			
			if ( s == null || s.isEmpty() || p == null || p.isEmpty())
				return;
			
			int port = Integer.parseInt(p);
			controller.getJoinWindow().setServerName(s);
			controller.getJoinWindow().setPort(port);
		}
		else
			if(e.getActionCommand().equals("Join"))
			{
				try
				{
					controller.getClient().join(controller.getJoinWindow().getServerName(),controller.getJoinWindow().getPort(), controller);
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
				
			}
			else 
				if(e.getSource().equals(controller.getJoinWindow().getCloseButton()))
					System.exit(0);
	}

}
