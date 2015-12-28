package tcpChat.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import tcpChat.controller.ClientController;

public class ChatListener implements ActionListener {

	private ClientController controller;

	public ChatListener(ClientController controller) { this.controller = controller; }

	public void actionPerformed(ActionEvent e) 
	{
		controller.getClientWindow().getContentPane().remove(controller.getClientWindow().getChat());
		controller.getClientWindow().addFeedback(controller.getChatText().get(e.getActionCommand()));
		controller.getClientWindow().getReciever().setText(e.getActionCommand());
		JButton x = (JButton) e.getSource();
		x.setBackground(null);
	}
}
