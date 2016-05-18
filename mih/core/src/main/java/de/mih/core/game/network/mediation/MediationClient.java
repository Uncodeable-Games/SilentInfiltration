package de.mih.core.game.network.mediation;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.*;

import de.mih.core.engine.network.mediation.MediationNetwork.*;
import de.mih.core.engine.network.server.Connection;
import de.mih.core.engine.network.server.DatagramReceiveHandler;
import de.mih.core.engine.network.server.UDPClient;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.game.gamestates.LobbyState;
import de.mih.core.game.network.GameClient;
import de.mih.core.game.network.GameServer;

public class MediationClient implements DatagramReceiveHandler
{
	UDPClient client;
	String name;
	private ChatFrame chatFrame;
	HashMap<Integer, Lobby> lobbies;
	
	int tcpPort, udpPort;
	
	
	
	//DEMO
	public static final String MEDIATIONSERVER = "127.0.0.1";
	protected GameServer gameServer;
	protected GameClient gameClient;

	public MediationClient() throws IOException
	{
		client = new UDPClient(MEDIATIONSERVER, MediationNetwork.udpPort);
		

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
	//	MediationNetwork.register(client);
		client.setDatagramReceiveHandler(this);
		String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server",
				JOptionPane.QUESTION_MESSAGE, null, null, MEDIATIONSERVER);
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
				ChatDatagram chatMessage = new ChatDatagram();
				chatMessage.message = chatFrame.getSendText();
				client.sendData(chatMessage, true);
			}
		});
		chatFrame.setLobbyUpdateListener(new Runnable() {
			
			@Override
			public void run()
			{
				client.sendData(new RequestLobbyUpdate(), true);
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
				//this send has somehow to come from the server itself?
				try
				{
					newLobby.lobby.tcpPort = tcpPort;
					newLobby.lobby.udpPort = udpPort;
					client.sendData(newLobby, true);
					client.close();
					MediationClient.this.gameServer = new GameServer( MediationNetwork.tcpPort, MediationNetwork.udpPort, MEDIATIONSERVER);
					
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
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

		chatFrame.selectCallback = new listSelect() {

			@Override
			public void selected(Lobby selected)
			{
				RequestLobbyJoin joinRequest = new RequestLobbyJoin();
				joinRequest.targetLobby = selected;
				joinRequest.targetAddress = selected.address;
				client.sendData(joinRequest, true);
				new Thread("Connect") {
					public void run()
					{
//						while(true)
//						{
							try
							{
								MediationClient.this.gameClient = new GameClient();
								client.close();
								gameClient.connect(5000, selected.address, selected.tcpPort, selected.udpPort);
								// Server communication after connection can go here, or in
								// Listener#connected().
								//break;
							}
							catch (IOException ex)
							{
								ex.printStackTrace();
							}
//						}
					}
				}.start();
			}

		};
		// We'll do the connect on a new thread so the ChatFrame can show a
		// progress bar.
		// Connecting to localhost is usually so fast you won't see the progress
		// bar.
//		new Thread("Connect") {
//			public void run()
//			{
//				try
//				{
//					client.start();
//					connected(null);
//				}
//				catch (IOException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.exit(1);
//				}
////				try
////				{
////					//client.connect(5000, host, MediationNetwork.tcpPort, MediationNetwork.udpPort);
////					// Server communication after connection can go here, or in
////					// Listener#connected().
////				}
////				catch (IOException ex)
////				{
////					ex.printStackTrace();
////					System.exit(1);
////				}
//			}
//		}.start();
		System.out.println("starting");
		client.start();
		System.out.println("started!");
		//connected(client.getServerConnection());
	}

//	@Override
	public void connected(Connection connection)
	{
		RegisterName registerName = new RegisterName();
		registerName.name = name;
		client.sendTo(connection, registerName, true);
		System.out.println("send register name: " + name);
	}

	@Override
	public void receive(Connection connection, BaseDatagram object)
	{
		//System.out.println(connection.getID());
		if (object instanceof ExternalInformation)
		{
			ExternalInformation ext = (ExternalInformation) object;
			this.udpPort = ext.udpPort;
			this.tcpPort = ext.tcpPort;
			System.out.println(ext.address + " " + ext.tcpPort + " " + ext.udpPort);
			return;
		}
		if (object instanceof UpdateNames)
		{
			UpdateNames updateNames = (UpdateNames) object;
			chatFrame.setNames(updateNames.names);
			return;
		}

		if (object instanceof ChatDatagram)
		{
			ChatDatagram chatMessage = (ChatDatagram) object;
			chatFrame.addMessage(chatMessage.message);
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

	//@Override
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
	public static interface listSelect
	{
		void selected(Lobby selectedLobby);
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
		public listSelect selectCallback;

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
//				JPanel panel = new JPanel(new BorderLayout());
//				contentPane.add(panel, "progress");
//				panel.add(new JLabel("Connecting to " + host + "..."));
//				{
//					panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
//					progressBar.setIndeterminate(true);
//				}
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
						lobbyList.addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent evt) {
						        JList list = (JList)evt.getSource();
						        if (evt.getClickCount() == 2) {

						            // Double-click detected
						            int index = list.locationToIndex(evt.getPoint());
						            Lobby selectedLobby = (Lobby)list.getModel().getElementAt(index);
						            selectCallback.selected(selectedLobby);
						        }
							}
						});
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
						model.addElement(l);
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
		try
		{
			new MediationClient();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
