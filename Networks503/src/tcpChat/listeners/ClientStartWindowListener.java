package tcpChat.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JTextArea;

import tcpChat.controller.ClientController;

public class ClientStartWindowListener implements ActionListener, KeyListener {

	private ClientController controller;

	public ClientStartWindowListener(ClientController controller) {	this.controller = controller; }

	public void actionPerformed(ActionEvent e) 
	{
		ActionEvent x = e;
		if (x.getSource().equals(controller.getClientWindow().getCloseButton()))
			controller.getClient().quit(controller.getChatText());
		else
			if(x.getActionCommand().equals("Send")) 
				doMessage();
			else
				if(x.getActionCommand().equals("Log Off"))
					controller.getClient().quit(controller.getChatText());
				else
					if(x.getActionCommand().equals("Start Chat")) 
					{
						if(controller.getClientWindow().getList().isSelectionEmpty())
							return;
						String selectedItem = (String) controller.getClientWindow().getList().getSelectedValue();

						if (selectedItem.equals("Only You Are Available") || selectedItem.equals("Click Update to View Online Members"))
							return;
						controller.getClientWindow().getReciever().setText(selectedItem);

						if(controller.getChatText().containsKey(selectedItem)) 
						{
							controller.getClientWindow().getContentPane().remove(controller.getClientWindow().getChat());
							controller.getClientWindow().addFeedback(controller.getChatText().get(selectedItem));
							controller.getChatButtons().get(selectedItem).setBackground(null);
						} 
						else 
						{
							if (controller.getChatText().size() == 7)
								return;

							controller.getClientWindow().getContentPane().remove(controller.getClientWindow().getChat());
							JTextArea feedback = new JTextArea("You are currently Chating with : " + selectedItem + "\n", 80, 72);
							controller.getClientWindow().addFeedback(feedback);

							JButton z = new JButton(selectedItem);
							z.addActionListener(controller.getChatB());
							controller.getClientWindow().addChatButtons(controller.getChatText().size(), z);
							controller.getChatButtons().put(selectedItem, z);
							controller.getChatText().put(selectedItem, feedback);
						}
					}
					else
						if (x.getActionCommand().equals("close chat")) 
						{
							if (controller.getClientWindow().getReciever().equals("Only You Are Available")
									|| controller.getClientWindow().getReciever().equals("Click Update to View Online Members"))
								return;

							if (controller.getChatButtons().get(controller.getClientWindow().getReciever().getText()) == null)
								return;

							controller.getClientWindow().getUsers().remove(controller.getChatButtons()
									.get(controller.getClientWindow().getReciever().getText()));
							controller.getChatButtons().remove(controller.getClientWindow().getReciever().getText());
							controller.getChatText().remove(controller.getClientWindow().getReciever().getText());


							for (Entry<String, JButton> rd : controller.getChatButtons().entrySet()) 
							{
								JButton xd = rd.getValue();
								xd.setBounds(10, 0, 150, 80);
							}

							controller.getClientWindow().remove(controller.getClientWindow().getChat());
							controller.getClientWindow().addFeedback(new JTextArea("Welcome to the Chat\n Please Start A Chat", 20, 72));
							controller.getClientWindow().getReciever().setText("reciever");
						}
	}

	private void doMessage() 
	{
		String message = controller.getClientWindow().getInput().getText();
		String destination = controller.getClientWindow().getReciever().getText();
		if (destination.equals("reciever"))
			return;
		controller.getClient().chat(destination, message);
		controller.getClientWindow().getFeedback().append("You : " + message + "\n");
		controller.getClientWindow().getInput().setText("");
	}


	public void keyReleased(KeyEvent e) 
	{
		if (e.getSource().equals(controller.getClientWindow().getInput()))
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				doMessage();
	}

	public void keyPressed(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
}
