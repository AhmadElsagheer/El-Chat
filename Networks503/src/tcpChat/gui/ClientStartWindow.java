package tcpChat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ClientStartWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea feedback = new JTextArea("Welcome to the Chat room\n", 80, 72);
	private JTextArea def = new JTextArea("Welcome to the Chat\n Please Start A Chat", 20, 72);
	private JLabel reciever = new JLabel("reciever", SwingConstants.CENTER), welcome = new JLabel("welcome", SwingConstants.CENTER);
	private JTextField input = new JTextField();
	
	private JButton closeButton = new JButton(new ImageIcon("resources/images/closeButton.png")),
	sendButton = new JButton("Send"), startButton = new JButton("Start Chat"), 
	updateButton = new JButton("Update"), closeChatButton = new JButton("close chat"), 
	logButton = new JButton("Log Off");
	
	private JScrollPane chat, mList;
	private JList<String> list;
	private JPanel users = new JPanel();

	public ClientStartWindow() 
	{
		super("El Chat");
		setContentPane(new JLabel(new ImageIcon("resources/images/clientSWBack.jpg")));
		setSize(1366, 725);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setLayout(null);

		addMiddlePannel();
		addLeftPannel();
		addUpperPannel();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	
	public void removeChateButtons(JButton x) 
	{
		users.remove(x);
		repaint();
		validate();
	}

	public void addChatButtons(int i, JButton x) 
	{
		Color color = new Color(240, 255, 255);

		users.add(x);
		x.setBounds(10 + i * 163, 0, 150, 80);
		x.setBackground(color);
	}
	
	private void addUpperPannel()
	{
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		Color color = new Color(240, 255, 255);

		getContentPane().add(closeButton);
		closeButton.setBounds(1300, 10, 30, 30);
		closeButton.setOpaque(true);
		closeButton.setBorder(null);
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setOpaque(false);

		welcome.setBounds(20, 50, 1310, 50);
		welcome.setBorder(border);
		welcome.setOpaque(true);
		welcome.setBackground(color);
		getContentPane().add(welcome);

		users.setBounds(20, 110, 1310, 80);
		users.setBorder(null);
		users.setOpaque(false);
		users.setLayout(null);
		getContentPane().add(users);

	}

	private void addLeftPannel() 
	{
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		Color color = new Color(240, 255, 255);
		String categories[] = { "Click Update to View Online Members" };

		getContentPane().add(reciever);
		reciever.setBounds(20, 200, 440, 50);
		reciever.setBorder(border);
		reciever.setOpaque(true);
		reciever.setBackground(color);

		setmList(categories);

		getContentPane().add(startButton);
		startButton.setBounds(20, 570, 140, 100);
		startButton.setBackground(color);

		getContentPane().add(logButton);
		logButton.setBounds(170, 570, 140, 100);
		logButton.setBackground(color);

		getContentPane().add(closeChatButton);
		closeChatButton.setBounds(320, 570, 140, 100);
		closeChatButton.setBackground(color);

	}

	private void addMiddlePannel() 
	{
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		Color color = new Color(240, 255, 255);

		addFeedback(def);

		getContentPane().add(input);
		input.setBounds(500, 570, 680, 100);
		input.setBorder(border);
		input.setBackground(color);

		getContentPane().add(sendButton);
		sendButton.setBounds(1190, 570, 140, 100);
		sendButton.setBackground(color);
	}

	public void addFeedback(JTextArea feedback2) 
	{
		feedback = feedback2;
		feedback.setEditable(false);
		SetChat(feedback);
	}

	private void SetChat(JTextArea feedback2) 
	{
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		Color color = new Color(240, 255, 255);
		chat = new JScrollPane(feedback2);
		chat.setBounds(500, 200, 830, 360);
		chat.setBorder(border);
		chat.setOpaque(true);
		chat.setBackground(color);
		getContentPane().add(chat);
		repaint();
		validate();
	}
	

	public JButton getCloseChatButton() { return closeChatButton; }

	public JScrollPane getChat() { return chat; }

	public JTextArea getDef() {	return def; }

	public JList<String> getList() { return list; }

	public JButton getCloseButton() { return closeButton; }

	public JButton getSendButton() { return sendButton; }

	public JTextField getInput() { return input; }

	public JTextArea getFeedback() { return feedback; }

	public JLabel getReciever() { return reciever; }

	public JScrollPane getmList() { return mList; }

	public void setmList(String[] arr) 
	{
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		Color color = new Color(240, 255, 255);

		list = new JList<String>(arr);
		this.mList = new JScrollPane(list);
		mList.setBounds(20, 260, 440, 300);
		mList.setOpaque(true);
		getContentPane().add(mList);
		mList.setBorder(border);
		mList.setBackground(color);
		repaint();
		validate();
	}

	public JButton getUpdateButton() { return updateButton; }

	public JButton getLogButton() {	return logButton; }

	public JButton getStartButton() { return startButton; }

	public void setWelcome(String x) { welcome.setText(x); }

	public JPanel getUsers() { return users; }

}
