package de.mih.core.engine.network;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import javax.swing.*;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import de.mih.core.engine.network.MediationNetwork.*;
import de.mih.core.game.gamestates.LobbyState;

public class MediationClient extends Listener
{
	Client client;
	String name;
	private ChatFrame chatFrame;
	
	//HashMap<Integer, Lobby> lobbies;

	public MediationClient()
	{
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		MediationNetwork.register(client);
		client.addListener(this);

		String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server",
				JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		final String host = input.trim();

		// Request the user's name.
		input = (String) JOptionPane.showInputDialog(null, "Name:", "Connect to chat server",
				JOptionPane.QUESTION_MESSAGE, null, null, "Test");
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		name = input.trim();

		// All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter
		// the KryoNet example code.
		chatFrame = new ChatFrame(host);
		// This listener is called when the send button is clicked.
		chatFrame.setSendListener(new Runnable() {
			public void run()
			{
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.text = chatFrame.getSendText();
				client.sendTCP(chatMessage);
			}
		});
		chatFrame.setLobbyUpdateListener(new Runnable() {
			
			@Override
			public void run()
			{
				client.sendTCP(new RequestLobbyUpdate());
			}
		});
		chatFrame.setCreateLobbyListener(new Runnable() {
			public void run()
			{
				RegisterLobby newLobby = new RegisterLobby();
				newLobby.lobby = new Lobby();
				newLobby.lobby.name = "TestLobby";
				newLobby.lobby.players = 1;
				//newLobby.lobby.address = 
				client.sendTCP(newLobby);
			}
		});
		// This listener is called when the chat window is closed.
		chatFrame.setCloseListener(new Runnable() {
			public void run()
			{
				client.stop();
			}
		});
		chatFrame.setVisible(true);

		// We'll do the connect on a new thread so the ChatFrame can show a
		// progress bar.
		// Connecting to localhost is usually so fast you won't see the progress
		// bar.
		new Thread("Connect") {
			public void run()
			{
				try
				{
					client.connect(5000, host, MediationNetwork.port);
					// Server communication after connection can go here, or in
					// Listener#connected().
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
	}

	@Override
	public void connected(Connection connection)
	{
		RegisterName registerName = new RegisterName();
		registerName.name = name;
		client.sendTCP(registerName);
	}

	@Override
	public void received(Connection connection, Object object)
	{
		if (object instanceof UpdateNames)
		{
			UpdateNames updateNames = (UpdateNames) object;
			chatFrame.setNames(updateNames.names);
			return;
		}

		if (object instanceof ChatMessage)
		{
			ChatMessage chatMessage = (ChatMessage) object;
			chatFrame.addMessage(chatMessage.text);
			return;
		}
		
		if (object instanceof UpdateLobbies)
		{
			UpdateLobbies update = (UpdateLobbies) object;
			//this.lobbies = update.lobbies;
			chatFrame.setLobbies(update.lobbies);
		}
		
		if(object instanceof RegisterLobby)
		{
			
		}
	}

	@Override
	public void disconnected(Connection connection)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				// Closing the frame calls the close listener which will stop
				// the client's update thread.
				chatFrame.dispose();
			}
		});
	}

	static private class ChatFrame extends JFrame
	{
		CardLayout cardLayout;
		JProgressBar progressBar;
		JList messageList;
		JTextField sendText;
		JButton newLobby;
		JButton sendButton;
		JButton lobbyUpdate;
		JList nameList;
		JList lobbyList;

		public ChatFrame(String host)
		{
			super("Chat Client");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(640, 200);
			setLocationRelativeTo(null);

			Container contentPane = getContentPane();
			cardLayout = new CardLayout();
			contentPane.setLayout(cardLayout);
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "progress");
				panel.add(new JLabel("Connecting to " + host + "..."));
				{
					panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
					progressBar.setIndeterminate(true);
				}
			}
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "chat");
				{
					JPanel topPanel = new JPanel(new GridLayout(1, 2));
					panel.add(topPanel);
					{
						topPanel.add(new JScrollPane(messageList = new JList()));
						messageList.setModel(new DefaultListModel());
					}
					{
						topPanel.add(new JScrollPane(nameList = new JList()));
						nameList.setModel(new DefaultListModel());
					}
					{
						topPanel.add(new JScrollPane(lobbyList = new JList()));
						lobbyList.setModel(new DefaultListModel());
					}
					DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
						public void setSelectionInterval(int index0, int index1)
						{
						}
					};
					messageList.setSelectionModel(disableSelections);
					nameList.setSelectionModel(disableSelections);
				}
				{
					JPanel bottomPanel = new JPanel(new GridBagLayout());
					panel.add(bottomPanel, BorderLayout.SOUTH);
					bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
							GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
					bottomPanel.add(newLobby = new JButton("New Lobby"));//, new GridBagConstraints(1, 0, 1, 1, 0, 0,
					bottomPanel.add(lobbyUpdate = new JButton("Update Lobbies"));//, new GridBagConstraints(1, 0, 1, 1, 0, 0,
		//GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
				}
			}

			sendText.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					sendButton.doClick();
				}
			});
		}
		
		public void setLobbies(HashMap<Integer, Lobby> lobbies)
		{

			EventQueue.invokeLater(new Runnable() {
				public void run()
				{
					DefaultListModel model = (DefaultListModel) lobbyList.getModel();
					model.removeAllElements();
					for(Lobby l : lobbies.values())
					{
						model.addElement(l.name + " " + l.address);
						System.out.println(l.name + " " + l.address);
						messageList.ensureIndexIsVisible(model.size() - 1);
					}
				}
			});
			
		
		}

		public void setLobbyUpdateListener(Runnable runnable)
		{
			lobbyUpdate.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					runnable.run();
				}
			});
		}
		public void setCreateLobbyListener(Runnable runnable)
		{
			newLobby.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					runnable.run();
				}
			});
		}

		public void setSendListener(final Runnable listener)
		{
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt)
				{
					if (getSendText().length() == 0)
						return;
					listener.run();
					sendText.setText("");
					sendText.requestFocus();
				}
			});
		
		}

		public void setCloseListener(final Runnable listener)
		{
			addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt)
				{
					listener.run();
				}

				public void windowActivated(WindowEvent evt)
				{
					sendText.requestFocus();
				}
			});
		}

		public String getSendText()
		{
			return sendText.getText().trim();
		}

		public void setNames(final String[] names)
		{
			// This listener is run on the client's update thread, which was
			// started by client.start().
			// We must be careful to only interact with Swing components on the
			// Swing event thread.
			EventQueue.invokeLater(new Runnable() {
				public void run()
				{
					cardLayout.show(getContentPane(), "chat");
					DefaultListModel model = (DefaultListModel) nameList.getModel();
					model.removeAllElements();
					for (String name : names)
						model.addElement(name);
				}
			});
		}

		public void addMessage(final String message)
		{
			EventQueue.invokeLater(new Runnable() {
				public void run()
				{
					DefaultListModel model = (DefaultListModel) messageList.getModel();
					model.addElement(message);
					messageList.ensureIndexIsVisible(model.size() - 1);
				}
			});
		}
	}
	
	public static void main (String[] args) {
		new MediationClient();
	}
}
